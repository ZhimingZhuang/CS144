#!/bin/bash
chmod u+x runLoad.sh

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files

ant -f build.xml run-all 


# If the Java code does not handle duplicate removal, do this now
sort SellersInf.txt > Sort_SellersInf.txt
uniq Sort_SellersInf.txt > Uniq_SellersInf.txt
sort BiddersInf.txt > Sort_BiddersInf.txt
uniq Sort_BiddersInf.txt > Uniq_BiddersInf.txt


# Run the load.sql batch file to load the data
mysql CS144 < load.sql

# Remove all temporary files
rm SellersInf.txt
rm Sort_SellersInf.txt
rm BiddersInf.txt
rm Sort_BiddersInf.txt
rm Uniq_SellersInf.txt
rm Uniq_BiddersInf.txt
rm ItemsBid.txt
rm ItemsCat.txt
rm ItemsInf.txt
