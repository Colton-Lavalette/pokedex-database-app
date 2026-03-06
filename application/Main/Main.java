package Main;

import View.RunLogic.GUIExecute;

import javax.swing.*;

/**
 * The Main class serves as the entry point for the application.
 * It is responsible for initiating the application by calling the
 * run method from the GUIExecute class, which starts the user interface
 * and handles the flow of the program.
 */
public class Main {

    /**
     * The main method is the entry point of the application.
     * It initializes the GUI by invoking the run() method from the
     * GUIExecute class. This method starts the program and displays
     * the login window to the user.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        GUIExecute.run();
    }
}
