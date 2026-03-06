package DAL;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Data Access Layer (DAL) class for performing CRUD operations on the Pokédex table.
 */
public class PokedexDAL {
    private final Connection connection;

    /**
     * Constructor for PokedexDAL.
     *
     * @param connection the database connection
     */
    public PokedexDAL(Connection connection) {
        this.connection = connection;
    }

    /**
     * Checks if a Pokédex entry exists in the database based on the pokedexNumber.
     *
     * @param pokedexNumber the Pokédex number to check
     * @return true if the entry exists, false otherwise
     */
    public boolean pokedexEntryExists(int pokedexNumber) {
        String query = "SELECT COUNT(*) FROM Pokedex WHERE pokedex_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pokedexNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Inserts a new Pokédex entry into the Pokédex table.
     *
     * @param pokedexEntry the pokedexEntry object containing Pokédex entry details
     * @return true if the Pokédex entry was inserted, false otherwise
     * @throws SQLException if a database error occurs, including unique constraint violations
     */
    public boolean insertPokedexEntry(PokedexEntry pokedexEntry) throws SQLException {
        String query = "INSERT INTO Pokedex (pokedex_number, species_name, region_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pokedexEntry.getPokedexNumber());
            preparedStatement.setString(2, pokedexEntry.getSpeciesName());
            preparedStatement.setInt(3, pokedexEntry.getRegionId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves all Pokédex entries with associated types from the database.
     *
     * @return a list of Pokédex Entry objects with their types
     */
    public List<PokedexEntry> getAllPokedexEntries() {
        List<PokedexEntry> pokedexEntries = new ArrayList<>();
        String query = "CALL GetPokedexEntriesWithTypes()"; // Call the stored procedure

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                int pokedexNumber = resultSet.getInt("pokedex_number");
                String speciesName = resultSet.getString("species_name");
                String types = resultSet.getString("pokemon_types");
                String regionName = resultSet.getString("region_name");

                PokedexEntry pokedexEntry = new PokedexEntry(pokedexNumber, speciesName, types, regionName);
                pokedexEntries.add(pokedexEntry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pokedexEntries;
    }

    /**
     * Deletes a Pokédex entry from the database if the entry exists.
     *
     * @param pokedexNumber the Pokédex Number of the entry to delete
     * @return true if the Pokédex entry was successfully deleted, false otherwise
     * @throws SQLException if a database error occurs, including inadequate permissions
     */
    public boolean deletePokedexEntry(int pokedexNumber) throws SQLException {
        if (!pokedexEntryExists(pokedexNumber)) {
            return false;
        }
        String query = "DELETE FROM Pokedex WHERE pokedex_number = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pokedexNumber);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves the species name by its Pokédex number from the database.
     * Needed for confirmation in entry deletion.
     *
     * @param pokedexNumber the Pokédex number of the entry
     * @return the species name, or null if no entry is found
     */
    public String getSpeciesNameByPokedexNumber(int pokedexNumber) {
        String query = "SELECT species_name FROM Pokedex WHERE pokedex_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pokedexNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("species_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Assigns a type to a Pokémon by inserting a value
     * into the PokemonTypes Associative Entity table.
     *
     * @param pokedexNumber the Pokédex number of the Pokémon to which the type will be assigned
     * @param typeId the ID of the type to be assigned
     * @return true if the type was assigned, false otherwise
     * @throws SQLException if a database error occurs, including unique constraint violations
     */
    public boolean assignType(int pokedexNumber, int typeId) throws SQLException {
        String query = "INSERT INTO PokemonTypes (pokedex_number,  type_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pokedexNumber);
            preparedStatement.setInt(2, typeId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Removes a type from a Pokémon by deleting a value
     * from the PokemonTypes Associative Entity table.
     *
     * @param pokedexNumber the Pokédex number of the Pokémon from which the type will be removed
     * @param typeId the ID of the type to be removed
     * @return true if the type association was removed, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean removeType(int pokedexNumber, int typeId) throws SQLException {
        String query = "DELETE FROM PokemonTypes WHERE pokedex_number = ? AND type_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pokedexNumber);
            ps.setInt(2, typeId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }


    /**
     * Retrieves the count of types assigned to a Pokémon by its Pokédex number.
     *
     * @param pokedexNumber the Pokédex number of the Pokémon
     * @return the count of types assigned to the Pokémon
     * @throws SQLException if a database error occurs
     */
    public int getTypeCountForPokedexNumber(int pokedexNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM PokemonTypes WHERE pokedex_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pokedexNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Checks whether a Pokémon with a given Pokédex number has a specific type.
     * </p>
     * This method queries the database to determine if the Pokémon identified by
     * the provided Pokédex number is associated with the specified type ID.
     * It returns true if the Pokémon has the specified type, and false otherwise.
     *
     * @param pokedexNumber the Pokedex number of the Pokémon to check
     * @param typeId the ID of the type to check for
     * @return true if the Pokémon has the specified type, false otherwise
     * @throws SQLException if there is an error while querying the database
     */
    public boolean pokemonHasType(int pokedexNumber, int typeId) throws SQLException {
        String query = "SELECT COUNT(*) FROM PokemonTypes WHERE pokedex_number = ? AND type_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pokedexNumber);
            stmt.setInt(2, typeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
