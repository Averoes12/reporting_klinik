package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
//    "jdbc:mysql://bore.pub:23306/klinik?user=root&password=root123"
//    "jdbc:mysql://localhost:3306/klinik?user=root&password="

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final String HOST = config("db.host", "DB_HOST", "localhost");
    private static final String PORT = config("db.port", "DB_PORT", "3306");
    private static final String DATABASE = config("db.name", "DB_NAME", "klinik");
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = config("db.user", "DB_USER", "root");
    private static final String PASSWORD = config("db.password", "DB_PASSWORD", "");

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

    private static String config(String propertyName, String envName, String defaultValue) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.trim().isEmpty()) {
            return propertyValue.trim();
        }

        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        return defaultValue;
    }
}
