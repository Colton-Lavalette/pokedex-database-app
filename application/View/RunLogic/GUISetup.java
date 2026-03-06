package View.RunLogic;

import DAL.DatabaseConnector;
import View.CRUDLogic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

/**
 * GUISetup is responsible for setting up the graphical user interface (GUI) for the application.
 * It manages the layout, buttons, and integrates with the database connector.
 */
public class GUISetup {
    private final DatabaseConnector dbConnector;
    private final Connection connection;
    private final JFrame frame;
    private JPanel tablePanel;
    private JMenuItem showTrainersItem;
    private JMenuItem showPokedexItem;
    private JMenuItem showTypeChartItem;
    private JMenuItem showRegionsItem;
    private JMenuItem logoutItem;
    private JMenuItem exitItem;
    private JMenuItem newTrainerItem;
    private JMenuItem newPokedexEntryItem;
    private JMenuItem deleteTrainerItem;
    private JMenuItem deletePokedexEntryItem;
    private JMenuItem deleteRegionItem;
    private JMenuItem deleteTypeItem;
    private JMenuItem newPokemonItem;
    private JMenuItem newRegionItem;
    private JMenuItem deletePokemonItem;
    private JMenuItem newTypeAssignmentItem;
    private JMenuItem updateLevelItem;
    private JMenuItem showPokemonItem;
    private JMenuItem newTypeItem;
    private JMenuItem deleteTypeAssignmentItem;

    /**
     * Constructor that initializes the GUI setup with the provided frame, database connector, and connection.
     *
     * @param frame       The JFrame that holds the entire application window.
     * @param dbConnector The database connector instance for managing database connections.
     * @param connection  The connection to the database used by the application.
     */
    public GUISetup(JFrame frame, DatabaseConnector dbConnector, Connection connection) {
        this.dbConnector = dbConnector;
        this.connection = connection;
        this.frame = frame;
    }

    /**
     * Configures the main frame of the application.
     * Sets up the layout, background color, and primary panels for navigation and content.
     */
    private void setUpFrame() {
        frame.getContentPane().setBackground(Color.WHITE);
        frame.getContentPane().setLayout(new BorderLayout());
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(Color.decode("#FFFFFF"));
        frame.add(tablePanel, BorderLayout.CENTER);
    }

