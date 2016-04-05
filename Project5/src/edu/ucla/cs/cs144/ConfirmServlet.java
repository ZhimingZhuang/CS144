package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConfirmServlet extends HttpServlet implements Servlet {
    
    public ConfirmServlet() {}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
//        if(request.isSecure()){
//            response.sendRedirect("error.html");
//        }else{
            HttpSession session = request.getSession(true);
            //String itemId = (String)request.getParameter("id");
            String Card = (String)request.getParameter("card");
            String tmp = (String)session.getAttribute("ItemId");
            
            if(tmp == null){
                response.sendRedirect("error.html");
            }else{
                String ItemId = (String)session.getAttribute("ItemId");
                String Name = (String)session.getAttribute("Name");
                String Buy_Price = (String)session.getAttribute("Buy_Price");
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String CurrentTime = format.format(date);
                
                request.setAttribute("ItemId", ItemId);
                request.setAttribute("Name", Name);
                request.setAttribute("Buy_Price", Buy_Price);
                request.setAttribute("Card", Card);
                request.setAttribute("Time", CurrentTime);
                
                request.getRequestDispatcher("confirm.jsp").forward(request, response);
            }
            
        }
//    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }
}
