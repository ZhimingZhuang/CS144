
CREATE TABLE SellersInf(
	SellerID varchar(50) NOT NULL,
	SellerRating int NOT NULL,

	PRIMARY KEY(SellerID)
)ENGINE=INNODB;


CREATE TABLE ItemsInf(
	ItemID varchar(20) NOT NULL,
	Name varchar(50) NOT NULL,
	Currently DECIMAL(8,2) NOT NULL,
	Buy_Price DECIMAL(8,2),
	First_Bid DECIMAL(8,2) NOT NULL,
	Number_of_Bids int NOT NULL,
	Location varchar(100) NOT NULL,
	Latitude varchar(20),
	Longitude varchar(20),
	Country varchar(50) NOT NULL,
	Started timestamp NOT NULL,
	Ends timestamp NOT NULL,
	SellerID varchar(50) NOT NULL,
	Description varchar(4000) NOT NULL, 

	PRIMARY KEY(ItemID),

	FOREIGN KEY(SellerID) REFERENCES SellersInf(SellerID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
)ENGINE=INNODB;


CREATE TABLE ItemsCat(
	ItemID varchar(20) NOT NULL,
	Category varchar(50) NOT NULL,
	PRIMARY KEY(ItemID, Category),

	FOREIGN KEY (ItemID) REFERENCES ItemsInf(ItemID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
)ENGINE=INNODB;

CREATE TABLE BiddersInf(
	BidderID varchar(50) NOT NULL,
	BidderRating int NOT NULL,
	BidderLoc varchar(100),
	BidderCountry varchar(50),

	PRIMARY KEY(BidderID)
)ENGINE=INNODB;

CREATE TABLE ItemsBid(
	ItemID varchar(20) NOT NULL,
	BidderID varchar(50) NOT NULL,
	BidTime timestamp NOT NULL,
	BidAmount DECIMAL(8,2) NOT NULL,

	PRIMARY KEY(ItemID, BidderID, BidTime),

	FOREIGN KEY(ItemID) REFERENCES ItemsInf(ItemID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY(BidderID) REFERENCES BiddersInf(BidderID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
)ENGINE=INNODB;

