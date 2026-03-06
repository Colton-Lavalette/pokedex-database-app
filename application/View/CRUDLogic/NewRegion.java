package View.CRUDLogic;

import BLL.PokedexBLL;
import BLL.RegionBLL;
import BLL.TrainerBLL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for adding a new region to the database.
 */
public class NewRegion {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField regionNameField;
    private JButton submitButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the NewRegion form with a database connection.
     *
     * @param connection      the database connection for interacting with region data
     * @param refreshCallback the callback to refresh the region table upon successful submission
     */
    public NewRegion(Connection connection, RefreshTableCallback refreshCallback) {
        this.connection = connection;
        this.refreshCallback = refreshCallback;

        setUpFrame();
        setUpInputFields();
        setUpButtons();
        setUpListeners();
        setUpKeyBindings();

        frame.setFocusable(true);
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> regionNameField.requestFocusInWindow());
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
     * Sets up the main frame for the NewRegion form.
     */
    private void setUpFrame() {
        frame = new JFrame("New Region");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 115);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the New Region form.
     */
    private void formatFields() {
        regionNameField = new JTextField();
        regionNameField.setFont(new Font("Courier New", Font.PLAIN, 12));
        regionNameField.setPreferredSize(new Dimension(300, 20));
    }

    /**
     * Adds the text fields for the New Region form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Region Name:"));
        inputPanel.add(regionNameField);
    }

    /**
     * Configures the input fields and their layout for the NewRegion form.
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
     * Configures the submit and cancel buttons for the NewRegion form.
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
        regionNameField.addActionListener(e -> submit());

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
     * Sets up key bindings for the NewRegion form, including the Escape key to close the form.
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
     * Handles the submission of the region form, validating input and updating the database.
     */
    private void submit() {
        try {
            String regionName = capitalizeFirstLetter(regionNameField.getText().trim());
            RegionBLL regionBLL = new RegionBLL(connection);
            boolean success = regionBLL.addRegion(regionName);

            if (success) {
                JOptionPane.showMessageDialog(frame, regionName + " region added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshCallback.refreshTable();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add region. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
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
