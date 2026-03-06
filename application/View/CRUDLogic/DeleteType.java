package View.CRUDLogic;

import BLL.TypeChartBLL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;

/**
 * A GUI form for deleting a type from the database.
 */
public class DeleteType {
    private final Connection connection;
    private final RefreshTableCallback refreshCallback;

    private JFrame frame;
    private JTextField typeIdField;
    private JButton deleteButton;
    private JButton cancelButton;
    private JPanel inputPanel;

    /**
     * Constructs the Delete Type form with a database connection.
     *
     * @param connection      the database connection for interacting with type data
     * @param refreshCallback the callback to refresh the type table upon successful deletion
     */
    public DeleteType(Connection connection, RefreshTableCallback refreshCallback) {
        this.connection = connection;
        this.refreshCallback = refreshCallback;

        setUpFrame();
        setUpInputFields();
        setUpButtons();
        setUpListeners();
        setUpKeyBindings();

        frame.setFocusable(true);
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> typeIdField.requestFocusInWindow());
        frame.setVisible(true);
    }

    /**
     * Sets up the main frame for the DeleteType form.
     */
    private void setUpFrame() {
        frame = new JFrame("Delete Type");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 115);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);
    }

    /**
     * Applies formatting to the text fields for the Delete Type form.
     */
    private void formatFields() {
        typeIdField = new JTextField();
        typeIdField.setFont(new Font("Courier New", Font.PLAIN, 12));
        typeIdField.setPreferredSize(new Dimension(300, 40));
    }

    /**
     * Adds the text fields for the Delete Type form and their labels to the input panel.
     */
    private void addFields() {
        inputPanel.add(new JLabel("Type ID:"));
        inputPanel.add(typeIdField);
    }

    /**
     * Configures the input fields and their layout for the DeleteType form.
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
     * Configures the delete and cancel buttons for the DeleteType form.
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
        typeIdField.addActionListener(e -> confirmAndDelete());
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
     * Sets up key bindings for the DeleteType form, including the Escape key to close the form.
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
     * Confirms the deletion of the type by showing the type's name and proceeds to delete if confirmed.
     */
    private void confirmAndDelete() {
        try {
            int typeId = Integer.parseInt(typeIdField.getText().trim());
            TypeChartBLL typeChartBLL = new TypeChartBLL(connection);

            String typeName = typeChartBLL.getTypeNameById(typeId);

            if (typeName == null) {
                JOptionPane.showMessageDialog(frame,
                        "No type found with ID " + typeId + ".",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete the " + typeName + " type?",
                    "Delete Type",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = typeChartBLL.deleteType(typeId);
                if (success) {
                    JOptionPane.showMessageDialog(frame,
                            typeName + " type deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshCallback.refreshTable();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Failed to delete type. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Type ID must be a valid number.",
                    "ID Error",
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
