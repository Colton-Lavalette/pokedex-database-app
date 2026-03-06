package View.CRUDLogic;

import BLL.PokemonBLL;
import DAL.PokedexDAL;
import DAL.TrainerDAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for adding a new Pokémon to the database.
 */
public class NewPokemon {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField pokedexNumberField;
    private JTextField trainerIdField;
    private JTextField levelField;
    private JButton submitButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the NewPokemon form with a database connection.
     *
     * @param connection      the database connection for interacting with Pokémon data
     * @param refreshCallback the callback to refresh the Pokémon table upon successful submission
     */
    public NewPokemon(Connection connection, RefreshTableCallback refreshCallback) {
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
     * Sets up the main frame for the NewPokemon form.
     */
    private void setUpFrame() {
        frame = new JFrame("New Pokémon");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the New Pokémon form.
     */
    private void formatFields() {
        pokedexNumberField = new JTextField();
        pokedexNumberField.setFont(new Font("Courier New", Font.PLAIN, 12));
        pokedexNumberField.setPreferredSize(new Dimension(300, 40));

        trainerIdField = new JTextField();
        trainerIdField.setFont(new Font("Courier New", Font.PLAIN, 12));
        trainerIdField.setPreferredSize(new Dimension(300, 40));

        levelField = new JTextField();
        levelField.setFont(new Font("Courier New", Font.PLAIN, 12));
        levelField.setPreferredSize(new Dimension(300, 40));
    }

    /**
     * Adds the text fields for the New Pokémon form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Pokédex Number:"));
        inputPanel.add(pokedexNumberField);
        inputPanel.add(new JLabel("Trainer ID:"));
        inputPanel.add(trainerIdField);
        inputPanel.add(new JLabel("Level:"));
        inputPanel.add(levelField);
    }

    /**
     * Configures the input fields and their layout for the NewPokemon form.
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
     * Configures the submit and cancel buttons for the NewPokemon form.
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
        pokedexNumberField.addActionListener(e -> trainerIdField.requestFocus());
        trainerIdField.addActionListener(e -> levelField.requestFocus());
        levelField.addActionListener(e -> submit());

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
     * Sets up key bindings for the NewPokemon form, including the Escape key to close the form.
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
     * Handles the submission of the Pokémon form, validating input and updating the database.
     */
    private void submit() {
        try {
            int pokedexNumber = Integer.parseInt(pokedexNumberField.getText().trim());
            int trainerId = Integer.parseInt(trainerIdField.getText().trim());
            int level = Integer.parseInt(levelField.getText().trim());

            TrainerDAL trainerDAL = new TrainerDAL(connection);
            if (!trainerDAL.trainerExists(trainerId)) {
                JOptionPane.showMessageDialog(frame, "Trainer with ID " + trainerId + " does not exist.", "Trainer Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PokedexDAL pokedexDAL = new PokedexDAL(connection);
            if (!pokedexDAL.pokedexEntryExists(pokedexNumber)) {
                JOptionPane.showMessageDialog(frame, "Pokédex entry for number " + pokedexNumber + " does not exist.", "Pokédex Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PokemonBLL pokemonBLL = new PokemonBLL(connection);
            boolean success = pokemonBLL.addPokemon(pokedexNumber, trainerId, level);

            if (success) {
                JOptionPane.showMessageDialog(frame, "Pokémon added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshCallback.refreshTable();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add Pokémon. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Pokedex number, Trainer ID, and Level must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            String errorMessage = ex.getMessage();
            if (errorMessage.contains("Permission denied")) {
                JOptionPane.showMessageDialog(frame, errorMessage, "Permission Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Unexpected Error: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
