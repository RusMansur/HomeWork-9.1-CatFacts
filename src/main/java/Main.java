import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    final public static String REMOTE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet(REMOTE_URL);
        CloseableHttpResponse response = httpClient.execute(request);
        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        List<CatFacts> catFacts = jsonToList(body);
        catFacts.stream()
                .filter(value -> value.getUpvotes() > 0)
                .forEach(System.out::println);
    }

    private static List<CatFacts> jsonToList(String json) {
        List<CatFacts> catFactsList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Object object = jsonParser.parse(json);
            JSONArray jsonArray = (JSONArray) object;
            for (Object node : jsonArray) {
                CatFacts catFacts = gson.fromJson(node.toString(), CatFacts.class);
                catFactsList.add(catFacts);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return catFactsList;
    }


}
