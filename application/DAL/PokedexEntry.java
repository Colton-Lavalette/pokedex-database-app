package DAL;

/**
 * This class represents an entry in the Pokédex.
 * It contains the Pokédex number and species name for the Pokédex entry.
 * Getter methods are provided to access the entry's attributes.
 */
public class PokedexEntry {
    private final int pokedexNumber;
    private int regionId;
    private String regionName;
    private final String speciesName;
    private String pokemonTypes;

    /**
     * Constructor for creating a Pokédex object for insertion.
     *
     * @param pokedexNumber the Pokédex entry's unique ID
     * @param speciesName the name of the Pokémon species
     */
    public PokedexEntry(int pokedexNumber, String speciesName, int regionId) {
        this.pokedexNumber = pokedexNumber;
        this.speciesName = speciesName;
        this.regionId = regionId;
    }

    /**
     * Constructor for creating a Pokédex object for display.
     *
     * @param pokedexNumber the Pokédex entry's unique ID
     * @param speciesName the name of the Pokémon species
     * @param pokemonTypes the concatenated string of Pokémon types
     * @param regionName the name of the Pokémon's region
     */
    public PokedexEntry(int pokedexNumber, String speciesName, String pokemonTypes, String regionName) {
        this.pokedexNumber = pokedexNumber;
        this.speciesName = speciesName;
        this.pokemonTypes = pokemonTypes;
        this.regionName = regionName;
    }

    /**
     * Returns the Pokédex entry's number
     *
     * @return the Pokédex entry's number
     */
    public int getPokedexNumber() {
        return pokedexNumber;
    }

    /**
     * Returns the Pokédex entry's region ID
     *
     * @return the Pokédex entry's region ID
     */
    public int getRegionId() {
        return regionId;
    }

    /**
     * Returns the Pokédex entry's region name
     *
     * @return the Pokédex entry's region name
     */
    public String getRegionName() {
        return regionName;
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
     * Returns the Pokémon's types.
     *
     * @return the Pokémon's types
     */
    public String getPokemonTypes() {
        return pokemonTypes;
    }
}
