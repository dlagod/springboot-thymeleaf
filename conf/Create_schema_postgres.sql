-- Database: gac_practica_3

-- DROP DATABASE gac_practica_3;

CREATE DATABASE gac_practica_3
    WITH 
    OWNER = gac
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Spain.1252'
    LC_CTYPE = 'Spanish_Spain.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
	
-- Role: gac
-- DROP ROLE gac;

CREATE ROLE gac WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  CREATEDB
  NOCREATEROLE
  NOREPLICATION;