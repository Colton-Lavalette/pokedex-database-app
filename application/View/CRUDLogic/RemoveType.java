package View.CRUDLogic;

import BLL.PokedexBLL;
import BLL.TypeChartBLL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for removing a type from a Pokémon
 */
public class RemoveType {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField pokedexNumberField;
    private JTextField typeIdField;
    private JButton deleteButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the RemoveType form with a database connection.
     *
     * @param connection      the database connection for interacting with type data
     * @param refreshCallback the callback to refresh the type table upon successful deletion
     */
    public RemoveType(Connection connection, RefreshTableCallback refreshCallback) {
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
     * Sets up the main frame for the RemoveType form.
     */
    private void setUpFrame() {
        frame = new JFrame("Remove Type");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the Remove Type form.
     */
    private void formatFields() {
        pokedexNumberField = new JTextField();
        pokedexNumberField.setFont(new Font("Courier New", Font.PLAIN, 12));
        pokedexNumberField.setPreferredSize(new Dimension(300, 20));

        typeIdField = new JTextField();
        typeIdField.setFont(new Font("Courier New", Font.PLAIN, 12));
        typeIdField.setPreferredSize(new Dimension(300, 20));
    }

    /**
     * Adds the text fields for the Remove Type form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Pokédex Number:"));
        inputPanel.add(pokedexNumberField);
        inputPanel.add(new JLabel("Type ID:"));
        inputPanel.add(typeIdField);
    }

    /**
     * Configures the input fields and their layout for the AssignType form.
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
     * Configures the confirmAndDelete and cancel buttons for the RemoveType form.
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
    private void setUpListeners() {
        pokedexNumberField.addActionListener(e -> typeIdField.requestFocus());
        typeIdField.addActionListener(e -> confirmAndRemove());

        deleteButton.addActionListener(e -> confirmAndRemove());
        cancelButton.addActionListener(e -> frame.dispose());

        deleteButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmAndRemove();
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
     * Sets up key bindings for the AssignType form, including the Escape key to close the form.
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
     * Confirms the removal of the type from the Pokémon by showing the Pokémon and type names and proceeds to remove if confirmed.
     */
    private void confirmAndRemove() {
        try {
            PokedexBLL pokedexBLL = new PokedexBLL(connection);
            TypeChartBLL typeChartBLL = new TypeChartBLL(connection);

            int pokedexNumber = Integer.parseInt(pokedexNumberField.getText().trim());
            int typeId = Integer.parseInt(typeIdField.getText().trim());
            String typeName = typeChartBLL.getTypeNameById(typeId);
            String speciesName = pokedexBLL.getSpeciesNameByPokedexNumber(pokedexNumber);

            if (typeName == null) {
                JOptionPane.showMessageDialog(frame,
                        "No type found with ID " + typeId + ".",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to remove " + typeName + " type from " + speciesName + "?",
                    "Remove Type",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = pokedexBLL.removeType(pokedexNumber, typeId, connection);
                if (success) {
                    JOptionPane.showMessageDialog(frame,
                            typeName + " type successfully removed from " + speciesName + "!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshCallback.refreshTable();
                    frame.dispose();
                }
            }
        } catch (IllegalArgumentException ex) {
            String errorMessage = ex.getMessage();
            System.out.println(errorMessage);
            if (errorMessage.contains("Pokédex number")) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid Pokédex number: " + errorMessage,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (errorMessage.contains("Type ID")) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid Type ID: " + errorMessage,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (errorMessage.contains("No types assigned")) {
                JOptionPane.showMessageDialog(frame,
                        errorMessage,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        errorMessage,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            }
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
