package handler;


import com.google.gson.Gson;
import dataaccess.DataAccess;
import service.ClearService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {

    private final DataAccess dataAccess;
    private final Gson gson = new Gson();

    public ClearHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            ClearService clearService = new ClearService(dataAccess);
            clearService.clear();
            response.status(200);
            return gson.toJson(new Object());
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}
