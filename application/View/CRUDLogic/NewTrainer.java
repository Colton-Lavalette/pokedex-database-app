package View.CRUDLogic;

import BLL.TrainerBLL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for adding a new trainer to the database.
 */
public class NewTrainer {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField trainerNameField;
    private JTextField trainerAgeField;
    private JTextField hometownField;
    private JTextField trainerClassField;
    private JButton submitButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the NewTrainer form with a database connection.
     *
     * @param connection      the database connection for interacting with trainer data
     * @param refreshCallback the callback to refresh the trainer table upon successful submission
     */
    public NewTrainer(Connection connection, RefreshTableCallback refreshCallback) {
        this.connection = connection;
        this.refreshCallback = refreshCallback;

        setUpFrame();
        setUpInputFields();
        setUpButtons();
        setUpListeners();
        setUpKeyBindings();

        frame.setFocusable(true);
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> trainerNameField.requestFocusInWindow());
        frame.setVisible(true);
    }

    /**
     * Capitalizes the first letter of each word in a string.
     *
     * @param input the string to format
     * @return the formatted string with each word capitalized
     */
    private static String capitalizeFirstLetter(String input) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
        }
        return result.toString().trim();
    }

    /**
     * Sets up the main frame for the NewTrainer form.
     */
    private void setUpFrame() {
        frame = new JFrame("New Trainer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the New Trainer form.
     */
    private void formatFields() {
        trainerNameField = new JTextField();
        trainerNameField.setFont(new Font("Courier New", Font.PLAIN, 12));
        trainerNameField.setPreferredSize(new Dimension(300, 40));

        trainerAgeField = new JTextField();
        trainerAgeField.setFont(new Font("Courier New", Font.PLAIN, 12));
        trainerAgeField.setPreferredSize(new Dimension(300, 40));

        hometownField = new JTextField();
        hometownField.setFont(new Font("Courier New", Font.PLAIN, 12));
        hometownField.setPreferredSize(new Dimension(300, 40));

        trainerClassField = new JTextField();
        trainerClassField.setFont(new Font("Courier New", Font.PLAIN, 12));
        trainerClassField.setPreferredSize(new Dimension(300, 40));
    }

    /**
     * Adds the text fields for the New Trainer form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Trainer Name:"));
        inputPanel.add(trainerNameField);
        inputPanel.add(new JLabel("Trainer Age:"));
        inputPanel.add(trainerAgeField);
        inputPanel.add(new JLabel("Trainer Hometown:"));
        inputPanel.add(hometownField);
        inputPanel.add(new JLabel("Trainer Class:"));
        inputPanel.add(trainerClassField);
    }
    
    /**
     * Configures the input fields and their layout for the NewTrainer form.
     */
    private void setUpInputFields() {
        inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formatFields();
        addFields();

        frame.add(inputPanel, BorderLayout.CENTER);
    }

    /**
     * Configures the submit and cancel buttons for the NewTrainer form.
     */
    private void setUpButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.decode("#cc0010"));

        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets up action listeners for user interactions with the form components.
     */
    private void setUpListeners() {
        trainerNameField.addActionListener(e -> trainerAgeField.requestFocus());
        trainerAgeField.addActionListener(e -> hometownField.requestFocus());
        hometownField.addActionListener(e -> trainerClassField.requestFocus());
        trainerClassField.addActionListener(e -> submit());

        submitButton.addActionListener(e -> submit());
        cancelButton.addActionListener(e -> frame.dispose());

        submitButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submit();
                }
            }
        });

        cancelButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.dispose();
                }
            }
        });
    }

    /**
     * Sets up key bindings for the NewTrainer form, including the Escape key to close the form.
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
                frame.dispose();
            }
        });
    }

    /**
     * Handles the submission of the trainer form, validating input and updating the database.
     */
    private void submit() {
        try {
            String trainerName = capitalizeFirstLetter(trainerNameField.getText().trim());
            int trainerAge = Integer.parseInt(trainerAgeField.getText().trim());
            String hometown = capitalizeFirstLetter(hometownField.getText().trim());
            String trainerClass = capitalizeFirstLetter(trainerClassField.getText().trim());

            TrainerBLL trainerBLL = new TrainerBLL(connection);
            boolean success = trainerBLL.addTrainer(trainerName, trainerAge, hometown, trainerClass);

            if (success) {
                JOptionPane.showMessageDialog(frame, trainerClass + " " + trainerName + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshCallback.refreshTable();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add trainer. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Age must be a valid number.", "Age Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            String errorMessage = ex.getMessage();
            if (errorMessage.contains("trainer with the same name and class")) {
                JOptionPane.showMessageDialog(frame, errorMessage, "Duplicate Entry Error", JOptionPane.ERROR_MESSAGE);
            } else if (errorMessage.contains("Permission denied")) {
                JOptionPane.showMessageDialog(frame, errorMessage, "Permission Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Unexpected Error: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
