package dataaccess;

public class MySQLDataAccess implements DataAccess{

    public MySQLDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }
}
