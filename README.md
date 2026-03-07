# Pokédex Database Application

A full-stack Java desktop application that manages a Pokémon database through a graphical user interface. Built as a final project for a graduate Database Principles course, demonstrating 3-tier architecture, relational database design, role-based access control, and cross-platform deployment.

## Project Overview

This Java application connects to a MySQL database and demonstrates the use of stored procedures and functions, along with a graphical user interface (GUI). It is designed with a multi-layered architecture, including the following components:

- **DAL (Data Access Layer):** Handles all database operations, including establishing and disconnecting from the database.
- **BLL (Business Logic Layer):** Contains the business logic for interacting with the database, including operations such as managing Pokémon, trainers, and their attributes.
- **View (GUI):** The user interface allows users to interact with the application, display data, and run queries without having to interact with the code themselves.

Pre-generated Javadoc documentation is available in the `docs/` folder.

## Features

- Connects to a MySQL database and handles queries and stored procedures for managing Pokémon, trainers, and related entities
- Full CRUD operations — add, view, update, and delete Pokémon, trainers, types, regions, and Pokédex entries
- Role-based access control with 5 user roles and permission-differentiated UI
- Stored procedures used for read operations (Pokédex and Pokémon tables use JOINs across multiple tables) and for updating Pokémon levels; other modifications are handled directly via JDBC
- Type assignment logic with error handling for invalid operations (e.g. attempting to assign a third type to a Pokédex entry)
- Cascade deletes — parent record deletions propagate correctly to child records
- Robust input validation — SQLState error codes are caught and translated into specific user-facing messages rather than raw exceptions
- Modular design with strict separation between DAL and BLL

## GUI Interactions

### Login Screen

When you start the application, you will be prompted to log in. Five user roles are available with the following credentials:

| Username | Password | Role |
|----------|----------|------|
| `read_only` | `read1234` | Read-only |
| `modify_user` | `modify1234` | Modify |
| `professor_oak` | `kantoprof1` | Professor (modify) |
| `professor_elm` | `johtoprof1` | Professor (modify) |
| `professor_birch` | `hoennprof1` | Professor (modify) |

Passwords are hidden by default; use the Show button to verify what you've entered.

### Main Dashboard

After logging in, you'll be taken to the main dashboard where you can:

- View all Pokémon in the database (all users)
- View detailed Pokémon information, including species, level, and associated trainer
- Update Pokémon levels (modify users and Professor roles)
- Add Pokémon, types, regions, trainers, or Pokédex entries (modify users only)
- Delete trainers, Pokédex entries, types, and regions (modify users only)
- Assign or remove types from Pokédex entries, with error handling for adding too many types or removing types that don't exist

### Permissions-based Access

| Role | Permissions |
|------|-------------|
| Read-only | View data only |
| Modify | View and modify all data |
| Professor | View and modify all data |

## Known Limitations

Permission checks are enforced at the SQL layer rather than the application layer. This means input validation (IllegalArgumentException) runs before role verification (SQLException) — so a read-only user will have their input validated before being told their permissions were denied. Unauthorized changes are still correctly prevented in all cases. This is a known architectural tradeoff.

## Getting Started

### Prerequisites

- Java 12 or newer (project uses switch expressions)
- MySQL database

### Setup

1. Run the SQL scripts in the following order:
```
   users.sql → ddl.sql → dml.sql → procedure_functions.sql → permissions.sql
```

   This will create the users, set up the database schema, insert default values, create stored procedures and functions, and grant the correct permissions to each role.

2. Navigate to the project root directory in your terminal.

3. Run the application:
```bash
   java -jar target/lavalette-colton-final-1.0-SNAPSHOT.jar
```

### Troubleshooting

If the JAR fails to run, rebuild with Maven first:
```bash
mvn clean install
java -jar target/lavalette-colton-final-1.0-SNAPSHOT.jar
```
