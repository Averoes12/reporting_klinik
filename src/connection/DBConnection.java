package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/klinik?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Connection connect() {
        try {
            Connection conn = getConnection();
            System.out.println("db connected");
            return conn;
        } catch (SQLException ex) {
            System.out.println("db failed connect " + ex.getMessage());
            LOGGER.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Connection getConnection() throws SQLException {
        loadDriver();
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void loadDriver() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("library connected");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("MySQL Connector/J tidak ditemukan", ex);
        }
    }
}
