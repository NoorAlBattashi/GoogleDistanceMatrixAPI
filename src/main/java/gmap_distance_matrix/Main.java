package gmap_distance_matrix;

import java.io.IOException;

public class Main {
    public static FileManager fileManager = new FileManager();
    public static RequestManager requestManager = new RequestManager();

    public static void main(String[] args) throws IOException {
        //get data from user
        String pickup = args[0];
        String dropOff = args[1];

        //check file existence
        boolean fileExist = fileManager.CheckAndCreateFile();
        if (fileExist) {
            fileManager.ReadJSONFile();
        } else if (!fileExist) {
            requestManager.userRequest(pickup, dropOff);
            fileManager.storeTheData(requestManager.getResponse());
        }


    }
}