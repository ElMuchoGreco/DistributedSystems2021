import java.util.ArrayList;

public interface Publisher {

    public void push();
    ArrayList<Value> generateChunks(String path, String video_name);
}
