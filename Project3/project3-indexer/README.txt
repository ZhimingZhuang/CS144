In Indexer.java, I create indexes of ItemID, Name and a combination of Name, Category and Description. 

The index files are stored in /var/lib/lucene/.

The buildSQLIndex.sql create spatial indexes of Latitude and Longitude. This file should be executed before using spatialSearch function.