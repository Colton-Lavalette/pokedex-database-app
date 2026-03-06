-- Drop the roles if they already exist
DROP ROLE IF EXISTS 'admin_role';
DROP ROLE IF EXISTS 'read_role';
DROP ROLE IF EXISTS 'modify_role';

CREATE ROLE 'admin_role';
CREATE ROLE 'read_role';
CREATE ROLE 'modify_role';

GRANT ALL PRIVILEGES ON *.* TO admin_role WITH GRANT OPTION;
GRANT SELECT ON *.* TO read_role;
GRANT CREATE, SELECT, INSERT, UPDATE, DELETE, EXECUTE ON *.* TO modify_role;

GRANT 'admin_role' TO 'admin_user'@'%';
GRANT 'read_role' TO 'read_only'@'%';
GRANT 'modify_role' TO 'modify_user'@'%';

SET DEFAULT ROLE 'admin_role' TO 'admin_user'@'%';
SET DEFAULT ROLE 'read_role' TO 'read_only'@'%';
SET DEFAULT ROLE 'modify_role' TO 'modify_user'@'%';  

-- Grant EXECUTE privilege on the specific stored procedures to the read_role
GRANT EXECUTE ON PROCEDURE POKEMON.GetPokedexEntriesWithTypes TO read_role;
GRANT EXECUTE ON PROCEDURE POKEMON.GetPokemonWithTrainers TO read_role;

-- Grant and set defaults for Professor Oak
GRANT 'modify_role' TO 'professor_oak'@'%';
SET DEFAULT ROLE 'modify_role' TO 'professor_oak'@'%';

-- Grant and set defaults for Professor Elm
GRANT 'modify_role' TO 'professor_elm'@'%';
SET DEFAULT ROLE 'modify_role' TO 'professor_elm'@'%';

-- Grant and set defaults for Professor Birch
GRANT 'modify_role' TO 'professor_birch'@'%';
SET DEFAULT ROLE 'modify_role' TO 'professor_birch'@'%';