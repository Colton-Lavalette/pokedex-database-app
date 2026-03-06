package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles the connection to the MySQL database for the application.
 * Provides methods to establish, retrieve, and close the connection.
 * This class is instantiated with user-specific credentials.
 */
public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/POKEMON";
    private final String USER;
    private final String PASSWORD;
    private Connection connection;

    /**
     * Constructs a DatabaseConnector instance with the provided user credentials.
     *
     * @param USER     the username for the database connection
     * @param PASSWORD the password for the database connection
     */
    public DatabaseConnector(String USER, String PASSWORD) {
        this.USER = USER;
        this.PASSWORD = PASSWORD;
    }

    /**
     * Establishes a connection to the MySQL database using URL, user, and password.
     * Sets the connection's auto-commit mode to true.
     */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true);
            System.out.println("\nConnecting to database…");
            System.out.println("Connection to database established successfully.\n");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }

    /**
     * Closes the database connection if it is open.
     * If the connection is already closed, no action is taken.
     * Prints a message when the connection is successfully closed.
     *
     * @return the current connection to the database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection to the database if it's open.
     * Prints a message when the connection is closed.
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("\nDisconnecting…");
                connection.close();
                System.out.println("Disconnected from database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
