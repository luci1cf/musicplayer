package ctrl;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    // Loads database properties from file or creates default ones
    private final static Properties prop = getProperties();

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

    /*
        Returns the single database connection instance
     */
    public static Connection getInstance() {
        return con;
    }

    /*
        Loads database properties from "db.properties".
        If the file doesn't exist, default properties will be created
        and written to a new file

        @return Properties object containing DB configuuration
     */

    private static Properties getProperties() {
        Properties p = new Properties();
        String fileName = "db.properties";

        try {
            p.load(new FileReader(fileName));

        } catch (IOException e) {
            p.setProperty("DB_Driver", "oracle.jdbc.OracleDriver");
            p.setProperty("DB_URL", "...");
            p.setProperty("DB_User", "...");
            p.setProperty("DB_Passwd", "...");

            try {
                p.store(new FileWriter(fileName), "lmaa");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return p;
    }
}