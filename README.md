# Lavalette-Colton-Final

## Project Overview

This Java application connects to a MySQL database and demonstrates the use of stored procedures and functions, along with a graphical user interface (GUI). It is designed with a multi-layered architecture, including the following components:

- **DAL (Data Access Layer)**: Handles all database operations, including establishing and disconnecting from the database.
- **BLL (Business Logic Layer)**: Contains the business logic for interacting with the database, including operations such as managing Pokémon, trainers, and their attributes.
- **Main**: The entry point of the application that initializes the database connection, runs test operations, and demonstrates functionality by invoking methods from the BLL.
- **GUI (Graphical User Interface)**: The user interface allows users to interact with the application, display data, and run queries without having to interact with the code themselves.

It also contains pre-generated Javadoc documentation in the `application` folder.

## Features

- Establishes a connection to a MySQL database.
- Handles database queries and stored procedures for managing Pokémon, trainers, and related entities.
- Provides a GUI for easier interaction with the data.
- Modular design separating database access (DAL) and business logic (BLL).
- Javadoc documentation is available in the `application` folder.

Note: The stored procedures for reading from the database are called by showing Pokédex and Pokémon tables, since these tables make use of joins to get information from other tables. The modification procedure is used for updating Pokémon levels. The other modifications (insertions and deletions) are done without using custom SQL procedures.

The credentials are the same as for our SCHOOL database, with the exception that I also added three Pokémon Professor users with modify roles for fun. The usernames are professor_oak, professor_elm and professor_birch. The passwords are kantoprof1, johtoprof1 and hoennprof1, respectively. The read_only user is still read_only with password1234, and you can still sign in as the original modify_user with password modify1234 if this is more convenient for testing.


## GUI Interactions

The application provides a graphical user interface (GUI) that allows users to interact with the database in a more user-friendly way. Here's what you can expect from the GUI:

1. **Login Screen**  
   When you start the application, you will be prompted to log in. As mentioned above, you can use the original credentials or a Professor role with modify permissions:
   - **Read-only user**: `read_only` / `read1234`
   - **Modify user**: `modify_user` / `modify1234`
   - **Professor roles**
     - **Professor Oak**: `professor_oak` / `kantoprof1`
     - **Professor Elm**: `professor_elm` / `johtoprof1`
     - **Professor Birch**: `professor_birch` / `hoennprof1`
  - Passwords are hidden by default, but the `Show` button allows you to verify what you've entered.

2. **Main Dashboard**  
   After logging in, you'll be taken to the main dashboard where you can:
   - View all Pokémon in the database (all users)
   - View detailed Pokémon information, including their species, level, and the trainer associated with them.
   - Update Pokémon levels (for modify users).
   - Add New Pokémon, types, regions, trainers or Pokédex entries (modify users only).
   - Delete entries for trainers, Pokédex entries, types and regions (modify users only).
   - Assign or remove specific types to a given Pokédex entry, with error handling for adding too many types or removing types from entries where they don't exist. 

Note: Deletions for most relationships cascade, ensuring that child records are removed when the parent is deleted.
Note: I think the error handling is pretty robust, so it can catch basic things like invalid parameter types, as well as more complex validation such as checking whether a Pokémon already has 2 types before attempting to assign another. This will give the user more information about why their request failed. Within the scope and timeline of this assignment, I was not able to make the permission checks happen before data validation. It will make sure all fields are valid before telling the read-only user that modify permissions were denied. It will still prevent them from making unauthorized changes, this logic (SQLException) just happens after validation (IllegalArgumentException). I think there is a way to make a permission check first, it was just more complicated than what I could accomplish within the time window of the course. I think my SCHOOL database also had this issue, so hopefully that is okay here.

3. **Interacting with Data**  
   - **View Pokémon**: You can click on the list of Pokémon to view detailed information about them.
   - **Update Pokémon Level**: If you have the appropriate permissions (e.g., as a Professor or modify user), you can update a Pokémon’s level directly from the GUI.

4. **Permissions-based Access**  
   The functionality available to you depends on the role you log in with:
   - **Read-only users** can view data but cannot modify it.
   - **Modify users** can view and update Pokémon levels.
   - **Professor roles** (with modify privileges) can view and update Pokémon levels as well.

This interactive GUI allows users to easily visualize and modify the data in the Pokémon database without modifying the SQL files directly.

## Getting Started

### Prerequisites

- **Java 12 or newer**: This project uses Java 12 features such as "switch expressions" and requires Java 12 or later installed.
- **MySQL Database**: A MySQL database is required, and the necessary tables and stored procedures should be set up (provided in the `sql` folder).

### Running the Program

1. **Set up the Database**  
   Use MySQL (either through the command line or Docker) and run the following SQL scripts in this order: `users.sql`, `ddl.sql`, `dml.sql`, `procedure_functions.sql`, `permissions.sql`. This will create the users, create the database, insert default values, set up the necessary stored functions and procedures, and grant the proper permissions to each user. 

2. **Navigate to the project directory**  
   In the command line, navigate to the root directory of the project (e.g., `lavalette-colton-final`).

3. **Run the program**  
   Once inside the project directory, run the program using the following command:

     ```bash
     java -jar target/lavalette-colton-final-1.0-SNAPSHOT.jar
     ```

This command will execute the program from the `target` directory, where the compiled `.jar` file is located.

## Troubleshooting

### Unable to run the program?

If you are unable to run the program after extracting the zip file, follow these steps:

1. **Clean install**  
   If you are unable to run the program, try rebuilding it with the following command:

   ```bash
   mvn clean install
   ```

2. **Run the program After rebuilding, run the program with the following command:**
    ```bash
    java -jar target/lavalette-colton-final-1.0-SNAPSHOT.jar
    ```