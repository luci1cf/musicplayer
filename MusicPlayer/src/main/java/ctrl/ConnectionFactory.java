package ctrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    // Loads database properties from file or creates default ones
    private final static Properties prop = getProperty();

    // Read database configuration with fallback default values (Didn't write the lines fully out of privacy reasons)
    private final static String driver = prop.getProperty("DB_Driver", "oracle.jdbc.OracleDriver");
    private final static String url = prop.getProperty("DB_URL", "...");
    private final static String user = prop.getProperty("DB_User", "...");
    private final static String passwd = prop.getProperty("DB_Passwd", "...");

    // Singleton instance (ensures constructor is called only once)
    private static ConnectionFactory instance = new ConnectionFactory();

    // Single shared database connection
    private static Connection con;

    /*
        Private constructor which prevents external instantiation
        and loads the JDBC driver and establishes the DB connection afterward
     */
    private ConnectionFactory() {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, passwd);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}