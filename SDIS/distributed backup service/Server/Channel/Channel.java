package Server.Channel;

import Server.Message.Message;
import Server.Peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public abstract class Channel {
    private Channel _channel = this;
    private MulticastSocket mcastSocket;
    private Thread thread;
    private Peer peer;
    private Integer port;
    private InetAddress address;
    private static final Integer bufferSize = 64000 + 1000;


    Channel(InetAddress address, int port, Peer peer) throws IOException {
        this.port = port;
        this.address = address;
        this.mcastSocket = new MulticastSocket(port);
        this.mcastSocket.joinGroup(address);
        this.mcastSocket.getPort();

        this.thread = new Thread(new ChannelRunnable());
        this.peer = peer;
    }

    public void listen(){
        thread.start();
    }

    public class ChannelRunnable implements Runnable {
        public void run() {
            while (true){
                try {
                    _channel.receivePacket();
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void receivePacket() throws IOException, IllegalAccessException {
        byte[] buffer = new byte[bufferSize];
        DatagramPacket receivePacket;

        receivePacket = new DatagramPacket(buffer, buffer.length);
        this.mcastSocket.receive(receivePacket);

  /*      InetAddress receiveAddress = receivePacket.getAddress();
        int receivePort = receivePacket.getPort();
        String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Received Request: " + request);*/

        handleRequest(receivePacket);
    }

    private void handleRequest(DatagramPacket receivePacket) throws IOException, IllegalAccessException {
        Message message = new Message(receivePacket, this.peer);
        message.start();
    }

    public int getPort(){
        return port;
    }

    public InetAddress getAddress(){
        return address;
    }

}
