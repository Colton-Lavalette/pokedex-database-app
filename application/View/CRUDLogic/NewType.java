package View.CRUDLogic;

import BLL.TypeChartBLL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for adding a new type to the database.
 */
public class NewType {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField typeNameField;
    private JButton submitButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the NewType form with a database connection.
     *
     * @param connection      the database connection for interacting with type data
     * @param refreshCallback the callback to refresh the type table upon successful submission
     */
    public NewType(Connection connection, RefreshTableCallback refreshCallback) {
        this.connection = connection;
        this.refreshCallback = refreshCallback;

        setUpFrame();
        setUpInputFields();
        setUpButtons();
        setUpListeners();
        setUpKeyBindings();

        frame.setFocusable(true);
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> typeNameField.requestFocusInWindow());
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
     * Sets up the main frame for the NewType form.
     */
    private void setUpFrame() {
        frame = new JFrame("New Type");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 115);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the New type form.
     */
    private void formatFields() {
        typeNameField = new JTextField();
        typeNameField.setFont(new Font("Courier New", Font.PLAIN, 12));
        typeNameField.setPreferredSize(new Dimension(300, 20));
    }

    /**
     * Adds the text fields for the New type form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Type Name:"));
        inputPanel.add(typeNameField);
    }

    /**
     * Configures the input fields and their layout for the NewType form.
     */
    private void setUpInputFields() {
        inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formatFields();
        addFields();
        frame.add(inputPanel, BorderLayout.CENTER);
    }

    /**
     * Configures the submit and cancel buttons for the NewType form.
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
        typeNameField.addActionListener(e -> submit());

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
     * Sets up key bindings for the NewType form, including the Escape key to close the form.
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
     * Handles the submission of the type form, validating input and updating the database.
     */
    private void submit() {
        try {
            String typeName = capitalizeFirstLetter(typeNameField.getText().trim());
            TypeChartBLL typeChartBLL = new TypeChartBLL(connection);
            boolean success = typeChartBLL.addType(typeName);

            if (success) {
                JOptionPane.showMessageDialog(frame, typeName + " type added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshCallback.refreshTable();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add " + typeName + "type. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Permission Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
