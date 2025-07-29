package client;

import com.google.gson.Gson;
import model.AuthData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.CreateGameResult;
import results.ListGamesResult;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

public class ServerFacade {

    private final Gson gson = new Gson();
    private final String serverUrl;

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public void clear() throws IOException {
        URL url = new URL(serverUrl + "/db");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("DELETE");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to clear database: " + connection.getResponseCode());
        }
    }

    public AuthData register(String un, String pass, String email) throws IOException {
        URL url = new URL(serverUrl + "/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        RegisterRequest request = new RegisterRequest(un, pass, email);

        try (var out = new OutputStreamWriter(connection.getOutputStream())) {
            gson.toJson(request, out);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (var in = new InputStreamReader(connection.getInputStream())) {
                return gson.fromJson(in, AuthData.class);
            }
        } else {
            throw new IOException("Failed to register: " + connection.getResponseCode());
        }
    }

    public AuthData login(String un, String pass) throws IOException {
        URL url = new URL(serverUrl + "/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        LoginRequest request = new LoginRequest(un, pass);

        try (var out = new OutputStreamWriter(connection.getOutputStream())) {
            gson.toJson(request, out);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (var in = new InputStreamReader(connection.getInputStream())) {
                return gson.fromJson(in, AuthData.class);
            }
        } else {
            throw new IOException("Failed to login: " + connection.getResponseCode());
        }
    }

    public CreateGameResult create(String auth, String name) throws IOException {
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", auth);

        CreateGameRequest request = new CreateGameRequest(name);

        try (var out = new OutputStreamWriter(connection.getOutputStream())) {
            gson.toJson(request, out);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (var in = new InputStreamReader(connection.getInputStream())) {
                return gson.fromJson(in, CreateGameResult.class);
            }
        } else {
            throw new IOException("Failed to create game: " + connection.getResponseCode());
        }
    }

    public ListGamesResult list(String auth) throws IOException {
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", auth);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (var in = new InputStreamReader(connection.getInputStream())) {
                return gson.fromJson(in, ListGamesResult.class);
            }
        } else {
            throw new IOException("Failed to list games: " + connection.getResponseCode());
        }
    }

    public void join(String auth, String color, int id) throws IOException {
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", auth);

        JoinGameRequest request = new JoinGameRequest(auth, color, id);

        try (var out = new OutputStreamWriter(connection.getOutputStream())) {
            gson.toJson(request, out);
        }

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to join game: " + connection.getResponseCode());
        }
    }

}
