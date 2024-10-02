package com.uv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRequestHandler {

    private HttpClient client;

    public HttpRequestHandler() {
        client = HttpClient.newHttpClient();
    }

    public void executeRequest(String url, String method, boolean isRaw, Callback callback) {
        try {
            HttpRequest request;

            switch (method) {
                case "POST":
                    request = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
                    break;
                case "PUT":
                    request = HttpRequest.newBuilder().uri(URI.create(url)).PUT(HttpRequest.BodyPublishers.noBody()).build();
                    break;
                case "DELETE":
                    request = HttpRequest.newBuilder().uri(URI.create(url)).DELETE().build();
                    break;
                default: // GET por defecto
                    request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                    break;
            }

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    String status = String.valueOf(response.statusCode());
                    String mimeType = response.headers().firstValue("Content-Type").orElse("Desconocido");
                    String headers = response.headers().toString();
                    String body = response.body();

                    callback.onResponse(status, mimeType, headers, body);
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Callback {
        void onResponse(String status, String mimeType, String headers, String body);
    }
}
