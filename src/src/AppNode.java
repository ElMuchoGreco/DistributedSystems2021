import java.io.*;
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

    public ArrayList<Value> generateChunks(String path, String video_name) {

        ArrayList<Value> chunks = new ArrayList<>();

        try {
            File file = new File(path + video_name);//File read from Source folder to Split.
            if (file.exists()) {

                String videoFileName = file.getName().substring(0, file.getName().lastIndexOf(".")); // Name of the videoFile without extension
                File splitFile = new File(path + "Videos_Split");//Destination folder to save.
                if (!splitFile.exists()) {
                    splitFile.mkdirs();
                    System.out.println("Directory Created -> "+ splitFile.getAbsolutePath());
                }

                int i = 1;// Files count starts from 1
                InputStream inputStream = new FileInputStream(file);
                String videoFile = splitFile.getAbsolutePath() +"/"+ String.format("%02d", i) +"_"+ file.getName();// Location to save the files which are Split from the original file.
                OutputStream outputStream = new FileOutputStream(videoFile);
                System.out.println("File Created Location: "+ videoFile);
                chunks.add(new Value(new VideoFile(videoFile, "", "")));
                int totalPartsToSplit = 3;// Total files to split.
                int splitSize = inputStream.available() / totalPartsToSplit;
                int streamSize = 0;
                int read = 0;
                while ((read = inputStream.read()) != -1) {

                    if (splitSize == streamSize) {
                        if (i != totalPartsToSplit) {
                            i++;
                            String fileCount = String.format("%02d", i); // output will be 1 is 01, 2 is 02
                            videoFile = splitFile.getAbsolutePath() +"/"+ fileCount +"_"+ file.getName();
                            chunks.add(new Value(new VideoFile(videoFile, "", "")));
                            outputStream = new FileOutputStream(videoFile);
                            System.out.println("File Created Location: "+ videoFile);
                            streamSize = 0;
                        }
                    }
                    outputStream.write(read);
                    streamSize++;
                }

                inputStream.close();
                outputStream.close();
                System.out.println("Total files Split ->"+ totalPartsToSplit);
            } else {
                System.err.println(file.getAbsolutePath() +" File Not Found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Value chunk : chunks) {
            System.out.println(chunk.videoFile.videoPath);
        }
        return chunks;
    }
}
