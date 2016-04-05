<!DOCTYPE html>
<%@ page import = "java.util.*" %>
<%@ page import = "edu.ucla.cs.cs144.*;" %>
<html>
<head>
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

   #content{
      width:100%;
      text-align: center;
      padding:5px;
   }

   #creditbox{
    text-align: center;
    font-size: 20pt;
    width: 100%;
    z-index: 99;
   }

   </style>
</head>

<body>
<div id = "header" style="color:#FFF">Confirm Page</div>

<div id = "content">
<h1>Bill Information:</h1>
<table border = "1" width = "100%">
      <tr>
         <td>ItemID</td>
         <td><%= request.getAttribute("ItemId") %></td>
      </tr>
      <tr>
         <td>Name</td>
         <td><%= request.getAttribute("Name") %></td>
      </tr>
      <tr>
         <td>Buy Price</td>
         <td><%= request.getAttribute("Buy_Price") %></td>
      </tr>
      <tr>
         <td>Credit Card</td>
         <td><%= request.getAttribute("Card") %></td>
      </tr>
      <tr>
         <td>Time</td>
         <td><%= request.getAttribute("Time") %></td>
      </tr>
</table>
</div>

<div id = "creditbox">
<p> Succeed. Thank you for your payment.</p>
<% String keywordsearch = "http://" + request.getServerName() + ":1448" + request.getContextPath() + "/keywordsearch.html";
   String getitem = "http://" + request.getServerName() + ":1448" + request.getContextPath() + "/getItem.html"; %>
<li><a href = "<%= keywordsearch %>">Keyword search</a></li>
<li><a href = "<%= getitem %>">Item search</a></li>   
</div>     

</body>
</html>