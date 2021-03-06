package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    private IndexWriter indexWriter = null;
	
	public IndexWriter getIndexWriter(boolean create) throws IOException {
		if(indexWriter == null){
			Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/"));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
			indexWriter = new IndexWriter(indexDir, config);
		}
		return indexWriter;
	}
	
	
	public void closeIndexWriter() throws IOException {
		if (indexWriter != null) {
	        indexWriter.close();
	    }
	}
	
	public void indexItem(String ItemID, String Name, String Category, String Description) throws IOException {
		IndexWriter writer = getIndexWriter(false);
		
		Document doc = new Document();
		doc.add(new StringField("ItemID", ItemID, Field.Store.YES));
		doc.add(new StringField("Name", Name, Field.Store.YES));
		//doc.add(new TextField("Category", Category, Field.Store.YES));
		//doc.add(new TextField("Description", Description, Field.Store.YES));
		String fullSearchableText = Name + " " + Category + " " + Description;
		doc.add(new TextField("Content", fullSearchableText, Field.Store.YES));
        
		writer.addDocument(doc);
	}
	
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
        try {
        	conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
        	System.out.println(ex);
        }


	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
        try{
            getIndexWriter(true);
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT ItemID, Name, Description FROM ItemsInf");
            PreparedStatement prestat = conn.prepareStatement("SELECT Category FROM ItemsCat WHERE ItemID=?");
            

            
            while(rs.next()){
                String ItemID = "";
                String Name = "";
                String Category = "";
                String Description ="";
                
                ItemID = rs.getString("ItemID");
                Name = rs.getString("Name");
                Description = rs.getString("Description");

                
                prestat.setString(1, ItemID);
                ResultSet rs_prestat = prestat.executeQuery();
                while(rs_prestat.next()){
                    Category += rs_prestat.getString("Category") + " ";
                }
                
                indexItem(ItemID, Name, Category, Description);
                rs_prestat.close();
            }
            
            /* close the resultset, statement IndexWriter*/
            rs.close();
            s.close();
            closeIndexWriter();
        } catch (IOException ex){
            System.out.println(ex);
        } catch (SQLException ex){
            System.out.println(ex);
        }
        
        
        // close the database connection
        try {
        	conn.close();
        } catch (SQLException ex) {
        	System.out.println(ex);
        }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
