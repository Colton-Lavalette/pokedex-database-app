package View.CRUDLogic;

import BLL.PokemonBLL;
import DAL.Pokemon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the table model for displaying a list of Pokémon.
 * This model is used by a JTable to display Pokémon details such as their ID, species name and types.
 */
public class PokemonTable extends AbstractTableModel {
    private final List<Pokemon> pokemonList;
    private final JPanel tablePanel;

    /**
     * Constructor that initializes the table with Pokémon data from the database.
     *
     * @param connection The database connection object used to fetch the data.
     */
    public PokemonTable(Connection connection) {
        this.tablePanel = new JPanel();
        PokemonBLL pokemonBLL = new PokemonBLL(connection);
        this.pokemonList = pokemonBLL.getAllPokemon();
    }

    /**
     * Creates and returns the JPanel that contains the Pokémon table with dynamic resizing and sorting functionality.
     *
     * @return The JPanel containing the JTable with the Pokémon data.
     */
    public JPanel createPokemonTable() {
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
        // Custom comparator to sort Pokémon IDs and levels (columns 0 & 3) as integers.
        sorter.setComparator(0, Comparator.comparingInt(o -> (Integer) o));
        sorter.setComparator(3, Comparator.comparingInt(o -> (Integer) o));

        // Disable sorting for the "Types" column (column 2) because types are packed
        // into a String and thus cannot be alphabetized within a cell
        sorter.setSortable(2, false);
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
        return pokemonList.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pokemon pokemon = pokemonList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> pokemon.getPokemonId();
            case 1 -> pokemon.getSpeciesName();
            case 2 -> pokemon.getTypes();
            case 3 -> pokemon.getLevel();
            case 4 -> pokemon.getTrainerTitle();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Pokemon ID №";
            case 1 ->  "Species Name";
            case 2 -> "Pokémon Type(s)";
            case 3 -> "Level";
            case 4 -> "Trainer";
            default -> "";
        };
    }
}
