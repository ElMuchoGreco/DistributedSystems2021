import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AppNodeHandler extends Node implements Runnable {
    AppNode publisher;
    AppNode consumer;
    ServerSocket socket;
    InetAddress ip;
    int port;
    ServerSocket providerSocket;
    Socket Connection;
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    Message message;
    Boolean check;

    public AppNodeHandler(AppNode publisher, AppNode consumer) {
        this.publisher = publisher;
        this.consumer = consumer;
        this.port = publisher.getPort();
        try {
            ip = InetAddress.getByName("192.168.2.2");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    void connectAndNotifyBrokers() {
        int port = 7654;
        int port2 = 8761;
        int port3 = 9876;
        List<Integer> ports = new ArrayList<Integer>();
        ports.add(port);
        ports.add(port2);
        ports.add(port3);
        for (Integer thePort : ports) {
            try {
                requestSocket = new Socket("192.168.2.2", thePort);
                out = new ObjectOutputStream(requestSocket.getOutputStream());
                System.out.println("Connection Established!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                out.writeObject(publisher.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void openServer() {
        try {
            providerSocket = new ServerSocket(publisher.getPort(), 10, this.ip);

        } catch (IOException e) {
            e.printStackTrace();
        }
        int i = 1;
        while (true) {
            try {
                Connection = providerSocket.accept();
                in = new ObjectInputStream(Connection.getInputStream());
                message = (Message) in.readObject();
                if (message != null) {
                    System.out.println(message.toString());
                    System.out.println("Message rcv'd");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.err.println("Message Not Found" + i);
                i++;
            }
            AppNode pblsh = new AppNode();

            pblsh.push();
        }
    }

    void connect(int port){
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            requestSocket = new Socket("192.168.2.2", port);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            System.out.println("Message created.");
            out.writeObject(consumer.getMessage());
            in = new ObjectInputStream(requestSocket.getInputStream());
            try {
                message = (Message) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            check = message.check;
            if (!check) {
                System.out.println("I change server");
                this.consumer.setPort(message.port);
                System.out.println("Server Changed");
            } else {
                System.out.println(message.getVideoFile());
                VideoFile tempMusicFile = new VideoFile();
                int i=0;
                while (true){
                    try {
                        Message message = (Message) in.readObject();
                        if(message.getTransfer()){
                            tempMusicFile.setData(message.getByteChunk());
                            i++;
                        }
                        if(!message.getTransfer()){
                            if(message.getChannelName().equals("File not found")){
                                System.err.println("Video does not exist");
                                break;
                            }
                            else if(message.getChannelName().equals("Channel not found")){
                                System.out.println("Channel not found");
                                break;
                            }
                            else if(message.getChannelName().equals("Video not found")){
                                System.out.println("Video not found");
                                break;
                            }
                            else {
                                System.out.println("Video Received");
                                break;
                            }
                        }
                    }catch (ClassNotFoundException e){
                        e.printStackTrace();
                    }catch (EOFException e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        connectAndNotifyBrokers();
        openServer();
        connect(port);
        System.out.println("Running...");
    }

    public static void main(String[] args) {
        AppNode publisher1 = new AppNode("mike","skyrim.mp4", true);
        AppNode consumer1 = new AppNode("mparmpa-nidis","", false);
        new Thread(new AppNodeHandler(publisher1, consumer1)).start();
    }
}
