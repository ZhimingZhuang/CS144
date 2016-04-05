package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        
        String query = "";
        int numResultsToSkip;
        int numResultsToReturn;
        
        try{
            query = (String) request.getParameter("q");
            numResultsToSkip = Integer.parseInt((String)request.getParameter("numResultsToSkip"));
            numResultsToReturn = Integer.parseInt((String)request.getParameter("numResultsToReturn"));
        }catch (Exception e){
            query = "";
            numResultsToSkip = 0;
            numResultsToReturn = 20;
        }
        
        if(numResultsToSkip < 0)    numResultsToSkip = 0;
        if(numResultsToReturn < 0)  numResultsToReturn = 20;
        if(numResultsToReturn == 0) numResultsToReturn = Integer.MAX_VALUE;
        

        SearchResult[] results = AuctionSearchClient.basicSearch(query, numResultsToSkip, numResultsToReturn);
        SearchResult[] nextpage = AuctionSearchClient.basicSearch(query, numResultsToSkip+numResultsToReturn, numResultsToReturn);
        request.setAttribute("results", results);
        request.setAttribute("query", URLEncoder.encode(query, "UTF-8"));
        request.setAttribute("pre", Math.max(0, numResultsToSkip - numResultsToReturn));
        request.setAttribute("next",  numResultsToSkip + numResultsToReturn);
        request.setAttribute("return", numResultsToReturn);

        if(numResultsToSkip == 0)
            request.setAttribute("showPre", "visibility : hidden");
        else
            request.setAttribute("showPre", "");

        if(nextpage.length <= 0)
            request.setAttribute("showNext", "visibility : hidden");
        else
            request.setAttribute("showNext", "");
        
        request.getRequestDispatcher("/keywordSearchResult.jsp").forward(request, response);
        
    }
}
