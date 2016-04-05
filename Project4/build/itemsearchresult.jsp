<!DOCTYPE html>
<%@ page import = "java.util.*" %>
<%@ page import = "edu.ucla.cs.cs144.*;" %>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<style type = "text/css">
#header{
   background-color: gray;
   line-height: 80px;
   text-align: center;
   font-size: 20pt;
   width: 100%;
   height: 80px;
   z-index: 99;
   top: 0px;
   left: 0px;
}
#searchbox{
   line-height: 60px;
   text-align: center;
   font-size: 20pt;
   width: 100%;
   height: 100px;
   z-index: 99;
   top: 80px;
   left: 0px;
}
#content{
/*   width:100%;
   float:left;
   top: 140px;
   left:0px;*/
    width:50%;
    /*height:450px;*/
    float:left;
    padding:5px;
}
#map_canvas {
  width:45%;
  height:450px;
  float:left;
  padding:5px;
} 
#Desc{
/*   width:100%;
   float:left;
   top: 140px;
   left:0px;*/
    background-color:#ffffff;
    width:100%;
    float:left;
    padding:5px;
}

#bidInfo{
   width:100%;
   float:left;
   left:0px;
}

</style> 
<script type="text/javascript" 
    src="http://maps.google.com/maps/api/js?sensor=false"> 
</script> 
<% Item item = (Item)request.getAttribute("item");
%>
<script type="text/javascript"> 
  function initialize() {
      var draw = function(latlng) {
          var myOptions = {
            zoom: 14, // default is 8 
            center: latlng,
            mapTypeId: google.maps.MapTypeId.ROADMAP
          };
          var title = "<%= item.getName() %>";
          var map = new google.maps.Map(document.getElementById("map_canvas"),
              myOptions);
          var marker = new google.maps.Marker({
            position: latlng,
            map: map,
            title: title
          });
      }
      var latitude = <%= item.getLocation().getLatitude() %>;
      var longitude = <%= item.getLocation().getLongitude() %>;
      var latlng = null;
      if(latitude < -90 || latitude > 90)
         latitude = null;
      if(longitude < -180 || longitude > 180)
         longitude = null;
      
      if(latitude != null && longitude != null) {
         latlng = new google.maps.LatLng(latitude,longitude);
         draw(latlng);
       }
       else {
         var address = "<%= item.getLocation().getLocation() %>" + ", " + "<%= item.getCountry() %>";
         var geocoder = new google.maps.Geocoder();
         geocoder.geocode({"address" : address}, function (results, status) {
            if(status == google.maps.GeocoderStatus.OK) {
               var latlng = results[0].geometry.location;
               draw(latlng);
            }
         });
       }
  } 
</script> 
</head>

<body onload="initialize()">
<div id = "header" style="color:#FFF">Please type in the itemId you search: </div>

<div id = "searchbox">
<form action = "/eBay/item" method = "GET">
<input type = "text" style="width:340px;" id = "id" name = "id" />
<input type = "submit" style="color:#F0F" value = "Search" />
</form>
</div>

<div id = "content">
<h1>The Detail of Items:</h1>
<table width = "100%">
      <tr>
         <td>ItemID</td>
         <td><%= item.getId() %></td>
      </tr>
      <tr>
         <td>Name</td>
         <td><%= item.getName() %></td>
      </tr>
      <tr>
         <td>Category</td>
         <td><%= item.getCategories() %></td>
      </tr>
      <tr>
         <td>Country</td>
         <td><%= item.getCountry() %></td>
      </tr>
      <tr>
         <td>Location Spec</td>
         <td>
            <ul>
               <li><%= item.getLocation().getLocation() %></li>
               <% if(item.getLocation().getLatitude() != null) { %>
               <li>Latitude: <%= item.getLocation().getLatitude() %></li>
               <% }else{ %>
               <li>Lattitude: </li>
               <% } %>
               <% if(item.getLocation().getLongitude() != null) { %>
               <li>Longitude: <%= item.getLocation().getLongitude() %></li>
               <% }else{ %>
               <li>Longitude: </li>
               <% } %>
            </ul>
         </td>
      </tr>
      <tr>
         <td>Seller</td>
         <td>
            <ul>
               <li>Name: <%= item.getSeller().getUserID() %></li>
               <li>Rating: <%= item.getSeller().getRating() %></li>
            </ul>
         </td>
      </tr>
      
      <tr>
         <td>Buy Price</td>
         <% if(item.getBuyPrice() != null) {%>
         <td><%= item.getBuyPrice() %></td>
         <% }else{ %>
         <td>Null</td>
         <% } %>
      </tr>
      <tr>
         <td>Currently</td>
         <td><%= item.getCurrently() %></td>
      </tr>
      <tr>
         <td>Started</td>
         <td><%= item.getStarted() %></td>
      </tr>
      <tr>
         <td>Ends</td>
         <td><%= item.getEnds() %></td>
      </tr>
      <tr>
         <td>Num Of Bids</td>
         <td><%= item.getNumBids() %></td>
      </tr>
      <tr>
         <td>First Bid</td>
         <td><%= item.getFirstBid() %></td>
      </tr>
</table>
</div>

<div id="map_canvas"></div> 

<div id = "Desc">
  <h3>Description</h3>
<table border="0" width = "100%">
  <tr>
     <td><%= item.getDescription() %></td>
  </tr>
</table>
</div>

<div id = "bidInfo" >
  <h1 id = "preLine">The Detail of Bids Information:</h1>
  <table border="1" width = "100%" >
      <tr>
        <th>Bidder Id</th>
        <th>Bidder Rating</th>
        <th>Bidder Loation</th>
        <th>Bidder Country</th>
        <th>Bid Time</th>
        <th>Bid Amount</th>
      </tr>
      <%  List<Bid> bids = item.getBids();
        for (int i = 0; i < bids.size(); i++) { %>
        <tr>
          <td><%= bids.get(i).getBidder().getBidderId() %></td>
          <td><%= bids.get(i).getBidder().getBidderRating() %></td>
          <td><%= bids.get(i).getBidder().getLocation() %></td>
          <td><%= bids.get(i).getBidder().getCountry() %></td>
          <td><%= bids.get(i).getTime() %></td>
          <td><%= bids.get(i).getAmount() %></td>
        </tr>
      <% } %>
  </table>
</div>

</body>
</html>