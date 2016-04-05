<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
   <script type="text/javascript" src="autosuggest.js"></script>
   <script type="text/javascript" src="suggestions.js"></script>
   <script type="text/javascript"> 
      window.onload = function () {
         var oTextbox = new AutoSuggestControl(document.getElementById("keyword"), new StateSuggestions());
      }
   </script>
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
      height: 60px;
      z-index: 99;
      top: 80px;
      left: 0px;
   }

   #content{
      top: 140px;
      left:0px;
   }
   #PreNext{
      line-height: 30px;
      text-align: center;
      top: 680px;
      left:42%;
   }
   div.suggestions {
       -moz-box-sizing: border-box;
       box-sizing: border-box;
       border: 1px solid black;
       position: absolute;
       background-color: white;  
   }

   div.suggestions div {
       cursor: default;
       padding: 0px 3px;
   }

   div.suggestions div.current {
       background-color: #3366cc;
       color: white;
   }
   </style>
</head>

<body>
<div id = "header" style="color:#FFF">Please type in the keyword you search: </div>

<div id = "searchbox">
<form action = "/eBay/search" method = "GET">
<input type = "text" style="width:340px;" autocomplete="off" id = "keyword" name = "q" />
<input type = "hidden" name = "numResultsToSkip" value = "0" />
<input type = "hidden" name = "numResultsToReturn" value = "20" />
<input type = "submit" style="color:#F0F" value = "Search" />
</form>
</div>


<table id = "content" border="1" width = "100%">
   <%    SearchResult[] results = (SearchResult[])request.getAttribute("results");
         if(results != null && results.length != 0){
   %>
      <tr>
         <th width = "20%">ItemId</th>
         <th width = "80%">Name</th>
      </tr>
   <%    }
   %>

   <% for(int i = 0; i < results.length; i++) { %>
      <tr>
         <td><a href = <%="item?id=" + results[i].getItemId() %>><%= results[i].getItemId() %></a></td>
         <td><%= results[i].getName() %></td>
      </tr>
   <% } %>
</table>

<table id = "PreNext" width = "16%">
   <tr>
      <td align="left"><a href = <%="search?q=" + request.getAttribute("query") + "&numResultsToSkip=" + request.getAttribute("pre") + "&numResultsToReturn=" + request.getAttribute("return") %> style = <%= request.getAttribute("showPre") %>>Previous</a>
      </td>
      <td align="right"><a href = <%="search?q=" + request.getAttribute("query") + "&numResultsToSkip=" + request.getAttribute("next") + "&numResultsToReturn=" + request.getAttribute("return")%> style = <%= request.getAttribute("showNext") %> >Next</a>
      </td>
   </tr>
</table>     

</body>
</html>