package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import java.net.URLEncoder;

import javax.xml.bind.JAXB;
import java.io.StringReader;

public class ItemServlet extends HttpServlet implements Servlet {
    
    public ItemServlet() {}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String itemId = (String)request.getParameter("id");
        String error = "";
        
        String xml = AuctionSearchClient.getXMLDataForItemId(itemId);

        Item item = null;
        try {
            StringReader sr = new StringReader(xml);
            item = JAXB.unmarshal(sr, Item.class);
        } catch (Exception e) {
            response.getWriter().write(e.toString());
            System.out.println(e);
        }

        if(item == null) {
            response.sendRedirect("reInputItemId.html");
        }
        else{
            HttpSession session = request.getSession(true);
            
            if(item.getBuyPrice() != null){
                session.setAttribute("ItemId", item.getId());
                session.setAttribute("Name", item.getName());
                session.setAttribute("Buy_Price", item.getBuyPrice());
            }
            
            request.setAttribute("item", item);
            request.getRequestDispatcher("/itemsearchresult.jsp").forward(request, response);
        }
        

    }
}
