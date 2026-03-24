package ctrl;

import java.sql.Connection;
import java.util.Properties;

public class ConnectionFactory {
    // Loads database properties from file or creates default ones
    private final static Properties prop = getProperty();

    // Read database configuration with fallback default values (Commented these lines and didn't write it fully out of privacy reasons)
    private final static String driver = prop.getProperty("DB_Driver", "oracle.jdbc.OracleDriver");
    /*
    private final static String url = prop.getProperty("DB_URL", "...");
    private final static String user = prop.getProperty("DB_User", "...");
    private final static String passwd = prop.getProperty("DB_Passwd", "...");
    */

    // Singleton instance (ensures constructor is called only once)
    private static ConnectionFactory instance = new ConnectionFactory();

    // Single shared database connection
    private static Connection con;
}