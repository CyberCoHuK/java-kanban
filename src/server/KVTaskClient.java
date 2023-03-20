package server;

import exception.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    public static final String URN_REGISTER = "/register";
    public static final String URN_SAVE = "/save";
    public static final String URN_LOAD = "/load";
    public static final String API_TOKEN = "?API_TOKEN=";
    String url;
    String apiToken;
    private HttpClient httpClient;

    public KVTaskClient(int port) {
        url = "http://localhost:" + port;
        httpClient = HttpClient.newHttpClient();
        register(httpClient, url);
    }

    private void register(HttpClient httpClient, String url) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url + URN_REGISTER))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "text/html")
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, handler);
            if (httpResponse.statusCode() != 200) {
                throw new ClientRegisterException("Ошибка регистрации");
            }
            apiToken = httpResponse.body();
        } catch (Exception exception) {
            throw new ClientRegisterException("Ошибка регистрации");
        }
    }

    public String load(String key) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url + URN_LOAD + "/" + key + API_TOKEN + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                throw new ClientRegisterException("Ошибка загрузки");
            }
            return httpResponse.body();

        } catch (Exception exception) {
            throw new ClientLoadException("Ошибка загрузки");
        }
    }

    public void put(String key, String json) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url + URN_SAVE + "/" + key + API_TOKEN + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                throw new ClientRegisterException("Ошибка сохранения");
            }

        } catch (Exception exception) {
            throw new ClientSaveException("Ошибка сохранения");
        }
    }

}