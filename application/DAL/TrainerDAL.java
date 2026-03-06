package DAL;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Data Access Layer (DAL) class for performing CRUD operations on the Trainer table.
 */
public class TrainerDAL {
    private final Connection connection;

    /**
     * Constructor for RegionDAL.
     *
     * @param connection the database connection
     */
    public TrainerDAL(Connection connection) {
        this.connection = connection;
    }

    /**
     * Checks if a trainer exists in the database based on the trainerId.
     *
     * @param trainerId the trainer ID to check
     * @return true if the trainer exists, false otherwise
     */
    public boolean trainerExists(int trainerId) {
        String query = "SELECT COUNT(*) FROM Trainer WHERE trainer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trainerId);
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
     * Inserts a new trainer into the Trainer table.
     *
     * @param trainer the trainer object containing trainer details
     * @return true if the trainer was inserted, false otherwise
     * @throws SQLException if a database error occurs, including unique constraint violations
     */
    public boolean insertTrainer(Trainer trainer) throws SQLException {
        String query = "INSERT INTO Trainer (name, age, hometown, trainer_class) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, trainer.getTrainerName());
            preparedStatement.setInt(2, trainer.getTrainerAge());
            preparedStatement.setString(3, trainer.getHometown());
            preparedStatement.setString(4, trainer.getTrainerClass());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves all trainers from the Trainer table.
     *
     * @return a list of Trainer objects
     */
    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = new ArrayList<>();
        String query = "SELECT * FROM Trainer ORDER BY trainer_id";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                int trainerId = resultSet.getInt("trainer_id");
                String trainerName = resultSet.getString("name");
                int trainerAge = resultSet.getInt("age");
                String hometown = resultSet.getString("hometown");
                String trainerClass = resultSet.getString("trainer_class");

                Trainer trainer = new Trainer(trainerId, trainerName, trainerAge, hometown, trainerClass);
                trainers.add(trainer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainers;
    }

    /**
     * Deletes a trainer from the database if the trainer exists.
     *
     * @param trainerId the ID of the trainer to delete
     * @return true if the trainer was successfully deleted, false otherwise
     * @throws SQLException if a database error occurs, including inadequate permissions
     */
    public boolean deleteTrainer(int trainerId) throws SQLException {
        if (!trainerExists(trainerId)) {
            return false;
        }
        String query = "DELETE FROM Trainer WHERE trainer_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, trainerId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves the trainer's name by their ID from the database.
     * Needed for confirmation in trainer deletion.
     *
     * @param trainerId the ID of the trainer
     * @return the trainer's name, or null if no trainer is found
     */
    public String getTrainerNameById(int trainerId) {
        String query = "SELECT name FROM Trainer WHERE trainer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trainerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the trainer's class by their ID from the database.
     * Needed for confirmation in trainer deletion.
     *
     * @param trainerId the ID of the trainer
     * @return the trainer's class, or null if no trainer is found
     */
    public String getTrainerClassById(int trainerId) {
        String query = "SELECT trainer_class FROM Trainer WHERE trainer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trainerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("trainer_class");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}