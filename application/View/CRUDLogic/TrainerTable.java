package View.CRUDLogic;

import BLL.TrainerBLL;
import DAL.Trainer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the table model for displaying a list of trainers.
 * This model is used by a JTable to display trainer details such as their ID, name and age.
 */
public class TrainerTable extends AbstractTableModel {
    private final List<Trainer> trainers;
    private final JPanel tablePanel;

    /**
     * Constructor that initializes the table with trainer data from the database.
     *
     * @param connection The database connection object used to fetch the data.
     */
    public TrainerTable(Connection connection) {
        this.tablePanel = new JPanel();
        TrainerBLL trainerBLL = new TrainerBLL(connection);
        this.trainers = trainerBLL.getAllTrainers();
    }

    /**
     * Creates and returns the JPanel that contains the trainer table with dynamic resizing and sorting functionality.
     *
     * @return The JPanel containing the JTable with the trainer data.
     */
    public JPanel createTrainerTable() {
        JTable table = new JTable(this);
        TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<>(this);
        configureRowSorter(sorter);
        table.setRowSorter(sorter);

        customizeTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        customizeScrollPane(scrollPane);

        return setupTablePanel(scrollPane);
    }

    /**
     * Configures the TableRowSorter for custom sorting behavior.
     *
     * @param sorter The TableRowSorter to configure.
     */
    private void configureRowSorter(TableRowSorter<AbstractTableModel> sorter) {
        // Custom comparator to sort trainer IDs and ages (columns 0 & 2) as integers.
        sorter.setComparator(0, Comparator.comparingInt(o -> (Integer) o));
        sorter.setComparator(2, Comparator.comparingInt(o -> (Integer) o));
    }

    /**
     * Customizes the appearance of the JTable.
     *
     * @param table The JTable to customize.
     */
    private void customizeTable(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setBackground(Color.decode("#FFFFFF"));
        table.setForeground(Color.decode("#000000"));
        table.setSelectionBackground(Color.decode("#ff738c"));
        table.setSelectionForeground(Color.decode("#FFFFFF"));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.decode("#2c363d"));
        tableHeader.setForeground(Color.decode("#FFFFFF"));
    }

    /**
     * Customizes the JScrollPane containing the JTable.
     *
     * @param scrollPane The JScrollPane to customize.
     */
    private void customizeScrollPane(JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(Color.decode("#FFFFFF"));
        scrollPane.setBackground(Color.decode("#999999"));
    }

    /**
     * Sets up and returns the table panel that holds the JScrollPane.
     *
     * @param scrollPane The JScrollPane to add to the panel.
     * @return The JPanel containing the JScrollPane.
     */
    private JPanel setupTablePanel(JScrollPane scrollPane) {
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(Color.decode("#FFFFFF"));
        tablePanel.removeAll();
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.revalidate();
        tablePanel.repaint();

        return tablePanel;
    }

    @Override
    public int getRowCount() {
        return trainers.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Trainer trainer = trainers.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> trainer.getTrainerId();
            case 1 -> trainer.getTrainerName();
            case 2 -> trainer.getTrainerAge();
            case 3 -> trainer.getHometown();
            case 4 -> trainer.getTrainerClass();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Trainer ID №";
            case 1 ->  "Name";
            case 2 -> "Age";
            case 3 -> "Hometown";
            case 4 -> "Trainer Class";
            default -> "";
        };
    }
}
