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
    String channelName;
    ArrayList<String> hashtags = new ArrayList<>();
    BigInteger theirKeys;
    Broker MyBroker;
    Message message;
    Socket stopComms;
    VideoFile videoFile;
    Object object;

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

    public void run(){

    }
}
