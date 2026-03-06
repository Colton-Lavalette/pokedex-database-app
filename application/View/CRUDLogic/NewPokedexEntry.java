package View.CRUDLogic;

import BLL.PokedexBLL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for adding a new Pokédex entry to the database.
 */
public class NewPokedexEntry {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField pokedexNumberField;
    private JTextField speciesNameField;
    private JTextField regionIdField;
    private JButton submitButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the NewPokedexEntry form with a database connection.
     *
     * @param connection      the database connection for interacting with Pokédex entry data
     * @param refreshCallback the callback to refresh the Pokédex table upon successful submission
     */
    public NewPokedexEntry(Connection connection, RefreshTableCallback refreshCallback) {
        this.connection = connection;
        this.refreshCallback = refreshCallback;

        setUpFrame();
        setUpInputFields();
        setUpButtons();
        setUpListeners();
        setUpKeyBindings();

        frame.setFocusable(true);
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> pokedexNumberField.requestFocusInWindow());
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
     * Sets up the main frame for the NewPokedexEntry form.
     */
    private void setUpFrame() {
        frame = new JFrame("New Pokédex Entry");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the New Pokédex Entry form.
     */
    private void formatFields() {
        pokedexNumberField = new JTextField();
        pokedexNumberField.setFont(new Font("Courier New", Font.PLAIN, 12));
        pokedexNumberField.setPreferredSize(new Dimension(300, 40));

        speciesNameField = new JTextField();
        speciesNameField.setFont(new Font("Courier New", Font.PLAIN, 12));
        speciesNameField.setPreferredSize(new Dimension(300, 40));

        regionIdField = new JTextField();
        regionIdField.setFont(new Font("Courier New", Font.PLAIN, 12));
        regionIdField.setPreferredSize(new Dimension(300, 40));
    }

    /**
     * Adds the text fields for the New Pokédex Entry form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Pokédex Number:"));
        inputPanel.add(pokedexNumberField);
        inputPanel.add(new JLabel("Species Name:"));
        inputPanel.add(speciesNameField);
        inputPanel.add(new JLabel("Region ID:"));
        inputPanel.add(regionIdField);
    }

    /**
     * Configures the input fields and their layout for the New Pokédex Entry form.
     */
    private void setUpInputFields() {
        inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formatFields();
        addFields();
        frame.add(inputPanel, BorderLayout.CENTER);
    }

    /**
     * Configures the submit and cancel buttons for the New Pokédex Entry form.
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
        pokedexNumberField.addActionListener(e -> speciesNameField.requestFocus());
        speciesNameField.addActionListener(e -> regionIdField.requestFocus());
        regionIdField.addActionListener(e -> submit());

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
     * Sets up key bindings for the New Pokédex Entry form, including the Escape key to close the form.
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
     * Handles the submission of the Pokédex Entry form, validating input and updating the database.
     */
    private void submit() {
        try {
            int pokedexNumber = Integer.parseInt(pokedexNumberField.getText().trim());
            String speciesName = capitalizeFirstLetter(speciesNameField.getText().trim());
            int regionId = Integer.parseInt(regionIdField.getText().trim());
            PokedexBLL pokedexBLL = new PokedexBLL(connection);
            boolean success = pokedexBLL.addPokedexEntry(pokedexNumber, speciesName, regionId);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Pokémon " + speciesName + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshCallback.refreshTable();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add " + speciesName + ". Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Pokédex number and Region ID must be valid numbers.", "Number Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            String errorMessage = ex.getMessage();
            if (errorMessage.contains("entry with this number already exists")) {
                JOptionPane.showMessageDialog(frame, errorMessage, "Duplicate Entry Error", JOptionPane.ERROR_MESSAGE);
            } else if (errorMessage.contains("Pokémon with this name already exists")) {
                JOptionPane.showMessageDialog(frame, errorMessage, "Duplicate Pokémon Error", JOptionPane.ERROR_MESSAGE);
            } else if (errorMessage.contains("Permission denied")) {
                JOptionPane.showMessageDialog(frame, errorMessage, "Permission Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
