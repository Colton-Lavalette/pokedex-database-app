-- Drop the users if they already exist
DROP USER IF EXISTS 'admin_user'@'%';
DROP USER IF EXISTS 'read_only'@'%';
DROP USER IF EXISTS 'modify_user'@'%';
DROP USER IF EXISTS 'professor_oak'@'%';
DROP USER IF EXISTS 'professor_elm'@'%';
DROP USER IF EXISTS 'professor_birch'@'%';

-- Create new users
CREATE USER 'admin_user'@'%' IDENTIFIED BY 'admin1234';
CREATE USER 'read_only'@'%' IDENTIFIED BY 'read1234';
CREATE USER 'modify_user'@'%' IDENTIFIED BY 'modify1234';

-- Add a custom user for Professor Oak
CREATE USER 'professor_oak'@'%' IDENTIFIED BY 'kantoprof1';

-- Add a custom user for Professor Elm
CREATE USER 'professor_elm'@'%' IDENTIFIED BY 'johtoprof1';

-- Add a custom user for Professor Birch
CREATE USER 'professor_birch'@'%' IDENTIFIED BY 'hoennprof1';