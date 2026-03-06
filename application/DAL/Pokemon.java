package DAL;

/**
 * This class represents a Pokémon in the system.
 * It contains information about the Pokémon's details such as trainer, Pokédex number, level, species name, trainer title, and types.
 * Getter methods are provided to access the Pokémon's attributes.
 */
public class Pokemon {
    private int pokemonId;
    private int pokedexNumber;
    private int trainerId;
    private final int level;
    private final String speciesName;
    private final String trainerTitle;
    private final String types; // Holds concatenated types as a String

    /**
     * Constructor for creating a Pokémon object for insert operations (without the pokemonId and types).
     *
     * @param pokedexNumber the Pokémon's Pokédex Number
     * @param trainerId the Pokémon's trainer's ID
     * @param level the Pokémon's level
     */
    public Pokemon(int pokedexNumber, int trainerId, int level) {
        this.pokedexNumber = pokedexNumber;
        this.trainerId = trainerId;
        this.level = level;
        this.speciesName = null;
        this.trainerTitle = null;
        this.types = null;
    }

    /**
     * Constructor for creating a Pokémon object when retrieving data from the database (with the pokemonId).
     *
     * @param pokemonId the Pokémon's unique ID
     * @param pokedexNumber the Pokémon's Pokédex Number
     * @param trainerId the Pokémon's trainer's ID
     * @param level the Pokémon's level
     */
    public Pokemon(int pokemonId, int pokedexNumber, int trainerId, int level) {
        this.pokemonId = pokemonId;
        this.pokedexNumber = pokedexNumber;
        this.trainerId = trainerId;
        this.level = level;
        this.speciesName = null;
        this.trainerTitle = null;
        this.types = null;
    }

    /**
     * Constructor for creating a Pokémon object when retrieving data from the database using
     * the stored procedure (with the pokemonId).
     *
     * @param pokemonId the Pokémon's unique ID
     * @param speciesName the Pokémon's species name
     * @param types the Pokémon's types (concatenated as a string)
     * @param level the Pokémon's level
     * @param trainerTitle the trainer's title (class and name)

     */
    public Pokemon(int pokemonId, String types, String speciesName, int level, String trainerTitle) {
        this.pokemonId = pokemonId;
        this.speciesName = speciesName;
        this.types = types;
        this.level = level;
        this.trainerTitle = trainerTitle;
    }

    /**
     * Returns the Pokémon's ID.
     *
     * @return the Pokémon's ID
     */
    public int getPokemonId() {
        return pokemonId;
    }

    /**
     * Returns the Pokémon's Pokédex number.
     *
     * @return the Pokémon's Pokédex number
     */
    public int getPokedexNumber() {
        return pokedexNumber;
    }

    /**
     * Returns the Pokémon's trainer's ID.
     *
     * @return the Pokémon's trainer's ID
     */
    public int getTrainerId() {
        return trainerId;
    }

    /**
     * Returns the Pokémon's level.
     *
     * @return the Pokémon's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the Pokémon's species name.
     *
     * @return the Pokémon's species name
     */
    public String getSpeciesName() {
        return speciesName;
    }

    /**
     * Returns the Pokémon's trainer title (class and name).
     *
     * @return the Pokémon's trainer title
     */
    public String getTrainerTitle() {
        return trainerTitle;
    }

    /**
     * Returns the Pokémon's types (concatenated as a String).
     *
     * @return the Pokémon's types
     */
    public String getTypes() {
        return types;
    }
}
