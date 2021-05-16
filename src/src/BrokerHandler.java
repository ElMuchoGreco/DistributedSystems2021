import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BrokerHandler extends Thread implements Serializable {

    ObjectInputStream in;
    ObjectOutputStream out;
    Socket stopCon = null;
    String channelName, video;
    AppNode appNode;
    ArrayList<String> hashtags = new ArrayList<>();
    BigInteger theirKeys;
    Broker MyBroker;
    Message message;
    Socket stopComms;
    VideoFile videoFile;
    Object object;
    boolean changeCheck=false;
    boolean CorrectBroker=false;

    public BrokerHandler(Broker broker) throws NullPointerException {
        stopComms = broker.getConnection();
        try {
            in = new ObjectInputStream(stopComms.getInputStream());
            out = new ObjectOutputStream(stopComms.getOutputStream());
            out.flush();
            try {
                this.message = (Message)in.readObject();
                this.channelName = message.getChannelName();
                this.hashtags = message.getHashtags();
                this.MyBroker = broker;
                this.object = message. getObject();
                this.videoFile = message.getVideoFile();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void disconnectClient(Socket connection){
        changeCheck=true;
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void pull(Message request){
        AppNode correctPublisher = null;
        AppNode tempConsumer = (AppNode) object;
        if (MyBroker.GetPublishers().size()!=0) {
            boolean flag = false;
            for (AppNode publisher : MyBroker.GetPublishers()) {
                if (publisher.getVideos().contains(tempConsumer.getChannelName())) {
                    correctPublisher = publisher;
                    flag = true;

                    Socket requestSocket = null;
                    ObjectOutputStream publisherOut = null;
                    ObjectInputStream publisherIn = null;

                    try {
                        requestSocket = new Socket("192.168.2.2", correctPublisher.getPort());
                        publisherOut = new ObjectOutputStream(requestSocket.getOutputStream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Message songRequest=new Message(request.getChannelName(),request.getVideoFile().videoName);
                    try {
                        System.out.println("Fetching video");
                        publisherOut.writeObject(songRequest);
                        publisherIn = new ObjectInputStream(requestSocket.getInputStream());
                        while(true) {
                            Message msg = (Message) publisherIn.readObject();

                            System.out.println("Pushing\n"+msg.toString()+"\nto"+tempConsumer.getPort()+"\n");
                            if(!msg.getTransfer()){
                                if(msg.video.equals("File not found")){
                                    System.err.println("Video does not exist");

                                }else if(msg.video.equals("Video not found")){
                                    System.err.println("Video not found");
                                }
                                else {
                                    System.out.println("Video Received");
                                    System.out.println("Whole Video Transferred!");
                                }
                                out.writeObject(msg);
                                break;
                            }
                            out.writeObject(msg);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    catch (ClassNotFoundException e){
                        e.printStackTrace();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    break;
                }
            }
            if(!flag){
                Message message=new Message("Channel not found");
                message.setTransfer(false);
                try {
                    out.writeObject(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }



    public void run(){
        if(!this.appNode.isPublisher()){
            calculateMessageKeys(this.message);
            checkBroker(this.MyBroker, appNode);
            if(!changeCheck) {
                pull(this.message);
            }
        }
        else if(this.appNode.isPublisher()){
            System.out.println("This is a publisher");
        }
    }


    public void calculateMessageKeys(Message request) throws NullPointerException {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.reset();
        m.update(channelName.getBytes());
        byte[] digest = m.digest();
        theirKeys = new BigInteger(1,digest);
        BigInteger mod=new BigInteger("35");
        theirKeys=theirKeys.mod(mod);
        System.out.println(theirKeys);
    }

    public void checkBroker(Broker broker, AppNode consumer){
        int thePort=0;
        int theirIntKeys=theirKeys.intValue();
        System.out.println(theirIntKeys+"theirkeys");
        if(theirIntKeys>Node.MAX.intValue()){
            theirIntKeys=theirIntKeys%Node.MIN.intValue();
        }
        for(Broker broker1:Node.getBrokers()){
            if(theirIntKeys<=broker1.myKeys.intValue()){
                if(broker1 == MyBroker){
                    CorrectBroker=true;
                    System.out.println("correct broker found port is" + broker.port);
                }
                thePort = broker1.port;
                break;
            }
        }
        if(!CorrectBroker){
            Message answer= new Message(this.channelName,thePort,false);
            try {
                out.writeObject(answer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println(broker.Name + "Client changing server");
            disconnectClient(stopCon);
        }else{
            consumer.Register(broker, channelName);
            System.out.println(broker.Name + "Client Connected and Registered");
            Message answer=(new Message("Searching video"));
            try {
                out.writeObject(answer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
