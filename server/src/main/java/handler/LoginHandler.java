package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import service.LoginService;
import service.requests.LoginRequest;
import service.results.RegisterAndLoginResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {

    private final DataAccess dataAccess;
    private final Gson gson = new Gson();

    public LoginHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {

        try {
            LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
            LoginService loginService = new LoginService(dataAccess);
            RegisterAndLoginResult result = loginService.login(loginRequest);

            if (result.getMessage() != null) {
                if (result.getMessage().contains("unauthorized")) {
                    response.status(401);
                } else if (result.getMessage().contains("bad request")) {
                    response.status(400);
                } else {
                    response.status(500);
                }
                return gson.toJson(new LoginHandler.ErrorResponse("Error: " + result.getMessage()));
            }

            response.status(200);
            return gson.toJson(result);

        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new LoginHandler.ErrorResponse("Error: " + e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}
