package raft;

import raft.States.CandidateState;
import raft.States.FollowerState;
import raft.States.State;
import raft.net.ssl.SSLChannel;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class Raft<T extends Serializable> {

    enum ServerState {
        INITIALIZING, WAITING, RUNNING, TERMINATING;
    }
    enum ClusterState {
        INITIALIZING, RUNNING, TERMINATING;
    }

	UUID ID = UUID.randomUUID();
	Integer port;
	ConcurrentHashMap<UUID, RaftCommunication> cluster = new ConcurrentHashMap<>();
	private AtomicReference<ServerState> serverState = new AtomicReference<>(ServerState.INITIALIZING);
	ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            System.out.println("Timeout");
            state.get().handleLeaderHeartBeatFailure();
        }
    };

    public void setState(State state) {
        this.state.set(state);
    }

    private AtomicReference<State> state = new AtomicReference<>(new FollowerState(this));
    private boolean isTimeout = false;

	UUID leaderID;

	//	Persistent serverState (save this to stable storage)
	Long currentTerm = 0L;
	UUID votedFor;
	RaftLog<T>[] log;
	RaftLog<T>[] entries;

	//	Volatile serverState
	Long commitIndex = 0L;
	Long lastApplied = 0L;

	// TODO we need more locks!!! (or maybe not)
	ReentrantLock lock = new ReentrantLock();

	// AtomicBoolean

	// TODO Create class (runnable) to redirect client requests (RaftForward? RaftRedirect?) Also create RPC for that (because why the hell not)

	public Raft(Integer port, InetSocketAddress cluster) {
		this.port = port;

		// Connect to known cluster
		{
			SSLChannel channel = new SSLChannel(cluster);
			if (channel.connect()) {
				this.pool.execute(new RaftDiscover(this, channel));
			} else {
				System.out.println("Connection failed!"); // DEBUG
				return; // Show better error message
			}
		}

		// Listen for new connections
		this.pool.execute(() -> {
			while (serverState.get() != ServerState.TERMINATING) {
				SSLChannel channel = new SSLChannel(port);
				if (channel.accept()) {
					pool.execute(new RaftServer<T>(this, channel));
				}
			}
		});
	}

	public Raft(Integer port) {
		this.port = port;

		// Listen for new connections
		this.pool.execute(() -> {
			while (serverState.get() != ServerState.TERMINATING) {
				SSLChannel channel = new SSLChannel(port);
				if (channel.accept()) {
					pool.execute(new RaftServer<T>(this, channel));
				}
			}
		});
	}

	public void run() {
		pool.execute(() -> {
			scheduleTimeout(); // TODO
		});
	}

	static <T extends Serializable> byte[] serialize(T variable) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     ObjectOutputStream oos = new ObjectOutputStream(baos)){
			oos.writeObject(variable);
			return Base64.getEncoder().encode(baos.toByteArray());
		} catch (Exception e) {
		//  e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	static <T extends Serializable> T deserialize(byte[] buffer) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(buffer));
		     ObjectInputStream ois = new ObjectInputStream(bais)) {
			return (T) ois.readObject();
		} catch (Exception e) {
		//  e.printStackTrace();
		}
		return null;
	}

	public boolean set(T var) {
		SSLChannel channel = connectToLeader();

		if(channel == null) {
			return false;
		}

		byte[] serObj = serialize(var);

		channel.send(RPC.callSetValue(new String(serObj)));

		String message = channel.receiveString();

		return message.equals(RPC.retSetValue(true));
	}

	public T get() {
		SSLChannel channel = connectToLeader();

		if(channel == null) {
			return null;
		}

		channel.send(RPC.callGetValue());

		String message = channel.receiveString();

		T obj = deserialize(message.split("\n")[1].getBytes());

		return obj;
	}

	public boolean delete() {
		SSLChannel channel = connectToLeader();

		if(channel == null) {
			return false;
		}

		channel.send(RPC.callDeleteValue());

		String message = channel.receiveString();

		return message.equals(RPC.retDeleteValue(true));
	}



	private SSLChannel connectToLeader() {
		if(this.leaderID == null) {
			return null;
		}

		RaftCommunication leader = this.cluster.get(this.leaderID);

		if(leader == null) {
			return null;
		}

		SSLChannel channel = new SSLChannel(leader.address);

		if (!channel.connect()) {
			System.out.println("Connection failed!"); // DEBUG
			return null; // Show better error message
		}

		return channel;
	}

	boolean setValue(T object) {
		return true;
	}

	boolean deleteValue() {
		return true;
	}

	T getValue() {
		return null;
	}

    public void scheduleTimeout() {
	    timer.schedule(timerTask, new Random().nextInt(RaftProtocol.maxRandomDelay-RaftProtocol.minRandomDelay) + RaftProtocol.minRandomDelay);
    }


}
