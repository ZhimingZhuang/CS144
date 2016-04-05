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
<div id = "header" style="color:#FFF">Payment Page</div>

<div id = "content">
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
</table>
</div>

<div id = "creditbox">
<p> Credit Card Number: </p>
<form action = "<%= request.getAttribute("Confirmlink") %>" method = "POST">
<input type = "text" style="width:300px;" name = "card" />
<input type = "submit" style="color:#F0F" value = "Submit" />
</form> 
</div>     

</body>
</html>