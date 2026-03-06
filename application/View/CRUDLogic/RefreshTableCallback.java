package View.CRUDLogic;

/**
 * Callback interface to refresh the corresponding table after adding data to the database.
 */
public interface RefreshTableCallback {
    /**
     * Refreshes the relevant table, allowing the updated versoin to be displayed to the user.
     */
    void refreshTable();
}
