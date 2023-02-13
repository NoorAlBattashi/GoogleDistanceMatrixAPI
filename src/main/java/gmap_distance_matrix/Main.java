package gmap_distance_matrix;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
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

        //convert url to json string
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(urlBuilder);

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        try (Response response = client.newCall(request).execute();){
            storeTheData(json);
           // System.out.println(response.body().string());

        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public static void storeTheData(String response) {
        String Data_FILE_PATH = "input/input.json";
        try (FileWriter fileWriter = new FileWriter(Data_FILE_PATH)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(response, fileWriter);
            fileWriter.write("\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}