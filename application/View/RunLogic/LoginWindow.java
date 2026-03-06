package View.RunLogic;

import DAL.DatabaseConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A GUI for user login to access the Pokémon Database.
 * <p>
 * Provides fields for username and password input, buttons for login and exit,
 * and optional password visibility toggling. Handles authentication and connects
 * to the database upon successful login.
 */
public class LoginWindow {
    private JFrame frame;
    private String username;
    private String password;
    private JPanel inputPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton showPasswordButton;
    private JButton loginButton;
    private JButton closeButton;

    /**
     * Constructs a LoginWindow instance and initializes the GUI components.
     */
    public LoginWindow() {
        setUpFrame();
        setUpInputFields();
        setUpButtons();
        setUpListeners();
        setUpKeyBindings();

        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());

        frame.setVisible(true);
    }

    /**
     * Configures the main frame for the login window.
     */
    private void setUpFrame() {
        frame = new JFrame("Pokémon Database Login");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(425, 250);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setFocusableWindowState(true);
    }

    /**
     * Sets up the input fields for username and password.
     */
    private void setUpInputFields() {
        inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(Color.decode("#FFFFFF"));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField();
        usernameField.setFont(new Font("Courier New", Font.PLAIN, 12));
        usernameField.setPreferredSize(new Dimension(300, 40));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Courier New", Font.PLAIN, 12));
        passwordField.setPreferredSize(new Dimension(300, 40));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.BLACK);
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLACK);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        frame.add(inputPanel, BorderLayout.CENTER);
    }

    /**
     * Configures buttons for login, exit, and password visibility toggle.
     */
    private void setUpButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.decode("#cc0010"));

        loginButton = new JButton("Log In");
        loginButton.setFocusable(true);

        closeButton = new JButton("Close");
        closeButton.setFocusable(true);

        showPasswordButton = new JButton("Show");
        showPasswordButton.setFocusable(true);
        inputPanel.add(showPasswordButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(closeButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets up listeners for button actions and keyboard events.
     */
    private void setUpListeners() {
        loginButton.addActionListener(e -> login(usernameField, passwordField));
        closeButton.addActionListener(e -> handleExit());

        usernameField.addActionListener(e -> passwordField.requestFocus());

        passwordField.addActionListener(e -> {
            username = usernameField.getText().trim();
            password = new String(passwordField.getPassword()).trim();
            frame.dispose();
            connectToDatabase();
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });

        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    passwordField.requestFocusInWindow(); // Ensure focus moves to password field
                }
            }
        });

        closeButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleExit();
                }
            }
        });

        // Log in with ENTER key on the login button
        loginButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login(usernameField, passwordField);
                }
            }
        });

        showPasswordButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login(usernameField, passwordField);
                }
            }
        });

        // Password visibility toggle
        showPasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                passwordField.setEchoChar((char) 0);
                showPasswordButton.setText("Hide");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                passwordField.setEchoChar('•');
                showPasswordButton.setText("Show");
            }
        });

        showPasswordButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    passwordField.setEchoChar((char) 0);
                    showPasswordButton.setText("Hide");
                }
            }

            public void keyReleased(KeyEvent e) {
                passwordField.setEchoChar('•');
                showPasswordButton.setText("Show");
            }
        });
    }

    /**
     * Configures key bindings for the Log-in Window.
     * Binds the Escape key to an action that closes the window, ensuring
     * that it works regardless of which component is focused.
     */
    private void setUpKeyBindings() {
        JRootPane rootPane = frame.getRootPane();

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "escapePressed"
        );

        rootPane.getActionMap().put("escapePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleExit();
            }
        });
    }

    /**
     * Handles login by validating credentials and connecting to the database.
     *
     * @param usernameField the input field for the username
     * @param passwordField the input field for the password
     */
    private void login(JTextField usernameField, JPasswordField passwordField) {
        username = usernameField.getText().trim();
        password = new String(passwordField.getPassword()).trim();
        frame.dispose();
        connectToDatabase();
    }

    /**
     * Confirms and handles the application's exit process.
     */
    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to close the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("\nClosing application…");
            System.out.println("Application closed.");
            System.exit(0);
        }
    }

    /**
     * Attempts to connect to the database with the provided credentials.
     */
    private void connectToDatabase() {
        DatabaseConnector dbConnector = new DatabaseConnector(username, password);
        dbConnector.connect();

        if (dbConnector.getConnection() != null) {
            System.out.println("Successfully connected to the database.");
            GUIBuilder gb = new GUIBuilder(1250, 650, dbConnector);
            gb.buildGUI();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to connect to the database. Please check your credentials and try again.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            GUIExecute.run();
        }
    }
}
