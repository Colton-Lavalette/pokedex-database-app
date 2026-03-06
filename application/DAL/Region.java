package DAL;

/**
 * This class represents a region in the system.
 * It contains information about the region's details such as region ID and region name.
 * Getter methods are provided to access the region's attributes.
 */
public class Region {
    private int regionId;
    private final String regionName;

    /**
     * Constructor for creating a Region object for insert operations (without the regionId).
     *
     * @param regionName the region's name
     */
    public Region(String regionName) {
        this.regionName = regionName;
    }
    /**
     * Constructor for creating a Region object when retrieving data from the database (with the regionId).
     *
     * @param regionId the region's unique ID
     * @param regionName the region's name
     */
    public Region(int regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }

    /**
     * Returns the region's ID.
     *
     * @return the region's ID
     */
    public int getRegionId() {
        return regionId;
    }

    /**
     * Returns the region's name.
     *
     * @return the region's name
     */
    public String getRegionName() {
        return regionName;
    }
}
