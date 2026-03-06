package View.CRUDLogic;

import BLL.RegionBLL;
import DAL.Region;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the table model for displaying a list of regions.
 * This model is used by a JTable to display region details such as region ID and region name.
 */
public class RegionTable extends AbstractTableModel {
    private final List<Region> regions;
    private final JPanel tablePanel;

    /**
     * Constructor that initializes the table with region data from the database.
     *
     * @param connection The database connection object used to fetch the data.
     */
    public RegionTable(Connection connection) {
        this.tablePanel = new JPanel();
        RegionBLL regionBLL = new RegionBLL(connection);
        this.regions = regionBLL.getAllRegions();
    }

    /**
     * Creates and returns the JPanel that contains the region table with dynamic resizing and sorting functionality.
     *
     * @return The JPanel containing the JTable with the region data.
     */
    public JPanel createRegionTable() {
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
        // Custom comparator to sort region IDs (column 0) as integers.
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
        return regions.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Region region = regions.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> region.getRegionId();
            case 1 -> region.getRegionName();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Region ID";
            case 1 ->  "Region Name";
            default -> "";
        };
    }
}
