package gmap_distance_matrix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileManager {
    /**
     * This method used to get the key in a private manner
     *
     * @return key
     */
    public String getKey() {
        String Key_FILE_PATH = "data/key.json";
        // Read json file
        File dataFile = new File(Key_FILE_PATH);
        String key = "";

        try {
            Scanner scanFile = new Scanner(dataFile);
            while (scanFile.hasNextLine()) {
                // System.out.println(scanFile.nextLine());
                // read from json file, convert json to hash map
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> myMap = gson.fromJson(scanFile.nextLine(), type);
                key = myMap.get("Key");
            }
            scanFile.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return key;
    }

    /**
     * This method used to store the data in a file
     *
     * @param response
     */
    public void storeTheData(String response) {
        try (FileWriter fileWriter = new FileWriter(Data_FILE_PATH)) {
            fileWriter.write(response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean CheckAndCreateFile() {
        File file = new File(Data_FILE_PATH);
        if (file.exists()) {
            System.out.println("File exists.");
            return true;
        } else {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + Data_FILE_PATH);
                    return false;
                } else {
                    System.out.println("File creation failed.");
                    return false;
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
                return false;
            }
        }
    }

    public void ReadJSONFile() {
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(Data_FILE_PATH))) {
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            Map<String, Object> map = gson.fromJson(reader, type);
            //check key validation
            try {
                //display the desired data
                List<Object> rows = (List<Object>) map.get("rows");
                Map<String, Object> rowsMap = (Map<String, Object>) rows.get(0);

                //check if the destination is valid
                try {
                    List<Object> elements = (List<Object>) rowsMap.get("elements");
                    Map<String, Object> elementsMap = (Map<String, Object>) elements.get(0);

                    Map<String, Object> dataMap = (Map<String, Object>) elementsMap.get("duration");
                    String value = (String) dataMap.get("text");

                    System.out.println("The estimated travel time: " + value);
                } catch (NullPointerException e) {
                    System.out.println();
                    System.out.println("Not Found, please try again");
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println();
                System.out.println("Not Found, please check your key");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static final String Data_FILE_PATH = "input/input.json";
}
