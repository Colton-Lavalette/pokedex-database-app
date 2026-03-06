package DAL;

/**
 * This class represents a Pokémon Trainer in the system.
 * It contains information about the trainer's details such as age, class and hometown.
 * Getter methods are provided to access the trainer's attributes.
 */
public class Trainer {
    private int trainerId;
    private final String trainerName;
    private final int trainerAge;
    private final String hometown;
    private final String trainerClass;

    /**
     * Constructor for creating a Trainer object for insert operations (without the trainerId).
     *
     * @param trainerName the trainer's name
     * @param trainerAge the trainer's age
     * @param hometown the trainer's hometown
     * @param trainerClass the trainer's class
     */
    public Trainer(String trainerName, int trainerAge, String hometown, String trainerClass) {
        this.trainerName = trainerName;
        this.trainerAge = trainerAge;
        this.hometown = hometown;
        this.trainerClass = trainerClass;
    }
    /**
     * Constructor for creating a Trainer object when retrieving data from the database (with the trainerId).
     *
     * @param trainerId the trainer's unique ID
     * @param trainerName the trainer's name
     * @param trainerAge the trainer's age
     * @param hometown the trainer's hometown
     * @param trainerClass the trainer's class
     */
    public Trainer(int trainerId, String trainerName, int trainerAge, String hometown, String trainerClass) {
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.trainerAge = trainerAge;
        this.hometown = hometown;
        this.trainerClass = trainerClass;
    }

    /**
     * Returns the trainer's ID.
     *
     * @return the trainer's ID
     */
    public int getTrainerId() {
        return trainerId;
    }

    /**
     * Returns the trainer's name.
     *
     * @return the trainer's name
     */
    public String getTrainerName() {
        return trainerName;
    }

    /**
     * Returns the trainer's age.
     *
     * @return the trainer's age
     */
    public int getTrainerAge() {
        return trainerAge;
    }

    /**
     * Returns the trainer's hometown.
     *
     * @return the trainer's town
     */
    public String getHometown() {
        return hometown;
    }

    /**
     * Returns the trainer's class.
     *
     * @return the trainer's class
     */
    public String getTrainerClass() {
        return trainerClass;
    }
}
