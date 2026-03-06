package DAL;

/**
 * This class represents an entry in the type chart.
 * It contains the ID and name for the type chart entry.
 * Getter methods are provided to access the entry's attributes.
 */
public class TypeChartEntry {
    private int typeId;
    private final String typeName;

    /**
     * Constructor for creating a type chart entry object.
     *
     * @param typeName the unique name of the type
     */
    public TypeChartEntry(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Constructor for creating a Type Chart Entry object when retrieving data from the database (with the typeId).
     *
     * @param typeId the type chart entry's unique ID
     * @param typeName the type name
     */
    public TypeChartEntry(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    /**
     * Returns the type chart entry's ID
     *
     * @return the type chart entry's ID
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * Returns the type's name.
     *
     * @return the type's name
     */
    public String getTypeName() {
        return typeName;
    }
}
