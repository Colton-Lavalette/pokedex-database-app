package View.RunLogic;

import DAL.DatabaseConnector;

import javax.swing.*;
import java.sql.Connection;

/**
 * GUIBuilder is responsible for setting up the main frame of the application
 * and initializing the graphical user interface (GUI) components.
 *
 * It creates the main JFrame window with the specified size and connects the
 * necessary database components. The method buildGUI() is then used to
 * initialize and display the GUI by setting up panels, buttons, and event listeners.
 */
public class GUIBuilder {
    private final JFrame frame;
    private final DatabaseConnector dbConnector;
    private final Connection connection;

    /**
     * Constructs a GUIBuilder object with the specified window dimensions,
     * database connector, and database connection.
     *
     * @param width the width of the main application window
     * @param height the height of the main application window
     * @param dbConnector the database connector used for connecting to the database
     */
    public GUIBuilder(int width, int height, DatabaseConnector dbConnector) {
        this.dbConnector = dbConnector;
        this.connection = dbConnector.getConnection();
        frame = new JFrame("Pokémon Database");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Sets up and displays the main GUI for the application. It initializes the
     * necessary components, including panels, buttons, and their event handlers.
     */
    public void buildGUI() {
        GUISetup guiSetup = new GUISetup(frame, dbConnector, connection);
        guiSetup.setUpGUI(); // Set up the GUI using GUISetup class
    }

    /**
     * Returns the main JFrame of the application.
     *
     * @return the JFrame object representing the main window
     */
    public JFrame getFrame() {
        return frame;
    }
}
