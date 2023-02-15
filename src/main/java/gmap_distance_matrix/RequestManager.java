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
    }

    public String getResponse() {
        return this.json;
    }
}
