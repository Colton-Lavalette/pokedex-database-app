package View.CRUDLogic;

import BLL.PokedexBLL;
import BLL.TrainerBLL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for deleting a trainer from the database.
 */
public class DeletePokedexEntry {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField pokedexNumberField;
    private JButton deleteButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the Delete Pokédex Entry form with a database connection.
     *
     * @param connection      the database connection for interacting with Pokédex data
     * @param refreshCallback the callback to refresh the Pokédex table upon successful deletion
     */
    public DeletePokedexEntry(Connection connection, RefreshTableCallback refreshCallback) {
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
     * Sets up the main frame for the DeletePokedexEntry form.
     */
    private void setUpFrame() {
        frame = new JFrame("Delete Pokédex Entry");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 115);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the Delete Pokédex Entry form.
     */
    private void formatFields() {
        pokedexNumberField = new JTextField();
        pokedexNumberField.setFont(new Font("Courier New", Font.PLAIN, 12));
        pokedexNumberField.setPreferredSize(new Dimension(300, 40));
    }

    /**
     * Adds the text fields for the Delete Pokédex Entry form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Pokédex Number:"));
        inputPanel.add(pokedexNumberField);
    }

    /**
     * Configures the input fields and their layout for the Delete Pokédex Entry form.
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
     * Configures the delete and cancel buttons for the Delete Pokédex Entry form.
     */
    private void setUpButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.decode("#cc0010"));

        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets up action listeners for user interactions with the form components.
     */
    /**
     * Sets up action listeners for user interactions with the form components.
     */
    private void setUpListeners() {
        pokedexNumberField.addActionListener(e -> confirmAndDelete());
        deleteButton.addActionListener(e -> confirmAndDelete());
        cancelButton.addActionListener(e -> frame.dispose());

        deleteButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmAndDelete();
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
     * Sets up key bindings for the DeleteTrainer form, including the Escape key to close the form.
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
     * Confirms the deletion of the trainer by showing the species name and proceeds to delete if confirmed.
     */
    private void confirmAndDelete() {
        try {
            int pokedexNumber = Integer.parseInt(pokedexNumberField.getText().trim());
            PokedexBLL pokedexBLL = new PokedexBLL(connection);

            String speciesName = pokedexBLL.getSpeciesNameByPokedexNumber(pokedexNumber);

            if (speciesName == null) {
                JOptionPane.showMessageDialog(frame,
                        "No entry found with Pokédex Number " + pokedexNumber + ".",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete " + speciesName + "?",
                    "Delete Pokédex Entry",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = pokedexBLL.deletePokedexEntry(pokedexNumber);
                if (success) {
                    JOptionPane.showMessageDialog(frame,
                            speciesName + " deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshCallback.refreshTable();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Failed to delete entry. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Pokédex Number must be a valid number.",
                    "Pokédex Number Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(frame,
                    ex.getMessage(),
                    "Permission Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "Unexpected Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
