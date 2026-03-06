package View.CRUDLogic;

import BLL.PokemonBLL;
import DAL.PokedexDAL;
import DAL.PokemonDAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for updating a Pokémon's level.
 */
public class UpdateLevel {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField pokemonIdField;
    private JTextField levelField;
    private JButton submitButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the UpdateLevel form with a database connection.
     *
     * @param connection      the database connection for interacting with Pokémon data
     * @param refreshCallback the callback to refresh the Pokémon table upon successful submission
     */
    public UpdateLevel(Connection connection, RefreshTableCallback refreshCallback) {
        this.connection = connection;
        this.refreshCallback = refreshCallback;

        setUpFrame();
        setUpInputFields();
        setUpButtons();
        setUpListeners();
        setUpKeyBindings();

        frame.setFocusable(true);
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> pokemonIdField.requestFocusInWindow());
        frame.setVisible(true);
    }

    /**
     * Sets up the main frame for the NewPokemon form.
     */
    private void setUpFrame() {
        frame = new JFrame("Update Level");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the UpdateLevel form.
     */
    private void formatFields() {
        pokemonIdField = new JTextField();
        pokemonIdField.setFont(new Font("Courier New", Font.PLAIN, 12));
        pokemonIdField.setPreferredSize(new Dimension(300, 40));

        levelField = new JTextField();
        levelField.setFont(new Font("Courier New", Font.PLAIN, 12));
        levelField.setPreferredSize(new Dimension(300, 40));
    }

    /**
     * Adds the text fields for the Update Level form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Pokémon ID:"));
        inputPanel.add(pokemonIdField);
        inputPanel.add(new JLabel("Pokémon Level:"));
        inputPanel.add(levelField);
    }

    /**
     * Configures the input fields and their layout for the UpdateLevel form.
     */
    private void setUpInputFields() {
        inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
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
        pokemonIdField.addActionListener(e -> levelField.requestFocus());
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
     * Sets up key bindings for the UpdateLevel form, including the Escape key to close the form.
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
     * Handles the submission of the Update Level form, validating input and updating the database.
     */
    private void submit() {
        try {
            int pokemonId = Integer.parseInt(pokemonIdField.getText().trim());
            int level = Integer.parseInt(levelField.getText().trim());
            PokemonDAL pokemonDAL = new PokemonDAL(connection);
            PokedexDAL pokedexDAL = new PokedexDAL(connection);
            int pokedexNumber = pokemonDAL.getPokedexNumberById(pokemonId);
            String speciesName = pokedexDAL.getSpeciesNameByPokedexNumber(pokedexNumber);

            if (!pokemonDAL.pokemonExists(pokemonId)) {
                JOptionPane.showMessageDialog(frame, "Pokémon with ID " + pokemonId + " does not exist.", "Pokémon Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PokemonBLL pokemonBLL = new PokemonBLL(connection);
            boolean success = pokemonBLL.updateLevel(pokemonId, level);

            if (success) {
                JOptionPane.showMessageDialog(frame, speciesName + "'s level updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshCallback.refreshTable();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update" + speciesName + "'s level. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Pokémon ID and Level must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
