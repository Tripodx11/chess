// server/src/main/java/dataaccess/DatabaseManager.java
package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {
    public static Connection getConnection() throws Exception {
        var props = new java.util.Properties();
        try (var fis = new java.io.FileInputStream("server/db/db.properties")) {
            props.load(fis);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);
    }
}
