/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */


package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile (File xmlFile) throws IOException {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). 
           You need to fill in code that processes the internal representation of the XML tree 
           and produces MySQL load files according to the relational schema you designed in Part B.
         */

        Element root = doc.getDocumentElement();
        Element [] elemItems = getElementsByTagNameNR(root, "Item");
        
        File file1 = new File("ItemsInf.txt");
        if(!file1.exists()){ file1.createNewFile(); }
        
        File file2 = new File("ItemsCat.txt");
        if(!file2.exists()){ file2.createNewFile(); }
        
        File file3 = new File("ItemsBid.txt");
        if(!file3.exists()){ file3.createNewFile(); }
        
        File file4 = new File("BiddersInf.txt");
        if(!file4.exists()){ file4.createNewFile(); }
        
        File file5 = new File("SellersInf.txt");
        if(!file5.exists()){ file5.createNewFile(); }
        
        BufferedWriter bwItemsInf = new BufferedWriter(new FileWriter(file1.getName(), true));
        BufferedWriter bwItemsCat = new BufferedWriter(new FileWriter(file2.getName(), true));
        BufferedWriter bwItemsBid = new BufferedWriter(new FileWriter(file3.getName(), true));
        BufferedWriter bwBiddersInf = new BufferedWriter(new FileWriter(file4.getName(), true));
        BufferedWriter bwSellersInf = new BufferedWriter(new FileWriter(file5.getName(), true));
        
        for(Element e : elemItems){
            String ItemID = e.getAttribute("ItemID");
            String Name = getElementTextByTagNameNR(e, "Name");
            String Currently = strip(getElementTextByTagNameNR(e, "Currently"));
            String Buy_Price = strip(getElementTextByTagNameNR(e, "Buy_Price"));
            String First_Bid = strip(getElementTextByTagNameNR(e, "First_Bid"));
            String Number_of_Bids = getElementTextByTagNameNR(e, "Number_of_Bids");
            String Country = getElementTextByTagNameNR(e, "Country");
            String Started = timetomysql(getElementTextByTagNameNR(e, "Started"));
            String Ends = timetomysql(getElementTextByTagNameNR(e, "Ends"));
            String Location = getElementTextByTagNameNR(e, "Location");
            Element locate = getElementByTagNameNR(e, "Location");
            String Latitude = locate.getAttribute("Latitude");
            String Longitude = locate.getAttribute("Longitude");
            Element seller = getElementByTagNameNR(e, "Seller");
            String SellerID = seller.getAttribute("UserID");
            String SellerRating = seller.getAttribute("Rating");
            String Description = getElementTextByTagNameNR(e, "Description");

            if(Description.length() > 4000)
                Description = Description.substring(0, 4000);

            // Write tuples to ItemsInf
            bwItemsInf.write(ItemInf(ItemID, Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Latitude, Longitude, Country, Started, Ends, SellerID, Description));
            bwItemsInf.newLine();
            
            // Write tuples to ItemsCat
            Element [] elemCates = getElementsByTagNameNR(e, "Category");
            for(Element cate : elemCates){
                String tmpcate = ItemID + columnSeparator + getElementText(cate) ;
                bwItemsCat.write(tmpcate);
                bwItemsCat.newLine();
            }
            
            // Write tuples to SellersInf
            String tmpseller = SellerID + columnSeparator + SellerRating;
            bwSellersInf.write(tmpseller);
            bwSellersInf.newLine();
            
            // Write tuples to BiddersInf and ItemsBid
            Element [] elemBidders = getElementsByTagNameNR(getElementByTagNameNR(e, "Bids"), "Bid");
            for(Element b : elemBidders){
            	Element bidder = getElementByTagNameNR(b, "Bidder");
            	String BidderID = bidder.getAttribute("UserID");
            	String BidderRating = bidder.getAttribute("Rating");
            	
            	String BidTime = timetomysql(getElementTextByTagNameNR(b, "Time"));
            	String BidAmount = strip(getElementTextByTagNameNR(b, "Amount"));
            	
            	String BidderLoc = getElementTextByTagNameNR(bidder, "Location");
            	String BidderCountry = getElementTextByTagNameNR(bidder, "Country");
            	
            	bwItemsBid.write(ItemBid(ItemID, BidderID, BidTime, BidAmount));
            	bwItemsBid.newLine();
            	
            	bwBiddersInf.write(BidderInf(BidderID, BidderRating, BidderLoc, BidderCountry));
            	bwBiddersInf.newLine();
            	
            	
            }
            
        }
        
        bwItemsInf.close();
        bwItemsCat.close();
        bwItemsBid.close();
        bwBiddersInf.close();
        bwSellersInf.close();
        /**************************************************************/
        
    }
    
    /* Function to store records in the given schema */

    private static String ItemInf(String ItemID, String Name, String Currently, String Buy_Price, String First_Bid, String Number_of_Bids, String Location, String Latitude, String Longitude, String Country, String Started, String Ends, String SellerID, String Description){
        
        StringBuffer tuple = new StringBuffer();
        /* 1 Add ItemID to the tuple */
        tuple.append(ItemID);
        tuple.append(columnSeparator);
        /* 2 Add Name to the tuple */
        tuple.append(Name);
        tuple.append(columnSeparator);
        /* 3 Add Currently to the tuple */
        tuple.append(Currently);
        tuple.append(columnSeparator);
        /* 4 Add Buy_Price to the tuple */
        if(Buy_Price.equals("")) tuple.append("\\N");
        else tuple.append(Buy_Price);
        tuple.append(columnSeparator);
        /* 5 Add First_Bid to the tuple */
        tuple.append(First_Bid);
        tuple.append(columnSeparator);
        /* 6 Add Number_of_Bids to the tuple */
        tuple.append(Number_of_Bids);
        tuple.append(columnSeparator);
        /* 7 Add Location to the tuple */
        tuple.append(Location);
        tuple.append(columnSeparator);
        /* 8 Add Latitude to the tuple */
        if(Latitude.equals("")) tuple.append("\\N");
        else tuple.append(Latitude);
        tuple.append(columnSeparator);
        /* 9 Add Longitude to the tuple */
        if(Longitude.equals("")) tuple.append("\\N");
        else tuple.append(Longitude);
        tuple.append(columnSeparator);
        /* 10 Add Country to the tuple */
        tuple.append(Country);
        tuple.append(columnSeparator);
        /* 11 Add Started to the tuple */
        tuple.append(Started);
        tuple.append(columnSeparator);
        /* 12 Add Ends to the tuple */
        tuple.append(Ends);
        tuple.append(columnSeparator);
        /* 13 Add SellerID to the tuple */
        tuple.append(SellerID);
        tuple.append(columnSeparator);
        /* 14 Add Description to the tuple */
        tuple.append(Description);

        return tuple.toString();

    }    

    private static String ItemBid(String ItemID, String BidderID, String BidTime, String BidAmount){
    	StringBuffer tuple = new StringBuffer();
    	/* 1 Add ItemID to the tuple */
    	tuple.append(ItemID);
    	tuple.append(columnSeparator);
    	/* 2 Add BidderID to the tuple */
    	tuple.append(BidderID);
    	tuple.append(columnSeparator);
    	/* 3 Add BidTime to the tuple */
    	tuple.append(BidTime);
    	tuple.append(columnSeparator);
    	/* 4 Add BidAmount to the tuple */
    	tuple.append(BidAmount);
    	
    	return tuple.toString();	
    }
    
    private static String BidderInf(String BidderID, String BidderRating, String BidderLoc, String BidderCountry){
    	StringBuffer tuple = new StringBuffer();
    	/* 1 Add BidderID to the tuple */
    	tuple.append(BidderID);
    	tuple.append(columnSeparator);
    	/* 2 Add BidderRating to the tuple */
    	tuple.append(BidderRating);
    	tuple.append(columnSeparator);
    	/* 3 Add BidderLoc to the tuple */
    	if(BidderLoc.equals("")) tuple.append("\\N");
    	else tuple.append(BidderLoc);
    	tuple.append(columnSeparator);
    	/* 4 Add BidderCountry to the tuple */
    	if(BidderCountry.equals("")) tuple.append("\\N");
    	else tuple.append(BidderCountry);
    	tuple.append(columnSeparator);
    	
    	return tuple.toString();
    	
    }
    /* Adjust the time format to timestamp of mysql*/
    private static String timetomysql(String date){
        /* MySQL timestamp format */
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /* XML date format */
        SimpleDateFormat format2 = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

        try{
            Date parsed = format2.parse(date);
            return format1.format(parsed);
        }catch(ParseException pe){
            System.out.println(pe);
        }
        return "";
    }
    

    public static void main (String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        

        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
