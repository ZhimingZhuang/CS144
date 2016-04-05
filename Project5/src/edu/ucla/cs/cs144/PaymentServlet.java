package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

public class PaymentServlet extends HttpServlet implements Servlet {
    
    public PaymentServlet() {}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        
        String itemId = (String)request.getParameter("id");
        String tmp = (String)session.getAttribute("ItemId");
        
        if(tmp == null){
            response.sendRedirect("error.html");
        }else{
            String ItemId = (String)session.getAttribute("ItemId");
            String Name = (String)session.getAttribute("Name");
            String Buy_Price = (String)session.getAttribute("Buy_Price");
        
            String serverName = request.getServerName();
            String context = request.getContextPath();
            String Confirmlink  = "https://" + serverName + ":8443" + context + "/confirm?id=" + itemId;
            
            request.setAttribute("ItemId", ItemId);
            request.setAttribute("Name", Name);
            request.setAttribute("Buy_Price", Buy_Price);
            request.setAttribute("Confirmlink", Confirmlink);
            
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }
    }

}
