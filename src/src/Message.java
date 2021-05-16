import java.util.ArrayList;

public class Message {

    private String channelName;
    private ArrayList<String> hashtags = new ArrayList<>();
    private Object object;
    private VideoFile videoFile;
    private boolean transfer;
    Boolean check = true;
    int port;
    VideoFile extract;
    private byte[] byteChunk;
    String videoPart, video;

    public String getChannelName() {
        return channelName;
    }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public Object getObject() {
        return object;
    }

    public VideoFile getVideoFile() {
        return videoFile;
    }

    public byte[] getByteChunk() {
        return byteChunk;
    }

    public Message() {}
    public Message(String channelName, int port, boolean check) {
        this.channelName = channelName;
        this.port = port;
        this.check = check;
        this.transfer=true;
    }
    public Message(byte[] chunk){
        this.byteChunk=chunk;
        this.transfer=true;
    }

    public Message(String channelName, String video) {
        this.channelName = channelName;
        this.byteChunk=new byte[0];
        this.video = video;
        this.transfer=true;
    }

    public Message(String a) {
        this.channelName = a;
        this.byteChunk=new byte[0];
        this.transfer=true;
    }

    public Message(String channelName, Object entity, String video) {
        this.channelName = channelName;
        this.object = entity;
        this.byteChunk = new byte[0];
        this.video = video;
        this.transfer=true;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public void setVideoFile(VideoFile videoFile) {
        this.videoFile = videoFile;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public boolean getTransfer() {
        return transfer;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
