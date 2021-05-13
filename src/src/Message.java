import java.util.ArrayList;

public class Message {

    private String channelName;
    private ArrayList<String> hashtags = new ArrayList<>();
    private Object object;
    private VideoFile videoFile;

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

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public void setVideoFile(VideoFile videoFile) {
        this.videoFile = videoFile;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
