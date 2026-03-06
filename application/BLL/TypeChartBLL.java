package BLL;

import DAL.TypeChartDAL;
import DAL.TypeChartEntry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Business Logic Layer (BLL) class for managing Type Chart Entries.
 */
public class TypeChartBLL {
    private final TypeChartDAL typeChartDAL;

    /**
     * Constructor for TypeChartBLL.
     *
     * @param connection the database connection
     */
    public TypeChartBLL(Connection connection) {
        this.typeChartDAL = new TypeChartDAL(connection);
    }

    /**
     * Validates the type chart entry data before adding it to the database.
     *
     * @param typeName the name of the type
     */
    private void validateTypeData(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Type name cannot be empty.");
        }
    }

    /**
     * Adds a new type by validating and passing the details to the DAL layer.
     *
     * @param typeName the name of the type
     * @return true if type chart entry added, false otherwise
     */
    public boolean addType(String typeName) {
        try {
            validateTypeData(typeName);
            TypeChartEntry typeChartEntry = new TypeChartEntry(typeName);
            typeChartDAL.insertTypeChartEntry(typeChartEntry);
            System.out.println("\nType added successfully.");
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                System.err.println("Unique constraint violation: A type with the same name already exists.");
                throw new RuntimeException("A type with the same name already exists.");
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
     * Deletes a type after confirming the type exists.
     *
     * @param typeId the ID of the type to delete
     * @return true if the type was successfully deleted, false otherwise
     */
    public boolean deleteType(int typeId) {
        try {
            if (!typeChartDAL.typeChartEntryExists(typeId)) {
                System.err.println("Type with ID " + typeId + " does not exist.");
                return false;
            }
            boolean deleted = typeChartDAL.deleteType(typeId);
            if (deleted) {
                System.out.println("Type with ID " + typeId + " deleted successfully.");
            } else {
                System.err.println("Failed to delete type with ID " + typeId + ".");
            }
            return deleted;
        } catch (SQLException e) {
            if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
                System.err.println("Database permission error: " + e.getMessage());
                throw new RuntimeException("Permission denied. Current user does not have permission to delete this type.");
            }
            System.err.println("Database error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error occurred during deletion: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the list of all type chart entries.
     *
     * @return a list of all type chart entries
     */
    public List<TypeChartEntry> getAllTypeChartEntries() {
        return typeChartDAL.getAllTypeChartEntries();
    }

    /**
     * Retrieves the type's name by its ID.
     * Needed for confirmation in type deletion.
     *
     * @param typeId the ID of the type
     * @return the type's name, or null if no type is found
     */
    public String getTypeNameById(int typeId) {
        return typeChartDAL.getTypeNameById(typeId);
    }

}
