package ece1779.ec2.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ece1779.ec2.mode.userMode;
import ece1779.ec2.service.UserService;


public class userLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		String tag = request.getParameter("tag");
		
		if(tag.equals("new") ){
//			System.out.println(tag);
			String login = request.getParameter("login");
			String password = request.getParameter("password");
			String Rpassword = request.getParameter("Rpassword");
			
			out.println("<head><title>Create New User</title></head>");
	        out.println("<body>");
	        
			if(login!="" && password!="" && Rpassword!="" && password.equals(Rpassword)){
				
				userMode newuser = new userMode();
				newuser.setLogin(login);
				newuser.setPassword(password);
				UserService userservice = new UserService();
				//if the new does not exist
				if( !userservice.createAccount(newuser)){
					out.println("Congratulations! New user created!");
				}
				else{
					out.println("The user already exists!");
				}
			}
			else{
				out.println("Fail!  Something is wrong, do it again!");
			}
			out.println("<li><a href='UserLogin.jsp'>Back</a></li>");
			out.println("</body>");
		    out.println("</html>");
		}
		
		else if(tag.equals("old")){
//			System.out.println(tag);
			String login = request.getParameter("login");
			String password = request.getParameter("password");
			userMode newuser = new userMode();
			newuser.setLogin(login);
			newuser.setPassword(password);
			UserService userservice = new UserService();
			if(userservice.authenticate(newuser)){
				request.getSession().setAttribute("login", login);
				response.sendRedirect("fileupload.jsp");
//				RequestDispatcher dispatcher = request.getRequestDispatcher("fileupload.jsp");
//				dispatcher.forward(request, response);
			}
			else{
				out.println("<head><title>Create New User</title></head>");
		        out.println("<body>");
		        out.println("Fail! UserID or Password is wrong!");
		        out.println("<li><a href='UserLogin.jsp'>Back</a></li>");
		        out.println("</body>");
			    out.println("</html>");
			}
			
			
		}
	}

}
