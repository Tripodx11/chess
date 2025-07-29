package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import service.CreateGameService;
import requests.CreateGameRequest;
import results.CreateGameResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {

    private final DataAccess dataAccess;
    private final Gson gson = new Gson();

    public CreateGameHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {

        try {
            String gameName = gson.fromJson(request.body(), CreateGameRequest.class).getGameName();
            String authToken = request.headers("authorization");
            CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
            CreateGameService createGameService = new CreateGameService(dataAccess);
            CreateGameResult result = createGameService.createGame(createGameRequest);

            if (result.getMessage() != null) {
                if (result.getMessage().contains("unauthorized")) {
                    response.status(401);
                } else if (result.getMessage().contains("bad request")) {
                    response.status(400);
                } else {
                    response.status(500);
                }
                return gson.toJson(new CreateGameHandler.ErrorResponse("Error: " + result.getMessage()));
            }

            response.status(200);
            return gson.toJson(result);

        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new CreateGameHandler.ErrorResponse("Error: " + e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}


