package ece1779.ec2.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManagerLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String managerID = request.getSession().getServletContext().getInitParameter("managerID");
    	String managerPassword = request.getSession().getServletContext().getInitParameter("managerPassword");
    	
    	String inputID = request.getParameter("managerID");
    	String inputPW = request.getParameter("managerPassword");
    	
    	if(inputID.equals(managerID)&&inputPW.equals(managerPassword)){
    		request.getSession().setAttribute("ManagerLogin", "loggedIn");
    		request.getSession().setAttribute("input", "valid");
    		response.sendRedirect("Manager.jsp");
    	}
    	else{
    		response.setContentType("text/html");
            PrintWriter out = response.getWriter();
    		out.println("<head><title>Create New User</title></head>");
	        out.println("<body>");
	        out.println("Please enter valid info!");
	        out.println("<li><a href='ManagerLogin.jsp'>Back</a></li>");
			out.println("</body>");
		    out.println("</html>");
    	}
	}

}
