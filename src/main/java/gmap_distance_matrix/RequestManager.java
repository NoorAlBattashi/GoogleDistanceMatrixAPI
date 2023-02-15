package gmap_distance_matrix;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestManager {
    FileManager Key = new FileManager();
    String json;

    public RequestManager() {

    }

    public void userRequest(String pickup, String dropOff) throws IOException {
        //create URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/distancematrix/json").newBuilder();
        urlBuilder.addQueryParameter("origins", pickup);
        urlBuilder.addQueryParameter("destinations", dropOff);
        urlBuilder.addQueryParameter("units", "imperial");
        urlBuilder.addQueryParameter("key", Key.getKey());

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        Response response = client.newCall(request).execute();

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        this.json = response.body().string();

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

    public String getResponse() {
        return this.json;
    }
}
