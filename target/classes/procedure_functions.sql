DELIMITER $$

DROP PROCEDURE IF EXISTS GetPokedexEntriesWithTypes $$

/* This procedure joins the Pokémon type information
and the region name information onto the Pokédex table
to allow this information to be displayed in the GUI. */
CREATE PROCEDURE GetPokedexEntriesWithTypes()
BEGIN
    SELECT P.pokedex_number, P.species_name, 
           GROUP_CONCAT(T.type_name ORDER BY T.type_name SEPARATOR ', ') AS pokemon_types,
           R.region_name
    FROM Pokedex P
    LEFT JOIN PokemonTypes PT ON P.pokedex_number = PT.pokedex_number
    LEFT JOIN TypeChart T ON PT.type_id = T.type_id
    LEFT JOIN Region R ON P.region_id = R.region_id
    GROUP BY P.pokedex_number, P.species_name, R.region_name
    ORDER BY P.pokedex_number;
END$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS GetPokemonWithTrainers $$

/* This procedure joins the trainer information
onto the Pokémon table to allow this information 
to be displayed in the GUI. */
CREATE PROCEDURE GetPokemonWithTrainers()
BEGIN
    SELECT 
        p.pokemon_id,
        pd.species_name,
        GROUP_CONCAT(TC.type_name ORDER BY TC.type_name SEPARATOR ', ') AS pokemon_types,
        p.level,
        CONCAT(t.trainer_class, ' ', t.name) AS trainer_title
    FROM 
        Pokemon p
    JOIN 
        Trainer t ON p.trainer_id = t.trainer_id
    JOIN 
        Pokedex pd ON p.pokedex_number = pd.pokedex_number
    LEFT JOIN 
        PokemonTypes PT ON p.pokedex_number = PT.pokedex_number
    LEFT JOIN 
        TypeChart TC ON PT.type_id = TC.type_id
    GROUP BY 
        p.pokemon_id, p.pokedex_number, pd.species_name, p.level, trainer_title
    ORDER BY 
        p.pokemon_id;
END $$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS UpdatePokemonLevel $$

CREATE PROCEDURE UpdatePokemonLevel(IN input_pokemon_id INT, IN new_level INT)
BEGIN
    IF EXISTS (SELECT 1 FROM Pokemon WHERE pokemon_id = input_pokemon_id) THEN
        UPDATE Pokemon
        SET level = new_level
        WHERE pokemon_id = input_pokemon_id;
    END IF;
END $$

DELIMITER ;

