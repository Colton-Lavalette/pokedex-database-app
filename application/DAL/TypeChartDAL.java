package DAL;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Data Access Layer (DAL) class for performing CRUD operations on the Type Chart table.
 */
public class TypeChartDAL {
    private final Connection connection;

    /**
     * Constructor for TypeChartDAL.
     *
     * @param connection the database connection
     */
    public TypeChartDAL(Connection connection) {
        this.connection = connection;
    }

    /**
     * Checks if a type chart entry exists in the database based on the typeId.
     *
     * @param typeId the type ID to check
     * @return true if the trainer exists, false otherwise
     */
    public boolean typeChartEntryExists(int typeId) {
        String query = "SELECT COUNT(*) FROM TypeChart WHERE type_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, typeId);
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
     * Inserts a new type chart entry into the type chart table.
     *
     * @param typeChartEntry the pokedexEntry object containing Pokédex entry details
     * @return true if the Pokédex entry was inserted, false otherwise
     * @throws SQLException if a database error occurs, including unique constraint violations
     */
    public boolean insertTypeChartEntry(TypeChartEntry typeChartEntry) throws SQLException {
        String query = "INSERT INTO TypeChart (type_name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, typeChartEntry.getTypeName());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Deletes a type from the database if the type exists.
     *
     * @param typeId the ID of the type to delete
     * @return true if the type was successfully deleted, false otherwise
     * @throws SQLException if a database error occurs, including inadequate permissions
     */
    public boolean deleteType(int typeId) throws SQLException {
        if (!typeChartEntryExists(typeId)) {
            return false;
        }
        String query = "DELETE FROM TypeChart WHERE type_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, typeId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves all type chart entries from the TypeChart table.
     *
     * @return a list of type chart entry objects
     */
    public List<TypeChartEntry> getAllTypeChartEntries() {
        List<TypeChartEntry> typeChartEntries = new ArrayList<>();
        String query = "SELECT * FROM TypeChart ORDER BY type_id";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                int typeId = resultSet.getInt("type_id");
                String typeName = resultSet.getString("type_name");

                TypeChartEntry typeChartEntry = new TypeChartEntry(typeId, typeName);
                typeChartEntries.add(typeChartEntry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeChartEntries;
    }

    /**
     * Retrieves the type's name by its ID from the database.
     * Needed for confirmation in type deletion.
     *
     * @param typeId the ID of the type
     * @return the trainer's name, or null if no type is found
     */
    public String getTypeNameById(int typeId) {
        String query = "SELECT type_name FROM TypeChart WHERE type_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, typeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("type_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
