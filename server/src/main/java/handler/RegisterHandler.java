package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import service.RegisterService;
import service.requests.RegisterRequest;
import service.results.RegisterResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {

    private final DataAccess dataAccess;
    private final Gson gson = new Gson();

    public RegisterHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {

        try {
            RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
            RegisterService registerService = new RegisterService(dataAccess);
            RegisterResult result = registerService.register(registerRequest);

            if (result.getMessage() != null) {
                if (result.getMessage().contains("already taken")) {
                    response.status(403);
                } else if (result.getMessage().contains("bad request")) {
                    response.status(400);
                } else {
                    response.status(500);
                }
                return gson.toJson(new ErrorResponse("Error: " + result.getMessage()));
            }

            response.status(200);
            return gson.toJson(result);

        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new RegisterHandler.ErrorResponse("Error: " + e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}
