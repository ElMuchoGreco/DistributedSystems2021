import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AppNode extends Node {
    private String username, password, email, channelName, videoName;
    private int videosPublished, port;
    List<Integer> givenList = Arrays.asList(7654, 8761, 9876);
    private Message message;
    private ArrayList<VideoFile> videos = new ArrayList<>();
    private ArrayList<String> hashtags = new ArrayList<>();
    private boolean isPublisher;
    InetAddress ip;
    Socket connection;
    ObjectInputStream in;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Integer> getGivenList() {
        return givenList;
    }

    public void setGivenList(List<Integer> givenList) {
        this.givenList = givenList;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setVideos(ArrayList<VideoFile> videos) {
        this.videos = videos;
    }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public Socket getConnection() {
        return connection;
    }

    public void setConnection(Socket connection) {
        this.connection = connection;
    }

    public Socket getRequestSocket() {
        return requestSocket;
    }

    public void setRequestSocket(Socket requestSocket) {
        this.requestSocket = requestSocket;
    }

    public static int getChunkSize() {
        return CHUNK_SIZE;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ServerSocket getProviderSocket() {
        return providerSocket;
    }

    public void setProviderSocket(ServerSocket providerSocket) {
        this.providerSocket = providerSocket;
    }

    ObjectOutputStream out;
    ServerSocket providerSocket;
    Socket Connection;
    Socket requestSocket;
    private static final int CHUNK_SIZE = 64000;

    public AppNode() {
        this.hashtags = setHashtags();
    }

    public AppNode(String channelName, String videoName, boolean isPublisher) {
        this.isPublisher = isPublisher;
        this.channelName = channelName;
        this.videoName = videoName;
        Random rand = new Random();
        if(!isPublisher) {
            this.port = givenList.get(rand.nextInt(givenList.size()));
            this.message = new Message(channelName, this, videoName);
        }
    }

    private ArrayList<String> setHashtags() {
        ArrayList<String> ht = new ArrayList<>();
        for (VideoFile v : videos) {
            for (String h : v.getHashtags()) {
                if (!ht.contains(h)) ht.add(h);
            }
        }
        return ht;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getVideosPublished() {
        return videosPublished;
    }

    public ArrayList<VideoFile> getVideos() {
        return videos;
    }

    public boolean isPublisher() {
        return isPublisher;
    }

    public void setPublisher(boolean set) {
        isPublisher = set;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVideosPublished(int videosPublished) {
        this.videosPublished = videosPublished;
    }

    public static byte[] inputStreamToByteArray(InputStream inStream) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        try {
            while ((bytesRead = inStream.read(buffer)) > 0) {
                baos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public synchronized void push() {

        VideoFile video = null;
        Message tempmsg = null;

        try {
            video = new VideoFile("", "skyrim.mp4", "mike", inputStreamToByteArray(new FileInputStream(new File("", "skyrim.mp4"))));

            try {
                for (int i = 0; i <= video.getData().length / CHUNK_SIZE; i++) {
                    byte[] chunk = extractByteChunk(i, video.getData());
                    tempmsg = new Message(chunk);
                    out.writeObject(tempmsg);
                    Thread.sleep(1000);
                    System.out.println("CHUNK :" + tempmsg.toString() + " Sent");
                }
                tempmsg = new Message("END");
                tempmsg.setTransfer(false);
                out.writeObject(tempmsg);
                System.out.println("\n\nVideo Sent");
            } catch (FileNotFoundException ex) {
                System.err.println("File Not Found!!!");
                tempmsg = new Message("File not found");
                tempmsg.setTransfer(false);
                try {
                    out.writeObject(tempmsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            System.err.println("File Not Found!!!");
            tempmsg = new Message("File not found");
            tempmsg.setTransfer(false);
            try {
                out.writeObject(tempmsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void Register(Broker broker, String channelName) {
        broker.GetConsumers().add(this);
        System.out.println(channelName);
    }

    public byte[] extractByteChunk(int i, byte[] song) {
        byte[] chunk;
        if (i < song.length / CHUNK_SIZE) {
            chunk = new byte[CHUNK_SIZE];
            for (int j = 0; j < CHUNK_SIZE; j++) {
                chunk[j] = song[(i * CHUNK_SIZE) + j];
            }
        } else {
            chunk = new byte[song.length - ((i) * CHUNK_SIZE)];
            for (int j = 0; j < song.length - ((i) * CHUNK_SIZE); j++) {
                chunk[j] = song[(i * CHUNK_SIZE) + j];
            }
        }
        return chunk;
    }
}

