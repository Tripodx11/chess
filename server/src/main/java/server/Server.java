package server;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySQLDataAccess;
import dataaccess.SystemDataAccess;
import handler.*;
import spark.*;
import websocket.ConnectionManager;
import websocket.WebSocketDispatcher;

public class Server {

    public int run(int desiredPort){
        Spark.port(desiredPort);
        Spark.webSocket("/ws", websocket.WebSocketHandler.class);

        Spark.staticFiles.location("web");
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        DataAccess dataAccess;
        try {
            // Swap this line to use either System or MySQL DAO
            dataAccess = new MySQLDataAccess();
            ConnectionManager connections = new ConnectionManager();
            WebSocketDispatcher.initialize(dataAccess, connections);
// persistent
            // dataAccess = new SystemDataAccess(); // in-memory
        } catch (DataAccessException e) {
            System.err.println("Failed to initialize data access: " + e.getMessage());
            Spark.stop();
            return -1;
        }


        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler(dataAccess));
        Spark.post("/user", new RegisterHandler(dataAccess));
        Spark.post("/session", new LoginHandler(dataAccess));
        Spark.delete("/session", new LogoutHandler(dataAccess));
        Spark.post("/game", new CreateGameHandler(dataAccess));
        Spark.put("/game", new JoinGameHandler(dataAccess));
        Spark.get("/game", new ListGamesHandler(dataAccess));


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
