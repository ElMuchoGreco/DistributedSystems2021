import java.util.ArrayList;

public class VideoFile {

    String videoPath;
    String videoName;
    String channelName;

    private ArrayList<String> hashtags = new ArrayList<>();

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }


    VideoFile(String videoPath, String videoName, String channelName) {
        this.videoPath = videoPath;
        this.videoName = videoName;
        this.channelName = channelName;
    }

}
