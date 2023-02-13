package gmap_distance_matrix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class Main {
    public static final String Data_FILE_PATH = "input/input.json";

    public static void main(String[] args) throws IOException {
        //get data from user
        System.out.println("welcome!");
        System.out.print("Please enter your pickup point: ");
        String pickup = stringScanner();
        System.out.print("Please enter your drop off point: ");
        String dropOff = stringScanner();

        //create URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/distancematrix/json").newBuilder();
        urlBuilder.addQueryParameter("origins", pickup);
        urlBuilder.addQueryParameter("destinations", dropOff);
        urlBuilder.addQueryParameter("units", "imperial");
        urlBuilder.addQueryParameter("key", getKey());

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        String json = response.body().string();
        //store the response in file
        storeTheData(json);

        //check key validation
        try {
            //display the desired data
            HashMap<String, Object> map = gson.fromJson(json, type);

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
    }

    /**
     * This method used to get String input from the user
     *
     * @return outputValue
     * @return stringScanner()
     */
    public static String stringScanner() {
        try {
            Scanner inputScanner = new Scanner(System.in);
            String outputValue = inputScanner.nextLine();
            return outputValue;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. It must be a string.");
            System.out.print("Enter again here: ");
            return stringScanner();
        }
    }

    /**
     * This method used to get the key in a private manner
     *
     * @return key
     */
    public static String getKey() {
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
    public static void storeTheData(String response) {

        try (FileWriter fileWriter = new FileWriter(Data_FILE_PATH)) {
            fileWriter.write(response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}