import java.util.ArrayList;

public class AppNode extends Node implements Publisher, Consumer{
    private String username, password, email, channelName;
    private int videosPublished;
    private ArrayList<VideoFile> videos = new ArrayList<>();
    private ArrayList<String> hashtags = new ArrayList<>();

    public AppNode() {
        this.hashtags = setHashtags();
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

    public void push() {

    }

    public void pull() {

    }
}
