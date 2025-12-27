package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Produk;
import model.Pesanan;

public class PanesyaApiClient {

    private static final String BASE_URL =
            "http://localhost/application-treetier-php/public/";

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    /* ======================= PRODUK ======================= */

    public List<Produk> findAllProduk() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "produk"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        ApiResponse<List<Produk>> apiResp =
                gson.fromJson(response.body(),
                        new TypeToken<ApiResponse<List<Produk>>>() {}.getType());

        if (!apiResp.success)
            throw new Exception(apiResp.message);

        return apiResp.data;
    }

    /* ======================= PESANAN ======================= */

    public List<Pesanan> findAllPesanan() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "pesanan"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        ApiResponse<List<Pesanan>> apiResp =
                gson.fromJson(response.body(),
                        new TypeToken<ApiResponse<List<Pesanan>>>() {}.getType());

        if (!apiResp.success)
            throw new Exception(apiResp.message);

        return apiResp.data;
    }

    public void createPesanan(Pesanan p) throws Exception {
        String json = gson.toJson(p);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "pesanan"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        ApiResponse<?> apiResp =
                gson.fromJson(response.body(), ApiResponse.class);

        if (!apiResp.success)
            throw new Exception(apiResp.message);
    }

    /* ======================= RESPONSE ======================= */

    private static class ApiResponse<T> {
        boolean success;
        T data;
        String message;
    }
}
