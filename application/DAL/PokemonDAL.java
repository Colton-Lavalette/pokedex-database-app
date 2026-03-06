package DAL;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Data Access Layer (DAL) class for performing CRUD operations on the Pokémon table.
 */
public class PokemonDAL {
    private final Connection connection;

    /**
     * Constructor for PokemonDAL.
     *
     * @param connection the database connection
     */
    public PokemonDAL(Connection connection) {
        this.connection = connection;
    }

    /**
     * Checks if a Pokémon exists in the database based on the pokemonId.
     *
     * @param pokemonId the Pokémon ID to check
     * @return true if the Pokémon exists, false otherwise
     */
    public boolean pokemonExists(int pokemonId) {
        String query = "SELECT COUNT(*) FROM Pokemon WHERE pokemon_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pokemonId);
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
     * Inserts a new Pokémon into the Pokémon table.
     *
     * @param pokemon the Pokémon object containing Pokémon details
     * @return true if the Pokémon was inserted, false otherwise
     * @throws SQLException if a database error occurs, including unique constraint violations
     */
    public boolean insertPokemon(Pokemon pokemon) throws SQLException {
        String query = "INSERT INTO Pokemon (pokedex_number, trainer_id, level) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pokemon.getPokedexNumber());
            preparedStatement.setInt(2, pokemon.getTrainerId());
            preparedStatement.setInt(3, pokemon.getLevel());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves all Pokémon from the Pokémon table.
     * Utilizes the stored procedure to join information from other tables.
     *
     * @return a list of Region objects
     */
    public List<Pokemon> getAllPokemon() {
        List<Pokemon> pokemonList = new ArrayList<>();
        String query = "CALL GetPokemonWithTrainers";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                int pokemonId = resultSet.getInt("pokemon_id");
                String speciesName = resultSet.getString("species_name");
                String types = resultSet.getString("pokemon_types");
                int level = resultSet.getInt("level");
                String trainerTitle = resultSet.getString("trainer_title");

                Pokemon pokemon = new Pokemon(pokemonId, types, speciesName, level, trainerTitle);
                pokemonList.add(pokemon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pokemonList;
    }

    /**
     * Deletes a Pokémon from the database if the Pokémon exists.
     *
     * @param pokemonId the ID of the Pokémon to delete
     * @return true if the Pokémon was successfully deleted, false otherwise
     * @throws SQLException if a database error occurs, including inadequate permissions
     */
    public boolean deletePokemon(int pokemonId) throws SQLException {
        if (!pokemonExists(pokemonId)) {
            return false;
        }
        String query = "DELETE FROM Pokemon WHERE pokemon_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pokemonId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves details of a Pokémon, including trainer name, trainer class, and species name.
     *
     * @param pokemonId the ID of the Pokémon
     * @return a string containing the Pokémon's species name, trainer name, and trainer class, or null if not found
     */
    public String getPokemonDetails(int pokemonId) {
        String query = """
        SELECT pkmn.species_name, tr.name AS trainer_name, tr.trainer_class
        FROM Pokemon AS pk
        JOIN Pokedex AS pkmn ON pk.pokedex_number = pkmn.pokedex_number
        JOIN Trainer AS tr ON pk.trainer_id = tr.trainer_id
        WHERE pk.pokemon_id = ?
    """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pokemonId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String speciesName = resultSet.getString("species_name");
                String trainerName = resultSet.getString("trainer_name");
                String trainerClass = resultSet.getString("trainer_class");
                return String.format("%s %s's %s", trainerClass, trainerName, speciesName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the level of a Pokémon in the database using a stored procedure.
     *
     * @param pokemonId the ID of the Pokémon to update
     * @param newLevel the new level to assign to the Pokémon
     * @return true if the level was successfully updated, false otherwise
     * @throws RuntimeException if a database error occurs
     */
    public boolean updatePokemonLevel(int pokemonId, int newLevel) {
        String query = "{CALL UpdatePokemonLevel(?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setInt(1, pokemonId);
            callableStatement.setInt(2, newLevel);

            int rowsAffected = callableStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating Pokémon level: " + e.getMessage());
        }
    }

    /**
     * Retrieves Pokédex number of a Pokémon based on its ID.
     *
     * @param pokemonId the ID of the Pokémon
     * @return the Pokédex number if found, otherwise 0.
     */
    public int getPokedexNumberById(int pokemonId) throws SQLException {
        String query = "SELECT pokedex_number FROM Pokemon WHERE pokemon_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pokemonId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("pokedex_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return 0;
    }

}
