
-- Create User
CREATE USER 'gac'@'localhost' IDENTIFIED BY 'password';
CREATE USER 'gac'@'%' IDENTIFIED BY 'password';

-- Create Database
CREATE DATABASE gac_practica_3 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

-- Establish permissions
GRANT ALL ON gac_practica_3.* TO gac@localhost;
GRANT ALL ON gac_practica_3.* TO gac@'%';