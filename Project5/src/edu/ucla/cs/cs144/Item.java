package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;

import java.util.*;

/* For Representing and Parsing an XML String */

@XmlRootElement(name="Item")
public class Item {
	
	private String id;			//itemID attribute
	
	private String name;		//name

	private String currently;	//Currently

	private String buyPrice;

	private String firstBid;	//First_Bid

	private int numBids;		//Number_of_Bids

	//private String location;	//Location

	private String country;		//Country
	
	private String started;		//Started

	private String ends;		//Ends

	private String description;

	private Location Location;

	private Seller Seller;

	private List<String> Category = new ArrayList<String>();

	private List<Bid> Bid = new ArrayList<Bid>();


	@XmlElement(name = "Category")
	public List<String> getCategories() {
		return this.Category;
	}

	public void setCategories(List<String> categories) {
		this.Category = categories;
	}

	@XmlElementWrapper(name = "Bids")
	@XmlElement(name = "Bid")
	public List<Bid> getBids() {
		return this.Bid;
	}

	public void setBids(List<Bid> bids) {
		this.Bid = bids;
	}

	@XmlElement(name = "Seller")
	public Seller getSeller() {
		return this.Seller;
	}

	public void setSeller(Seller seller) {
		this.Seller = seller;
	} 	

	@XmlElement(name = "Buy_Price")
	public String getBuyPrice() {
		return this.buyPrice;
	}

	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}


	@XmlAttribute(name="ItemID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="Currently")
    public String getCurrently() {
        return currently;
    }

    public void setCurrently(String currently) {
        this.currently = currently;
    }

    @XmlElement(name="First_Bid")
    public String getFirstBid() {
        return firstBid;
    }

    public void setFirstBid(String firstBid) {
        this.firstBid = firstBid;
    }

    @XmlElement(name="Number_of_Bids")
    public int getNumBids() {
        return numBids;
    }

    public void setNumBids(int numBids) {
        this.numBids = numBids;
    }

	@XmlElement(name = "Location")
    public Location getLocation() {
		return this.Location;
	}
	
	public void setLocation(Location location) {
		this.Location = location;
	}

    @XmlElement(name="Country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @XmlElement(name="Started")
    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    @XmlElement(name="Ends")
    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    @XmlElement(name="Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