    /**
     * Handles the logout process by confirming the user's intention to disconnect from the database
     * and return to the log-in screen.
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Return to login?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            dbConnector.disconnect();
            System.out.println("\nUser logged out.");
            frame.dispose();
            GUIExecute.run(); // Re-run to return to log-in window.
        }
    }

    /**
     * Handles the closure process by confirming the user's intention to disconnect from the database
     * and close the application.
     */
    private void closeApp() {
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to close the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dbConnector.disconnect();
            System.out.println("\nClosing application…");
            System.out.println("Application closed.");
            System.exit(0);
        }
    }

    /**
     * Sets up the GUI components such as buttons, actions for button clicks, and window close event.
     * This method configures the main window and handles user interactions.
     */
    public void setUpGUI() {
        setUpFrame();
        setUpMenuBar();
        setUpListeners();
        setUpKeyBindings();
        frame.setFocusable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        SwingUtilities.invokeLater(frame::requestFocusInWindow);
    }

    /**
     * Sets up the menu bar with different menus and their respective menu items.
     */
    private void setUpMenuBar() {
        CustomMenuBar customMenuBar = new CustomMenuBar();

        setUpFileMenu(customMenuBar);
        setUpShowMenu(customMenuBar);
        setUpNewMenu(customMenuBar);
        setUpUpdateMenu(customMenuBar);
        setUpDeleteMenu(customMenuBar);

        frame.setJMenuBar(customMenuBar);
    }

    /**
     * Sets up the "File" menu with logout and exit options.
     */
    private void setUpFileMenu(CustomMenuBar customMenuBar) {
        JMenu fileMenu = new JMenu("File");

        logoutItem = new JMenuItem("Logout");
        exitItem = new JMenuItem("Exit");

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        customMenuBar.add(fileMenu);
    }

    /**
     * Sets up the "Show" menu with options to show trainers, Pokédex, type chart, and regions.
     */
    private void setUpShowMenu(CustomMenuBar customMenuBar) {
        JMenu showMenu = new JMenu("Show");

        showTrainersItem = new JMenuItem("Trainers");
        showPokedexItem = new JMenuItem("Pokédex");
        showPokemonItem = new JMenuItem("Pokémon");
        showTypeChartItem = new JMenuItem("Type Chart");
        showRegionsItem = new JMenuItem("Regions");

        showMenu.add(showTrainersItem);
        showMenu.add(showPokedexItem);
        showMenu.add(showPokemonItem);
        showMenu.add(showTypeChartItem);
        showMenu.add(showRegionsItem);

        customMenuBar.add(showMenu);
    }

    /**
     * Sets up the "New" menu with options to add various entries.
     */
    private void setUpNewMenu(CustomMenuBar customMenuBar) {
        JMenu newMenu = new JMenu("New");

        newTrainerItem = new JMenuItem("Trainer");
        newPokedexEntryItem = new JMenuItem("Pokédex Entry");
        newPokemonItem = new JMenuItem("Pokémon");
        newTypeItem = new JMenuItem("Type");
        newRegionItem = new JMenuItem("Region");
        newTypeAssignmentItem = new JMenuItem("Type Assignment");

        newMenu.add(newTrainerItem);
        newMenu.add(newPokedexEntryItem);
        newMenu.add(newPokemonItem);
        newMenu.add(newTypeItem);
        newMenu.add(newRegionItem);
        newMenu.add(newTypeAssignmentItem);
        customMenuBar.add(newMenu);
    }

    /**
     * Sets up the "Update" menu with options to show trainers, Pokédex, type chart, and regions.
     */
    private void setUpUpdateMenu(CustomMenuBar customMenuBar) {
        JMenu updateMenu = new JMenu("Update");

        updateLevelItem = new JMenuItem("Pokémon Level");

        updateMenu.add(updateLevelItem);

        customMenuBar.add(updateMenu);
    }
    /**
     * Sets up the "Delete" menu with options to delete various entries.
     */
    private void setUpDeleteMenu(CustomMenuBar customMenuBar) {
        JMenu deleteMenu = new JMenu("Delete");

        deleteTrainerItem = new JMenuItem("Trainer");
        deletePokedexEntryItem = new JMenuItem("Pokédex Entry");
        deletePokemonItem = new JMenuItem("Pokémon");
        deleteTypeItem = new JMenuItem("Type");
        deleteRegionItem = new JMenuItem("Region");
        deleteTypeAssignmentItem = new JMenuItem("Type Assignment");

        deleteMenu.add(deleteTrainerItem);
        deleteMenu.add(deletePokedexEntryItem);
        deleteMenu.add(deletePokemonItem);
        deleteMenu.add(deleteTypeItem);
        deleteMenu.add(deleteRegionItem);
        deleteMenu.add(deleteTypeAssignmentItem);

        customMenuBar.add(deleteMenu);
    }


    /**
     * Sets up event listeners for buttons and frame interactions.
     * Includes actions for button clicks, key presses, and window close events.
     */
    private void setUpListeners() {
        showTrainersItem.addActionListener(e -> showTrainerTable());
        showPokedexItem.addActionListener(e -> showPokedexTable());
        showPokemonItem.addActionListener(e -> showPokemonTable());
        showTypeChartItem.addActionListener(e -> showTypeChartTable());
        showRegionsItem.addActionListener(e -> showRegionTable());
        
        newTrainerItem.addActionListener(e -> newTrainer());
        newPokedexEntryItem.addActionListener(e -> newPokedexEntry());
        newPokemonItem.addActionListener(e -> newPokemon());
        newTypeItem.addActionListener(e -> newType());
        newRegionItem.addActionListener(e -> newRegion());
        newTypeAssignmentItem.addActionListener(e -> assignType());

        updateLevelItem.addActionListener(e -> levelUp());

        deleteTrainerItem.addActionListener(e -> deleteTrainer());
        deletePokedexEntryItem.addActionListener(e -> deletePokedexEntry());
        deletePokemonItem.addActionListener(e -> deletePokemon());
        deleteTypeItem.addActionListener(e -> deleteType());
        deleteRegionItem.addActionListener(e -> deleteRegion());
        deleteTypeAssignmentItem.addActionListener(e -> removeType());
        
        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> closeApp());
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApp();
            }
        });
    }

    /**
     * Refreshes the table panel by removing all existing components and adding the provided table.
     *
     * @param tableWithScrollPane The new table component wrapped in a scroll pane to display.
     */
    private void refreshTablePanel(JPanel tableWithScrollPane) {
        tablePanel.removeAll();
        tablePanel.add(tableWithScrollPane, BorderLayout.CENTER);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    /**
     * Configures key bindings for the Database Window.
     * Binds the Escape key to an action that closes the window, ensuring
     * that it works regardless of which component is focused.
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
                logout();
            }
        });
    }

    /**
     * Displays the Trainer table by creating and setting up the panel for the trainer table.
     */
    private void showTrainerTable() {
        TrainerTable trainerTableModel = new TrainerTable(connection);
        refreshTablePanel(trainerTableModel.createTrainerTable());
    }

    /**
     * Displays the Pokédex entries table by creating and setting up the panel for the Pokédex table.
     */
    private void showPokedexTable() {
        PokedexTable pokedexTableModel = new PokedexTable(connection);
        refreshTablePanel(pokedexTableModel.createPokedexTable());
    }

    /**
     * Displays the Pokémon table by creating and setting up the panel for the Pokémon table.
     */
    private void showPokemonTable() {
        PokemonTable pokemonTable = new PokemonTable(connection);
        refreshTablePanel(pokemonTable.createPokemonTable());
    }

    /**
     * Displays the type chart table by creating and setting up the panel for the type chart table.
     */
    private void showTypeChartTable() {
        TypeChartEntryTable typeChartEntryTable = new TypeChartEntryTable(connection);
        refreshTablePanel(typeChartEntryTable.createTypeChartEntryTable());
    }

    /**
     * Displays the region table by creating and setting up the panel for the region table.
     */
    private void showRegionTable() {
        RegionTable regionTable = new RegionTable(connection);
        refreshTablePanel(regionTable.createRegionTable());
    }

    /**
     * Opens a dialog to add a new trainer and refreshes the trainer table upon successful addition.
     */
    private void newTrainer() {
        new NewTrainer(connection, this::showTrainerTable);
    }

    /**
     * Opens a dialog to add a new Pokédex entry and refreshes the Pokédex table upon successful addition.
     */
    private void newPokedexEntry() {
        new NewPokedexEntry(connection, this::showPokedexTable);
    }

    /**
     * Opens a dialog to add a new Pokémon and refreshes the Pokémon table upon successful addition.
     */
    private void newPokemon() {
        new NewPokemon(connection, this::showPokemonTable);
    }

    /**
     * Opens a dialog to add a new type chart entry and refreshes the type chart table upon successful addition.
     */
    private void newType() {
        new NewType(connection, this::showTypeChartTable);
    }

    /**
     * Opens a dialog to add a new region and refreshes the region table upon successful addition.
     */
    private void newRegion() {
        new NewRegion(connection, this::showRegionTable);
    }

    /**
     * Opens a dialog to assign a new type to a Pokémon in the Pokédex and refreshes the Pokédex table upon successful assignment.
     */
    private void assignType() {
        new AssignType(connection, this::showPokedexTable);
    }

    /**
     * Opens a dialog to update Pokémon level and refreshes the Pokémon table upon successful update.
     */
    private void levelUp() {
        new UpdateLevel(connection, this::showPokemonTable);
    }

    /**
     * Opens a dialog to delete a trainer and refreshes the Trainer table upon successful deletion.
     */
    private void deleteTrainer() {
        new DeleteTrainer(connection, this::showTrainerTable);
    }

    /**
     * Opens a dialog to delete a Pokédex Entry and refreshes the Pokédex table upon successful deletion.
     */
    private void deletePokedexEntry() {
        new DeletePokedexEntry(connection, this::showPokedexTable);
    }

    /**
     * Opens a dialog to delete a Pokémon and refreshes the Pokémon table upon successful deletion.
     */
    private void deletePokemon() {
        new DeletePokemon(connection, this::showPokemonTable);
    }

    /**
     * Opens a dialog to delete a type and refreshes the type chart table upon successful deletion.
     */
    private void deleteType() {
        new DeleteType(connection, this::showTypeChartTable);
    }

    /**
     * Opens a dialog to delete a region and refreshes the region table upon successful deletion.
     */
    private void deleteRegion() {
        new DeleteRegion(connection, this::showRegionTable);
    }

    /**
     * Opens a dialog to remove a type assignment from a Pokémon and refreshes the Pokédex table upon successful deletion.
     */
    private void removeType() {
        new RemoveType(connection, this::showPokedexTable);
    }
}