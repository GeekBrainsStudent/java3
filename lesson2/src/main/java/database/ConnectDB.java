package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    private Connection connect;

    public ConnectDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connect = DriverManager.getConnection("jdbc:sqlite:lesson2/src/main/resources/store.db");
    }

    public void close() throws SQLException {
        if(connect != null)
            connect.close();
    }

    public Connection getConnect() {
        return connect;
    }
}
