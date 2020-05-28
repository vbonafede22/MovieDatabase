package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConnection {
    public static Connection Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            return connection;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
