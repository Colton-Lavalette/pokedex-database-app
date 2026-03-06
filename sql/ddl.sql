-- 'Start fresh' by dropping POKEMON if it exists and setting is as the current database
DROP DATABASE IF EXISTS POKEMON;
CREATE DATABASE IF NOT EXISTS POKEMON;
USE POKEMON;

-- Ensure tables do not exist
DROP TABLE IF EXISTS Trainer;
DROP TABLE IF EXISTS Pokedex;
DROP TABLE IF EXISTS Pokemon;
DROP TABLE IF EXISTS TypeChart;
DROP TABLE IF EXISTS PokemonTypes;
DROP TABLE IF EXISTS Region;
DROP TABLE IF EXISTS PokemonRegions;

-- Create all tables

CREATE TABLE Trainer ( 
	/*
    This table contains the trainer information
    */
	trainer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    age INT,
    hometown VARCHAR(100),
    trainer_class VARCHAR(100),
    UNIQUE (name, trainer_class)
);

CREATE TABLE Region (
	/*
    This table contains the Pokemon Regions.
    */
    region_id INT AUTO_INCREMENT PRIMARY KEY,
    region_name VARCHAR(50) UNIQUE
);

CREATE TABLE Pokedex(
	/*
    This table contains the Pokedex.
    */
    pokedex_number INT PRIMARY KEY,
    species_name VARCHAR(50) UNIQUE,
    region_id INT,  
    FOREIGN KEY (region_id) REFERENCES Region(region_id) ON DELETE CASCADE
);

CREATE TABLE TypeChart (
	/*
    This table contains all the possible Pokemon types in the type chart
    */
	type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE
);

CREATE TABLE PokemonTypes (
	/*
    This table links the Pokedex to the TypeChart
    */
    pokedex_number INT,
    type_id INT,
    PRIMARY KEY (pokedex_number, type_id),
    FOREIGN KEY (pokedex_number) REFERENCES Pokedex(pokedex_number) ON DELETE CASCADE,
    FOREIGN KEY (type_id) REFERENCES TypeChart(type_id) ON DELETE CASCADE
);

CREATE TABLE Pokemon (
	/*
    This table contains the actual Pokemon owned by trainers
    */
    pokemon_id INT AUTO_INCREMENT PRIMARY KEY,  
    pokedex_number INT,                         
    trainer_id INT,                           
    level INT,                             
    FOREIGN KEY (trainer_id) REFERENCES Trainer(trainer_id) ON DELETE CASCADE,
    FOREIGN KEY (pokedex_number) REFERENCES Pokedex(pokedex_number) ON DELETE CASCADE
);




