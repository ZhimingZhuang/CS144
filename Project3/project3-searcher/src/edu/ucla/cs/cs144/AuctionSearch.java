package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.StringWriter;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;


public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		SearchResult[] results = new SearchResult[0];
		if(numResultsToSkip < 0 || numResultsToReturn < 0 || numResultsToSkip + numResultsToReturn <= 0)
				return results;

		try{
			
			//ArrayList<SearchResult> arrayList = new ArrayList<SearchResult>();
		    IndexSearcher searcher = null;
		    QueryParser parser = null;
		    
		    searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/"))));
		    parser = new QueryParser("Content", new StandardAnalyzer());
		    
		    Query q = parser.parse(query);
		    TopDocs topDocs = searcher.search(q, numResultsToSkip + numResultsToReturn);
		    
		    ScoreDoc[] hits = topDocs.scoreDocs;
		    results = new SearchResult[Math.max(hits.length - numResultsToSkip, 0)];
		    for(int i = numResultsToSkip; i < hits.length; i++){
		    	Document doc = searcher.doc(hits[i].doc);
		    	SearchResult tmp = new SearchResult();
		    	tmp.setItemId(doc.get("ItemID"));
		    	tmp.setName(doc.get("Name"));
		    	results[i - numResultsToSkip] = tmp;
		    }

	    } catch (ParseException pe) {
	    	System.out.println(pe);
	    } catch (IOException ex) {
	    	System.out.println(ex);
	    }

		return results;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		double lx = region.getLx();
		double ly = region.getLy();
		double rx = region.getRx();
		double ry = region.getRy();
		// Record the ItemID that are in the region
		HashSet<String> RecordGeo = new HashSet<String>();

	    Connection conn = null;

	    // create a connection to the database to retrieve Items from MySQL
	    try {
	        conn = DbManager.getConnection(true);
	    } catch (SQLException ex) {
	        System.out.println(ex);
	    }
	    
	    // Spatial Search
	    try {
	    	
		    Statement s = conn.createStatement(); 
		    ResultSet rs = s.executeQuery("SELECT ItemID FROM ItemsGeo WHERE MBRCONTAINS(GEOMFROMTEXT('POLYGON(("+lx+" "+ly+", "+lx+" "+ry+", "+rx+" "+ry+", "+rx+" "+ly+", "+lx+" "+ly+"))'), Geo)");
		    
		    while(rs.next()){
		    	RecordGeo.add(rs.getString("ItemID"));
		    }
		    s.close();
		    rs.close();
		    
	    } catch(SQLException ex) {
	    	System.out.println(ex);
	    }

	    // Basic Search
	    ArrayList<SearchResult> arrayList = new ArrayList<SearchResult>();
	    try {

		    IndexSearcher searcher = null;
		    QueryParser parser = null;
		    
		    searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/"))));
		    parser = new QueryParser("Content", new StandardAnalyzer());
		    
		    Query q = parser.parse(query);
		    TopDocs topDocs = searcher.search(q, Integer.MAX_VALUE);
		    
		    ScoreDoc[] hits = topDocs.scoreDocs;
		    
		    // Reserve the results matching both basic query and spatial index
		    for(int i = 0; i < hits.length; i++){
		    	Document doc = searcher.doc(hits[i].doc);
		    	if(RecordGeo.contains(doc.get("ItemID"))){
			    	SearchResult tmp = new SearchResult();
			    	tmp.setItemId(doc.get("ItemID"));
			    	tmp.setName(doc.get("Name"));    		
		    		arrayList.add(tmp);
		    	}
		    }


	    	
	    } catch (IOException ex) {
	    	System.out.println(ex);
	    } catch (ParseException ex) {
	    	System.out.println(ex);
	    }

    	// Return the results from numResultsToSkip to numResultsToSkip + numResultsToReturn
	    SearchResult[] results = new SearchResult[Math.min(Math.max(arrayList.size() - numResultsToSkip, 0), numResultsToReturn)];

	    for(int i = 0; i < results.length; i++){
	    	results[i] = arrayList.get(i + numResultsToSkip);
	    }

        // close the database connection
        try {
        	conn.close();
        } catch (SQLException ex) {
        	System.out.println(ex);
        }
        
        return results;
	    
	}
	
	private static String timetoxml(String date){
		/* MySQL timestamp format */
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/* XML date format */
		SimpleDateFormat format2 = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		try{

			Date d = format1.parse(date);
			return format2.format(d);

		} catch(Exception e) {
			System.out.println(e);
		}
		return "";
	}
	
	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		Connection conn = null;
		
	    // create a connection to the database to retrieve Items from MySQL
	    try {
	        conn = DbManager.getConnection(true);
	    } catch (SQLException ex) {
	        System.out.println(ex);
	    }
	    
		StreamResult results = new StreamResult(new StringWriter());
		
		// Get XML Data From ItemID
		try {
			PreparedStatement psItem = conn.prepareStatement("SELECT * FROM ItemsInf, SellersInf WHERE ItemID = ? AND ItemsInf.SellerID = SellersInf.SellerID");
			psItem.setString(1, itemId);
			ResultSet rsItem = psItem.executeQuery();
			
			PreparedStatement psCat = conn.prepareStatement("SELECT * FROM ItemsCat WHERE ItemID = ?");
			psCat.setString(1, itemId);
			ResultSet rsCat = psCat.executeQuery();
			
			PreparedStatement psBid = conn.prepareStatement("SELECT * FROM BiddersInf, ItemsBid WHERE ItemsBid.ItemID = ? AND ItemsBid.BidderID = BiddersInf.BidderID ORDER BY BidTime");
			psBid.setString(1, itemId);
			ResultSet rsBid = psBid.executeQuery();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.newDocument();
			
			while(rsItem.next()){
				
				Element root = doc.createElement("Item");
				doc.appendChild(root);
				// Append ItemID
				Attr itemid_root = doc.createAttribute("ItemID");
				itemid_root.setValue(rsItem.getString("ItemID"));
				root.setAttributeNode(itemid_root);
				
                
				// Append Name
				Element name = doc.createElement("Name");
				name.appendChild(doc.createTextNode(rsItem.getString("Name")));
				root.appendChild(name);
				
				// Append Category
				while(rsCat.next()){
					Element category = doc.createElement("Category");
					category.appendChild(doc.createTextNode(rsCat.getString("Category")));
					root.appendChild(category);
				}
				
				// Append Currently
				Element currently = doc.createElement("Currently");
				currently.appendChild(doc.createTextNode("$" + String.format("%.2f", rsItem.getFloat("Currently"))));
				root.appendChild(currently);
				
				// Append Buy_Price (may exist or not)
				if(rsItem.getFloat("Buy_Price") != 0){
					Element buyprice = doc.createElement("Buy_Price");
					buyprice.appendChild(doc.createTextNode("$" + String.format("%.2f", rsItem.getFloat("Buy_Price"))));
					root.appendChild(buyprice);
				}
				
				// Append First_Bid
				Element firstbid = doc.createElement("First_Bid");
				firstbid.appendChild(doc.createTextNode("$" + String.format("%.2f", rsItem.getFloat("First_Bid"))));
				root.appendChild(firstbid);
				
				// Append Number_of_Bids
				Element numbid = doc.createElement("Number_of_Bids");
				numbid.appendChild(doc.createTextNode(rsItem.getString("Number_of_Bids")));
				root.appendChild(numbid);
				
				// Append Bids
				Element bids = doc.createElement("Bids");
				root.appendChild(bids);
				while(rsBid.next()){
					Element bid = doc.createElement("Bid");
					bids.appendChild(bid);
					Element bidder = doc.createElement("Bidder");
					bid.appendChild(bidder);
					// Append UserID
					Attr userid_bidder = doc.createAttribute("UserID");
					userid_bidder.setValue(rsBid.getString("BidderID"));
					bidder.setAttributeNode(userid_bidder);
					// Append Rating
					Attr rate_bidder = doc.createAttribute("Rating");
					rate_bidder.setValue(rsBid.getString("BidderRating"));
					bidder.setAttributeNode(rate_bidder);
                    
					// Append Location (may exist or not)
					if(rsBid.getString("BidderLoc") != null){
						Element location = doc.createElement("Location");
						location.appendChild(doc.createTextNode(rsBid.getString("BidderLoc")));
						bidder.appendChild(location);						
					}
					// Append Country (may exist or not)
					if(rsBid.getString("BidderCountry") != null){
						Element country = doc.createElement("Country");
						country.appendChild(doc.createTextNode(rsBid.getString("BidderCountry")));
						bidder.appendChild(country);
					}
					
					// Append Time
					Element time = doc.createElement("Time");
					time.appendChild(doc.createTextNode(timetoxml(rsBid.getString("BidTime"))));
					bid.appendChild(time);
					
					// Append Amount
					Element amount = doc.createElement("Amount");
					amount.appendChild(doc.createTextNode("$" + String.format("%.2f", rsBid.getFloat("BidAmount"))));
					bid.appendChild(amount);
				}
				
                
				// Append Location
				Element location = doc.createElement("Location");
				root.appendChild(location);
				location.appendChild(doc.createTextNode(rsItem.getString("Location")));
                
				// Append Latitude (may exist or not)
				if(rsItem.getString("Latitude") != null){
					Attr lati_loc = doc.createAttribute("Latitude");
					lati_loc.setValue(rsItem.getString("Latitude"));
					location.setAttributeNode(lati_loc);					
				}
				// Append Longitude (may exist or not)
				if(rsItem.getString("Longitude") != null){
					Attr long_loc = doc.createAttribute("Longitude");
					long_loc.setValue(rsItem.getString("Longitude"));
					location.setAttributeNode(long_loc);					
				}				
				// Append Country
				Element country = doc.createElement("Country");
				country.appendChild(doc.createTextNode(rsItem.getString("Country")));
				root.appendChild(country);
				
				// Append Started
				Element started = doc.createElement("Started");
				started.appendChild(doc.createTextNode(timetoxml(rsItem.getString("Started"))));
				root.appendChild(started);
				
				// Append Ends
				Element ends = doc.createElement("Ends");
				ends.appendChild(doc.createTextNode(timetoxml(rsItem.getString("Ends"))));
				root.appendChild(ends);
				
				// Append Seller
				Element seller = doc.createElement("Seller");
				// Append UserID
				Attr userid_seller = doc.createAttribute("UserID");
				userid_seller.setValue(rsItem.getString("SellerID"));
				seller.setAttributeNode(userid_seller);
				// Append Rating
				Attr rate_seller = doc.createAttribute("Rating");
				rate_seller.setValue(rsItem.getString("SellerRating"));
				seller.setAttributeNode(rate_seller);
				root.appendChild(seller);
				
				// Append Description
				Element description = doc.createElement("Description");
				description.appendChild(doc.createTextNode(rsItem.getString("Description")));
				root.appendChild(description);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
            
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, results);
			
			rsItem.close();
			rsCat.close();
			rsBid.close();
			
		} catch (Exception ex) {
			System.out.println(ex);
		}
		
        // close the database connection
        try {
        	conn.close();
        } catch (SQLException ex) {
        	System.out.println(ex);
        }
        
		return results.getWriter().toString();
	}
	
	public String echo(String message) {
		return message;
	}

}
