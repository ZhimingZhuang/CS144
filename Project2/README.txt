Answers to PartB:

1. List my relations:

ItemsInf(ItemID, Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Latitude, Longitude, Country, Started, Ends, SellerID, Description))
Primary key(ItemID)

ItemsCat(ItemID, Category);
Primary key(ItemID, Category)
 
SellersInf(SellerID, SellerRating);
Primary key(SellerID)

ItemsBid(ItemID, BidderID, BidTime, BidAmount);
Primary key(ItemID, BidderID, BidTime)

BiddersInf(BidderID, BidderRating, BidderLoc, BidderCountry);
Primary key(BidderID)

2. List all completely nontrivial functional dependencies:

In ItemsInf, we have ItemID->Name, ItemID->Currently, ItemID->Buy_Price, ItemID->First_Bid, ItemID->Number_of_Bids, ItemID->Location, ItemID->Latitude, ItemID->Longitude, ItemID->Country, ItemID->Started, ItemID->Ends, ItemID->SellerID, ItemID->Description. 

In ItemsCat, we don’t have any nontrivial functional dependencies.

In SellersInf, we have SellerID->SellerRating. 

In ItemsBid, we have ItemID, BidderID, BidTime->BidAmount.

In BiddersInf, we have BidderID->BidderRating, BidderID->BidderLoc, BidderID->BidderCountry. 

3. Yes, all of my relations are in Boyce-Codd Normal Form (BCNF).

4. Yes, since there are no multivalued dependency in my relations, all of my relations are in Fourth Normal Form (4NF).

Description of my project2:

I first fill code in myparser.java to transform XML data into MySQL load files without removing duplicates. Then I use sort and uniq to eliminate duplicates. After that, I create tables based on the schema I design, load data in it and run queries. 
My css layout is built according to the requirements. 