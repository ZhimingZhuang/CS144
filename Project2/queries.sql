SELECT COUNT(*) FROM (SELECT BidderID FROM BiddersInf UNION SELECT SellerID FROM SellersInf) New;

SELECT COUNT(*) FROM ItemsInf WHERE BINARY Location = "New York";

SELECT COUNT(*) FROM (SELECT ItemID FROM ItemsCat GROUP BY ItemID HAVING COUNT(*) = 4) Cat;

SELECT ItemID FROM (SELECT ItemID, MAX(Currently) FROM ItemsInf WHERE Ends > '2001-12-20 12:00:01') HighestBid;

SELECT COUNT(*) FROM (SELECT SellerID FROM SellersInf WHERE SellerRating> 1000) GoodSeller;

SELECT COUNT(*) FROM (SELECT SellerID FROM SellersInf, BiddersInf WHERE SellerID = BidderID) SellerBidder;

SELECT COUNT(*) FROM (SELECT DISTINCT Category FROM ItemsCat, ItemsBid WHERE ItemsCat.ItemID = ItemsBid.ItemID AND BidAmount > 100) Cat;