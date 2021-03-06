DROP TABLE IF EXISTS ItemsGeo;

CREATE TABLE ItemsGeo(
	ItemID varchar(20) PRIMARY KEY,
	Geo POINT NOT NULL,
	SPATIAL INDEX(Geo)
)ENGINE=MyISAM;


INSERT INTO ItemsGeo(ItemID, Geo)
SELECT ItemID, POINT(Latitude, Longitude) 
FROM ItemsInf
WHERE Latitude IS NOT NULL AND Longitude IS NOT NULL;
