package BLL;

import DAL.Region;
import DAL.RegionDAL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Business Logic Layer (BLL) class for managing regions.
 */
public class RegionBLL {
    private final RegionDAL regionDAL;

    /**
     * Constructor for RegionBLL.
     *
     * @param connection the database connection
     */
    public RegionBLL(Connection connection) {
        this.regionDAL = new RegionDAL(connection);
    }

    /**
     * Validates the region data before adding it to the database.
     *
     * @param regionName the name of the region
     */
    private void validateRegionData(String regionName) {
        if (regionName == null || regionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Region name cannot be empty.");
        }
    }

    /**
     * Adds a new region by validating and passing the details to the DAL layer.
     *
     * @param regionName the name of the region
     * @return true if region added, false otherwise
     */
    public boolean addRegion(String regionName) {
        try {
            validateRegionData(regionName);
            Region region = new Region(regionName);
            regionDAL.insertRegion(region);
            System.out.println("\nRegion added successfully.");
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                System.err.println("Unique constraint violation: A region with the same name already exists.");
                throw new RuntimeException("A region with the same name already exists.");
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
     * Deletes a region after confirming the region exists.
     *
     * @param regionId the ID of the region to delete
     * @return true if the region was successfully deleted, false otherwise
     */
    public boolean deleteRegion(int regionId) {
        try {
            if (!regionDAL.regionExists(regionId)) {
                System.err.println("Region with ID " + regionId + " does not exist.");
                return false;
            }
            boolean deleted = regionDAL.deleteRegion(regionId);
            if (deleted) {
                System.out.println("Region with ID " + regionId + " deleted successfully.");
            } else {
                System.err.println("Failed to delete region with ID " + regionId + ".");
            }
            return deleted;
        } catch (SQLException e) {
            if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
                System.err.println("Database permission error: " + e.getMessage());
                throw new RuntimeException("Permission denied. Current user does not have permission to delete this region.");
            }
            System.err.println("Database error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error occurred during deletion: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the list of all regions.
     *
     * @return a list of all regions
     */
    public List<Region> getAllRegions() {
        return regionDAL.getAllRegions();
    }

    /**
     * Retrieves the region's name by its ID.
     * Needed for confirmation in region deletion.
     *
     * @param regionId the ID of the region
     * @return the region's name, or null if no region is found
     */
    public String getRegionNameById(int regionId) {
        return regionDAL.getRegionNameById(regionId);
    }
}
