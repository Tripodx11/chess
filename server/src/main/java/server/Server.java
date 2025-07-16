package server;

import dataaccess.DataAccess;
import dataaccess.SystemDataAccess;
import handler.ClearHandler;
import handler.LoginHandler;
import handler.RegisterHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        DataAccess dataAccess = new SystemDataAccess();

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler(dataAccess));
        Spark.post("/user", new RegisterHandler(dataAccess));
        Spark.post("/session", new LoginHandler(dataAccess));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
