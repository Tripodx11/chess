package client;

import com.google.gson.Gson;
import model.*;
import service.requests.RegisterRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
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
}
