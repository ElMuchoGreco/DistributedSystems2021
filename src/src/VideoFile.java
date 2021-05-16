import java.util.ArrayList;

public class VideoFile {

    String videoPath;
    String videoName;
    String channelName;
    private byte[] data;

    private ArrayList<String> hashtags = new ArrayList<>();

    public String getVideoName() {
        return videoName;
    }

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] videoFile) {
        this.data = videoFile;
    }

    VideoFile() {}
    VideoFile(String videoPath, String videoName, String channelName, byte[] videoFile) {
        this.videoPath = videoPath;
        this.videoName = videoName;
        this.channelName = channelName;
        this.data = videoFile;
    }

}
