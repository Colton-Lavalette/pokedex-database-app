package BLL;

import DAL.Trainer;
import DAL.TrainerDAL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Business Logic Layer (BLL) class for managing trainers.
 */
public class TrainerBLL {
    private final TrainerDAL trainerDAL;

    /**
     * Constructor for TrainerBLL.
     *
     * @param connection the database connection
     */
    public TrainerBLL(Connection connection) {
        this.trainerDAL = new TrainerDAL(connection);
    }

    /**
     * Validates the trainer data before adding it to the database.
     *
     * @param trainerName   the name of the trainer
     * @param trainerAge    the age of the trainer
     * @param hometown      the hometown of the trainer
     * @param trainerClass  the class of the trainer
     * @throws IllegalArgumentException if any validation fails
     */
    private void validateTrainerData(String trainerName, int trainerAge, String hometown, String trainerClass) {
        if (trainerName == null || trainerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Trainer name cannot be empty.");
        }
        if (trainerAge < 10) {
            throw new IllegalArgumentException("A Pokémon trainer must be at least 10 years old!");
        }
        if (hometown == null || hometown.trim().isEmpty()) {
            throw new IllegalArgumentException("Hometown cannot be empty.");
        }
        if (trainerClass == null || trainerClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Trainer class cannot be empty.");
        }
    }

    /**
     * Adds a new trainer by validating and passing the details to the DAL layer.
     *
     * @param trainerName   the name of the trainer
     * @param trainerAge    the age of the trainer
     * @param hometown      the hometown of the trainer
     * @param trainerClass  the class of the trainer
     * @return true if trainer added, false otherwise
     */
    public boolean addTrainer(String trainerName, int trainerAge, String hometown, String trainerClass) {
        try {
            validateTrainerData(trainerName, trainerAge, hometown, trainerClass);
            Trainer trainer = new Trainer(trainerName, trainerAge, hometown, trainerClass);
            trainerDAL.insertTrainer(trainer);
            System.out.println("\nTrainer added successfully.");
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                System.err.println("Unique constraint violation: A trainer with the same name and class already exists.");
                throw new RuntimeException("A trainer with the same name and class already exists.");
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
     * Deletes a trainer after confirming the trainer exists.
     *
     * @param trainerId the ID of the trainer to delete
     * @return true if the trainer was successfully deleted, false otherwise
     */
    public boolean deleteTrainer(int trainerId) {
        try {
            if (!trainerDAL.trainerExists(trainerId)) {
                System.err.println("Trainer with ID " + trainerId + " does not exist.");
                return false;
            }
            boolean deleted = trainerDAL.deleteTrainer(trainerId);
            if (deleted) {
                System.out.println("Trainer with ID " + trainerId + " deleted successfully.");
            } else {
                System.err.println("Failed to delete trainer with ID " + trainerId + ".");
            }
            return deleted;
        } catch (SQLException e) {
            if (e.getSQLState().equals("42000") || e.getMessage().contains("denied")) {
                System.err.println("Database permission error: " + e.getMessage());
                throw new RuntimeException("Permission denied. Current user does not have permission to delete this trainer.");
            }
            System.err.println("Database error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error occurred during deletion: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the list of all trainers.
     *
     * @return a list of all trainers
     */
    public List<Trainer> getAllTrainers() {
        return trainerDAL.getAllTrainers();
    }

    /**
     * Retrieves the trainer's name by their ID.
     * Needed for confirmation in trainer deletion.
     *
     * @param trainerId the ID of the trainer
     * @return the trainer's name, or null if no trainer is found
     */
    public String getTrainerNameById(int trainerId) {
        return trainerDAL.getTrainerNameById(trainerId);
    }

    /**
     * Retrieves the trainer's class by their ID.
     * Needed for confirmation in trainer deletion.
     *
     * @param trainerId the ID of the trainer
     * @return the trainer's class, or null if no trainer is found
     */
    public String getTrainerClassById(int trainerId) {
        return trainerDAL.getTrainerClassById(trainerId);
    }
}
