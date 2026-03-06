package View.CRUDLogic;

import BLL.PokedexBLL;
import DAL.PokedexEntry;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the table model for displaying a list of Pokédex entries.
 * This model is used by a JTable to display Pokédex details such as Pokédex number and species name.
 */
public class PokedexTable extends AbstractTableModel {
    private final List<PokedexEntry> pokedexEntries;
    private final JPanel tablePanel;

    /**
     * Constructor that initializes the table with Pokédex entry data from the database.
     *
     * @param connection The database connection object used to fetch the data.
     */
    public PokedexTable(Connection connection) {
        this.tablePanel = new JPanel();
        PokedexBLL pokedexBLL = new PokedexBLL(connection);
        this.pokedexEntries = pokedexBLL.getAllPokedexEntries();
    }

    /**
     * Creates and returns the JPanel that contains the Pokédex table with dynamic resizing and sorting functionality.
     *
     * @return The JPanel containing the JTable with the Pokédex entry data.
     */
    public JPanel createPokedexTable() {
        JTable table = new JTable(this);
        TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<>(this);
        configureRowSorter(sorter);
        table.setRowSorter(sorter);

        customizeTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        customizeScrollPane(scrollPane);

        Dimension tablePreferredSize = new Dimension(500, 400);
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
        // Disable sorting for the types column (column 2) because types are packed
        // into a String and thus cannot be alphabetized within a cell
        sorter.setSortable(2, false);

        // Custom comparator to sort Pokédex numbers (column 0) as integers.
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
        return pokedexEntries.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PokedexEntry pokedexEntry = pokedexEntries.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> pokedexEntry.getPokedexNumber();
            case 1 -> pokedexEntry.getSpeciesName();
            case 2 -> pokedexEntry.getPokemonTypes();
            case 3 -> pokedexEntry.getRegionName();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Pokédex №";
            case 1 ->  "Species Name";
            case 2 -> "Pokémon Types";
            case 3 -> "Region Name";
            default -> "";
        };
    }
}
