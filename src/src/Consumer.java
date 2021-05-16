import java.io.*;
import java.util.Arrays;

public interface Consumer {

    public void pull();
    public static void playData(String path, String video_name) {

        try {
            File splitFiles = new File(path + "Videos_Split");// get all files which are to be joined
            if (splitFiles.exists()) {
                File[] files = splitFiles.getAbsoluteFile().listFiles();
                if (files.length != 0) {
                    System.out.println("Total files to be joined: "+ files.length);

                    String joinFileName = Arrays.asList(files).get(0).getName();
                    System.out.println("Join file created with name -> "+ joinFileName);

                    String fileName = joinFileName.substring(0, joinFileName.lastIndexOf("."));// video fileName without extension
                    File fileJoinPath = new File(path + "Videos_Joined");// merge video files saved in this location

                    if (!fileJoinPath.exists()) {
                        fileJoinPath.mkdirs();
                        System.out.println("Created Directory -> "+ fileJoinPath.getAbsolutePath());
                    }

                    OutputStream outputStream = new FileOutputStream(fileJoinPath.getAbsolutePath() +"/"+ joinFileName);

                    for (File file : files) {
                        System.out.println("Reading the file -> "+ file.getName());
                        InputStream inputStream = new FileInputStream(file);

                        int readByte = 0;
                        while((readByte = inputStream.read()) != -1) {
                            outputStream.write(readByte);
                        }
                        inputStream.close();
                    }

                    System.out.println("Join file saved at -> "+ fileJoinPath.getAbsolutePath() +"/"+ joinFileName);
                    outputStream.close();
                } else {
                    System.err.println("No Files exist in path -> "+ splitFiles.getAbsolutePath());
                }
            } else {
                System.err.println("This path doesn't exist -> "+ splitFiles.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runtime runtime = Runtime.getRuntime();
        try {
            String[] command = {"cmd.exe", "/k", "Start", path + "Videos_Joined/" + "01_" + video_name};
            Process p = runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
