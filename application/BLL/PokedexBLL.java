package BLL;

import DAL.PokedexEntry;
import DAL.PokedexDAL;
import DAL.TypeChartDAL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Business Logic Layer (BLL) class for managing Pokédex entries.
 */
public class PokedexBLL {
    private final PokedexDAL pokedexDAL;
    private TypeChartDAL typeChartDAL;

    /**
     * Constructor for PokedexBLL.
     *
     * @param connection the database connection
     */
    public PokedexBLL(Connection connection) {
        this.pokedexDAL = new PokedexDAL(connection);
    }

    /**
     * Validates the Pokédex entry data before adding it to the database.
     *
     * @param pokedexNumber   the number of the Pokédex entry
     * @param speciesName    the name of the Pokémon species
     * @throws IllegalArgumentException if any validation fails
     */
    private void validatePokedexData(int pokedexNumber, String speciesName, int regionId) {
        if (pokedexNumber <= 0) {
            throw new IllegalArgumentException("Pokédex number must be a positive integer.");
        }
        if (regionId <= 0) {
            throw new IllegalArgumentException("Region ID must be a positive integer.");
        }
        if (speciesName == null || speciesName.trim().isEmpty()) {
            throw new IllegalArgumentException("Species name cannot be empty.");
        }
    }

    /**
     * Adds a new Pokédex entry by validating and passing the details to the DAL layer.
     *
     * @return true if Pokédex entry added, false otherwise
     */
    public boolean addPokedexEntry(int pokedexNumber, String speciesName, int regionId) {
        try {
            validatePokedexData(pokedexNumber, speciesName, regionId);
            PokedexEntry pokedexEntry = new PokedexEntry(pokedexNumber, speciesName, regionId);
            pokedexDAL.insertPokedexEntry(pokedexEntry);
            System.out.println("\nPokédex entry added successfully.");
            return true;
        } catch (IllegalArgumentException e) {
        System.err.println("Validation error: " + e.getMessage());
        throw e;
    } catch (SQLException e) {
        if (e.getSQLState().equals("23000")) {
                if (e.getMessage().contains("duplicate key") || e.getMessage().contains("PRIMARY")) {
                    throw new RuntimeException("An entry with this number already exists!");
                } else if (e.getMessage().contains("Duplicate")) {
                    throw new RuntimeException("A Pokémon with this name already exists!");
                }
        } else if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
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
     * Retrieves the list of all Pokédex entries.
     *
     * @return a list of all Pokédex entries
     */
    public List<PokedexEntry> getAllPokedexEntries() {
        return pokedexDAL.getAllPokedexEntries();
    }

    /**
     * Deletes a Pokédex entry after confirming the entry exists.
     *
     * @param pokedexNumber the Pokédex number of the entry to delete
     * @return true if the entry was successfully deleted, false otherwise
     */
    public boolean deletePokedexEntry(int pokedexNumber) {
        try {
            if (!pokedexDAL.pokedexEntryExists(pokedexNumber)) {
                System.err.println("Pokémon with number " + pokedexNumber + " does not exist.");
                return false;
            }
            boolean deleted = pokedexDAL.deletePokedexEntry(pokedexNumber);
            if (deleted) {
                System.out.println("Pokémon with number " + pokedexNumber + " deleted successfully.");
            } else {
                System.err.println("Failed to delete Pokémon with number " + pokedexNumber + ".");
            }
            return deleted;
        } catch (SQLException e) {
            if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
                System.err.println("Database permission error: " + e.getMessage());
                throw new RuntimeException("Permission denied. Current user does not have permission to delete this Pokédex Entry.");
            }
            System.err.println("Database error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error occurred during deletion: " + e.getMessage());
            return false;
        }
    }

    /**
     * Assigns a type to a Pokédex entry in the database.
     *
     * @param pokedexNumber the number of the Pokédex entry to which the type will be assigned
     * @param typeId the ID of the type to be assigned
     * @param connection the current database connection, necessary for verifying type ID exists
     * @return true if the entry was successfully deleted, false otherwise
     */
    public boolean assignType(int pokedexNumber, int typeId, Connection connection) {
        TypeChartDAL typeChartDAL = new TypeChartDAL(connection);
        String speciesName = pokedexDAL.getSpeciesNameByPokedexNumber(pokedexNumber);
        String typeName = typeChartDAL.getTypeNameById(typeId);

        try {
            if (!pokedexDAL.pokedexEntryExists(pokedexNumber)) {
                return false;
            }

            int currentTypeCount = pokedexDAL.getTypeCountForPokedexNumber(pokedexNumber);
            if (currentTypeCount >= 2) {
                throw new IllegalArgumentException(speciesName + " already has 2 types. Cannot assign more.");
            }

            if (!typeChartDAL.typeChartEntryExists(typeId)) {
                System.err.println(typeName + " type does not exist.");
                return false;
            }

            boolean assigned = pokedexDAL.assignType(pokedexNumber, typeId);

            if (assigned) {
                System.out.println("\n" + typeName + " type assigned successfully to " + speciesName + ".");
            } else {
                System.err.println("Failed to assign " + typeName + " type to " + speciesName + ".");
            }
            return assigned;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                if (e.getMessage().contains("duplicate key") || e.getMessage().contains("PRIMARY")) {
                    throw new IllegalArgumentException("Cannot assign! " + speciesName + " already has the " + typeName + " type!");
                }
            } else if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
                System.err.println("Database permission error: " + e.getMessage());
                throw new RuntimeException("Permission denied. Current user does not have permission to assign types.");
            }
            System.err.println("Database error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error occurred during assignment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a type from a Pokédex entry in the database.
     *
     * @param pokedexNumber the number of the Pokédex entry from which the type will be removed
     * @param typeId the ID of the type to be removed
     * @param connection the current database connection, necessary for verifying type ID exists
     * @return true if the entry was successfully deleted, false otherwise
     */
    public boolean removeType(int pokedexNumber, int typeId, Connection connection) {
        TypeChartDAL typeChartDAL = new TypeChartDAL(connection);
        String speciesName = pokedexDAL.getSpeciesNameByPokedexNumber(pokedexNumber);
        String typeName = typeChartDAL.getTypeNameById(typeId);

        if (!pokedexDAL.pokedexEntryExists(pokedexNumber)) {
            throw new IllegalArgumentException("Pokémon with number " + pokedexNumber + " does not exist.");
        }

        boolean hasType = false;
        try {
            hasType = pokedexDAL.pokemonHasType(pokedexNumber, typeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int currentTypeCount = 0;
        try {
            currentTypeCount = pokedexDAL.getTypeCountForPokedexNumber(pokedexNumber);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (currentTypeCount == 0) {
            throw new IllegalArgumentException("No types assigned to Pokémon with number " + pokedexNumber + ". Cannot remove type.");
        }

        if (!hasType) {
            throw new IllegalArgumentException("Can't remove type. " + speciesName + " does not have " + typeName + " type.");
        }

        if (!typeChartDAL.typeChartEntryExists(typeId)) {
            throw new IllegalArgumentException("Type with ID " + typeId + " does not exist.");
        }

        boolean removed = false;

        try {
            removed = pokedexDAL.removeType(pokedexNumber, typeId);
        } catch (SQLException e) {
            if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
                System.err.println("Database permission error: " + e.getMessage());
                throw new RuntimeException("Permission denied. Current user does not have permission to remove types.");
            }
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
        return removed;
    }

    /**
     * Retrieves the Pokémon's species by the Pokédex number.
     * Needed for confirmation in entry deletion.
     *
     * @param pokedexNumber the Pokédex number of the entry
     * @return the species name, or null if no entry is found
     */
    public String getSpeciesNameByPokedexNumber(int pokedexNumber) {
        return pokedexDAL.getSpeciesNameByPokedexNumber(pokedexNumber);
    }

}
