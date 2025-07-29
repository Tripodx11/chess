package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import service.LogoutService;
import requests.LogoutAndListGamesRequest;
import results.LogoutResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {

    private final DataAccess dataAccess;
    private final Gson gson = new Gson();

    public LogoutHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {

        try {
            String authToken = request.headers("authorization");
            LogoutAndListGamesRequest logoutAndListGamesRequest = new LogoutAndListGamesRequest(authToken);
            LogoutService logoutService = new LogoutService(dataAccess);
            LogoutResult result = logoutService.logout(logoutAndListGamesRequest);

            if (result.getMessage() != null) {
                if (result.getMessage().contains("unauthorized")) {
                    response.status(401);
                } else if (result.getMessage().contains("bad request")) {
                    response.status(400);
                } else {
                    response.status(500);
                }
                return gson.toJson(new LogoutHandler.ErrorResponse("Error: " + result.getMessage()));
            }

            response.status(200);
            return gson.toJson(result);

        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new LogoutHandler.ErrorResponse("Error: " + e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}
