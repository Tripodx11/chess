package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import service.JoinGameService;
import service.requests.JoinGameRequest;
import service.results.JoinGameResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {

    private final DataAccess dataAccess;
    private final Gson gson = new Gson();

    public JoinGameHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {

        try {
            String color = gson.fromJson(request.body(), JoinGameRequest.class).getColor();
            int id = gson.fromJson(request.body(), JoinGameRequest.class).getGameID();
            String authToken = request.headers("authorization");

            JoinGameRequest createGameRequest = new JoinGameRequest(authToken, color, id);
            JoinGameService createGameService = new JoinGameService(dataAccess);
            JoinGameResult result = createGameService.joinGame(createGameRequest);

            if (result.getMessage() != null) {
                if (result.getMessage().contains("unauthorized")) {
                    response.status(401);
                } else if (result.getMessage().contains("bad request")) {
                    response.status(400);
                } else if (result.getMessage().contains("already taken")) {
                    response.status(403);
                } else {
                    response.status(500);
                }
                return gson.toJson(new JoinGameHandler.ErrorResponse("Error: " + result.getMessage()));
            }

            response.status(200);
            return gson.toJson(result);

        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new JoinGameHandler.ErrorResponse("Error: " + e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}
