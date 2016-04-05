CREATE TABLE Actors(Name varchar(40), Movie varchar(80), Year int, Role varchar(40));
LOAD DATA LOCAL INFILE '~/data/actors.csv' INTO TABLE Actors FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
SELECT Name From Actors WHERE Movie = 'Die Another Day';
DROP TABLE Actors;
