package View.CRUDLogic;

import BLL.TypeChartBLL;
import DAL.TypeChartEntry;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the table model for displaying a list of type chart entries.
 * This model is used by a JTable to display Pokédex details such as type ID and type name.
 */
public class TypeChartEntryTable extends AbstractTableModel {
    private final List<TypeChartEntry> typeChartEntries;
    private final JPanel tablePanel;

    /**
     * Constructor that initializes the table with Pokédex entry data from the database.
     *
     * @param connection The database connection object used to fetch the data.
     */
    public TypeChartEntryTable(Connection connection) {
        this.tablePanel = new JPanel();
        TypeChartBLL typeChartBLL = new TypeChartBLL(connection);
        this.typeChartEntries = typeChartBLL.getAllTypeChartEntries();
    }

    /**
     * Creates and returns the JPanel that contains the type chart entry table with dynamic resizing and sorting functionality.
     *
     * @return The JPanel containing the JTable with the type chart entry data.
     */
    public JPanel createTypeChartEntryTable() {
        JTable table = new JTable(this);
        TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<>(this);
        configureRowSorter(sorter);
        table.setRowSorter(sorter);

        customizeTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        customizeScrollPane(scrollPane);

        Dimension tablePreferredSize = new Dimension(300, 400);
        table.setPreferredScrollableViewportSize(tablePreferredSize);
        scrollPane.setPreferredSize(tablePreferredSize);

        return setupTablePanel(scrollPane);
    }

    /**
     * Configures the TableRowSorter for custom sorting behavior.
     *
     * @param sorter The TableRowSorter to configure.
     */
    private void configureRowSorter(TableRowSorter<AbstractTableModel> sorter) {
        // Custom comparator to sort type IDs (column 0) as integers.
        sorter.setComparator(0, Comparator.comparingInt(o -> (Integer) o));
    }

    /**
     * Customizes the appearance of the JTable.
     *
     * @param table The JTable to customize.
     */
    private void customizeTable(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
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
        scrollPane.setBackground(Color.decode("#FFFFFF"));
    }

    /**
     * Sets up and returns the table panel that holds the JScrollPane.
     *
     * @param scrollPane The JScrollPane to add to the panel.
     * @return The JPanel containing the JScrollPane.
     */
    private JPanel setupTablePanel(JScrollPane scrollPane) {
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tablePanel.setBackground(Color.decode("#FFFFFF"));
        tablePanel.removeAll();
        tablePanel.add(scrollPane);
        tablePanel.revalidate();
        tablePanel.repaint();
        return tablePanel;
    }

    @Override
    public int getRowCount() {
        return typeChartEntries.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TypeChartEntry typeChartEntry = typeChartEntries.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> typeChartEntry.getTypeId();
            case 1 -> typeChartEntry.getTypeName();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Type ID №";
            case 1 ->  "Type Name";
            default -> "";
        };
    }
}
