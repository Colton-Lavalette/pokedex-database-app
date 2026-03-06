package DAL;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Data Access Layer (DAL) class for performing CRUD operations on the Region table.
 */
public class RegionDAL {
    private final Connection connection;

    /**
     * Constructor for RegionDAL.
     *
     * @param connection the database connection
     */
    public RegionDAL(Connection connection) {
        this.connection = connection;
    }

    /**
     * Checks if a region exists in the database based on the regionId.
     *
     * @param regionId the region ID to check
     * @return true if the region exists, false otherwise
     */
    public boolean regionExists(int regionId) {
        String query = "SELECT COUNT(*) FROM Region WHERE region_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, regionId);
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
     * Inserts a new region into the Region table.
     *
     * @param region the region object containing region details
     * @return true if the region was inserted, false otherwise
     * @throws SQLException if a database error occurs, including unique constraint violations
     */
    public boolean insertRegion(Region region) throws SQLException {
        String query = "INSERT INTO Region (region_name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, region.getRegionName());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves all regions from the Region table.
     *
     * @return a list of Region objects
     */
    public List<Region> getAllRegions() {
        List<Region> regions = new ArrayList<>();
        String query = "SELECT * FROM Region ORDER BY region_id";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                int regionId = resultSet.getInt("region_id");
                String regionName = resultSet.getString("region_name");

                Region region = new Region(regionId, regionName);
                regions.add(region);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regions;
    }

    /**
     * Deletes a region from the database if the region exists.
     *
     * @param regionId the ID of the region to delete
     * @return true if the region was successfully deleted, false otherwise
     * @throws SQLException if a database error occurs, including inadequate permissions
     */
    public boolean deleteRegion(int regionId) throws SQLException {
        if (!regionExists(regionId)) {
            return false;
        }
        String query = "DELETE FROM Region WHERE region_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, regionId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves the region's name by its ID from the database.
     * Needed for confirmation in region deletion.
     *
     * @param regionId the ID of the region
     * @return the trainer's name, or null if no region is found
     */
    public String getRegionNameById(int regionId) {
        String query = "SELECT region_name FROM Region WHERE region_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, regionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("region_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
