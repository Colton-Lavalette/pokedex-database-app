package BLL;

import DAL.Pokemon;
import DAL.PokemonDAL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Business Logic Layer (BLL) class for managing Pokémon.
 */
public class PokemonBLL {
    private final PokemonDAL pokemonDAL;

    /**
     * Constructor for PokemonBLL.
     *
     * @param connection the database connection
     */
    public PokemonBLL(Connection connection) {
        this.pokemonDAL = new PokemonDAL(connection);
    }

    /**
     * Validates the Pokémon data before adding it to the database.
     *
     * @param pokedexNumber the Pokémon's Pokédex number
     * @param trainerId     the Pokémon's trainer ID
     * @param level         the Pokémon's level
     * @throws IllegalArgumentException if any validation fails
     */
    private void validatePokemonData(int pokedexNumber, int trainerId, int level) {
        if (pokedexNumber <= 0) {
            throw new IllegalArgumentException("Pokédex number must be greater than 0.");
        }
        if (trainerId <= 0) {
            throw new IllegalArgumentException("Trainer ID must be greater than 0.");
        }
        if (level <= 0) {
            throw new IllegalArgumentException("Pokémon level must be greater than 0.");
        }
    }

    /**
     * Adds a new Pokémon by validating and passing the details to the DAL layer.
     *
     * @param pokedexNumber the Pokédex number of the Pokémon
     * @param trainerId the Pokémon's trainer's ID
     * @param level the Pokémon's level
     * @return true if Pokémon added, false otherwise
     */
    public boolean addPokemon(int pokedexNumber, int trainerId, int level) {
        try {
            validatePokemonData(pokedexNumber, trainerId, level);
            Pokemon pokemon = new Pokemon(pokedexNumber, trainerId, level);
            pokemonDAL.insertPokemon(pokemon);
            System.out.println("\nPokemon added successfully.");
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
                System.err.println("Database permission error: " + e.getMessage());
                throw new RuntimeException("Permission denied. Current user does not have permission to perform this function.");
            }
            System.err.println("Database error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a Pokémon after confirming the Pokémon exists.
     *
     * @param pokemonId the ID of the Pokémon to delete
     * @return true if the Pokémon was successfully deleted, false otherwise
     */
    public boolean deletePokemon(int pokemonId) {
        try {
            if (!pokemonDAL.pokemonExists(pokemonId)) {
                System.err.println("Pokémon with ID " + pokemonId + " does not exist.");
                return false;
            }
            boolean deleted = pokemonDAL.deletePokemon(pokemonId);
            if (deleted) {
                System.out.println("Pokémon with ID " + pokemonId + " deleted successfully.");
            } else {
                System.err.println("Failed to delete Pokémon with ID " + pokemonId + ".");
            }
            return deleted;
        } catch (SQLException e) {
            if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
                System.err.println("Database permission error: " + e.getMessage());
                throw new RuntimeException("Permission denied. Current user does not have permission to delete this Pokémon.");
            }
            System.err.println("Database error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error occurred during deletion: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a Pokémon's level by passing details to the DAL layer.
     *
     * @param pokemonId the Pokédex number of the Pokémon
     * @param level the Pokémon's level
     * @return true if level updated, false otherwise
     */
    public boolean updateLevel(int pokemonId, int level) {
        if (level < 1 || level > 100) {
            throw new IllegalArgumentException("Out of range. Level must be between 1 and 100.");
        }
        try {
            pokemonDAL.updatePokemonLevel(pokemonId, level);
            System.out.println("\nLevel updated successfully.");
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the list of all Pokémon.
     *
     * @return a list of all Pokémon
     */
    public List<Pokemon> getAllPokemon() {
        return pokemonDAL.getAllPokemon();
    }

    /**
     * Retrieves detailed information about a Pokémon for display in delete confirmation.
     *
     * @param pokemonId the ID of the Pokémon to retrieve details for
     * @return a formatted string with species name, trainer class, and trainer name,
     *         or null if the Pokémon does not exist
     */
    public String getPokemonDetails(int pokemonId) {
        return pokemonDAL.getPokemonDetails(pokemonId);
    }
}
