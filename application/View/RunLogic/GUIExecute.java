package View.RunLogic;

/**
 * The GUIExecute class is responsible for managing the application startup process,
 * including handling the login procedure and launching the main application GUI.
 * This class provides methods to initiate the login window and control the flow
 * of the application after a successful login.
 * </p>
 * The class cannot be instantiated due to its private constructor, as it is designed
 * to serve as a utility class for executing the GUI logic.
 */
public class GUIExecute {

    /**
     * Empty constructor for the GUIExecute class to prevent instantiation.
     */
    private GUIExecute() {

    }

    /**
     * Starts the login process by creating the LoginWindow.
     * Once login is successful, the main application GUI is displayed.
     */
    public static void run() {
        launchLoginWindow();
    }

    /**
     * Launches the LoginWindow and manages the flow after successful login.
     */
    private static void launchLoginWindow() {
        LoginWindow loginWindow = new LoginWindow();
    }

}
