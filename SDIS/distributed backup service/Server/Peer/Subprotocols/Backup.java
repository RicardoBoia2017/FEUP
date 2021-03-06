package Server.Peer.Subprotocols;

import Server.Message.Message;
import Server.Message.MessageType;
import Server.Peer.Peer;
import Server.Peer.Utilities.Pair;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static Server.Peer.Utilities.Utilities.createFile;
import static Server.Peer.Utilities.Utilities.generateFileId;

public class Backup {

    private Integer replicationDegree;
    private Peer peer;
    private String fileId = null;
    private File file;
    private static final Integer maxChunkSize = 64000;
    private static final Integer maxNumTries = 5;
    private static final Integer minSleepTime = 1000;
    private static final Integer maxRandomDelay = 400;
    private static final Integer enhancementTime = 2000;


    public Backup(Peer peer) {
        this.peer = peer;
    }

    public Backup(String filepath, Integer replicationDegree, Peer peer) throws IOException, NoSuchAlgorithmException {
        this(peer);
        this.file = new File(filepath);
        this.fileId = generateFileId(file);
        this.replicationDegree = replicationDegree;
        System.out.println("New Backup Protocol Started");
    }

    public Backup(Peer peer, String fileID, Integer replicationDegree) {
        this.peer = peer;
        this.fileId = fileID;
        this.replicationDegree = replicationDegree;
        System.out.println("New Backup Protocol Started");

    }


    public void readChunks() throws IOException, IllegalAccessException {

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Long fileToRead = file.length();
        int chunkNo = 0;
        int bytesRead = 0;
        int lastBytesRead = 0;
        do {
            Integer chunkSize = (int) Math.min(fileToRead, maxChunkSize);
            System.out.println(chunkSize);
            byte[] chunk = new byte[chunkSize];
            bytesRead = inputStream.read(chunk);
            sendChunk(chunk, chunkNo++);
            fileToRead -= bytesRead;
            lastBytesRead = bytesRead;
        } while (fileToRead > 0);

        inputStream.close();

        if (lastBytesRead == maxChunkSize) {
            sendChunk(new byte[0], chunkNo);
        }

        System.out.println("Chucks Successfully Read");

    }

    public void sendChunk(byte[] chunk, int chunkNo) throws IllegalAccessException {
        new Message(new String[]{MessageType.PUTCHUNK.toString(), String.valueOf(peer.getProtocolVersion()), String.valueOf(peer.getServerID()), fileId, String.valueOf(chunkNo), String.valueOf(replicationDegree)}, chunk, this.peer).start();
    }

    public void sendPutChunk(Message message) {
        System.out.println("Sending PutChunk");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        int desiredReplicationDegree = message.getReplicationDegree();
        int attempts = 0;

        do {
            message.send(this.peer.getMDB());
            try {
                executor.awaitTermination((long) minSleepTime * 2 ^ attempts, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (peer.getCurrentReplicationDegree(new Pair<>(message.getFileID(), message.getChunkNo())) < desiredReplicationDegree && attempts++ < maxNumTries);

        if (attempts >= maxNumTries) {
            System.out.println("Desired replication degree not achieved");
        } else {
            System.out.println("Desired replication degree achieved");
        }

    }

    public void sendStored(Message message) throws IllegalAccessException {
        System.out.println("Sending Stored");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        try {
            executor.awaitTermination((long) (Math.random() * maxRandomDelay), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Message(new String[]{MessageType.STORED.toString(), String.valueOf(peer.getProtocolVersion()), String.valueOf(peer.getServerID()), message.getFileID(), String.valueOf(message.getChunkNo())}, this.peer).send(this.peer.getMC());
    }

    public void receivePutChunk(Message message) throws IOException, IllegalAccessException {
        System.out.println("Received PutChuck");

        Long availableSpace = peer.getAvailableSpace();
        Integer bodyLength = message.getBodySpace();
        if (availableSpace > bodyLength) {
            String path = Peer.getBaseStorageDir() + peer.getServerID() + "/" + message.getFileID() + "/" + message.getChunkNo();
            FileOutputStream outputStream = createFile(path);
            if (outputStream == null) {
                return;
            }
            outputStream.write(message.getBody(), 0, bodyLength);
            peer.addUsedSpace(Long.valueOf(bodyLength));
            peer.addChunkToStorage(new Pair<>(message.getFileID(), message.getChunkNo()), message.getReplicationDegree());
            outputStream.close();
            System.out.println("Received PutChunk successfully");
            sendStored(message);
        } else {
            System.out.println("Not enough space");
        }
    }


    public void receivePutChunkEnhancement(Message message) {
        Pair<String, Integer> key = new Pair<>(message.getFileID(), message.getChunkNo());
        if (!peer.hasChunk(key)) {
            int oldCurrentReplicationDegree = peer.getCurrentReplicationDegree(key);
            try {
                Thread.sleep((long) (Math.random() * enhancementTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int desiredReplicationDegree = message.getReplicationDegree();
            int currentReplicationDegree = peer.getCurrentReplicationDegree(key);
            if (currentReplicationDegree < desiredReplicationDegree && currentReplicationDegree == oldCurrentReplicationDegree) {
                try {
                    receivePutChunk(message);
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
