package ece1779.ec2.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ece1779.ec2.mode.ManagerMode;
import ece1779.ec2.service.ManagerService;


public class AutoScaling extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        ManagerService managerservice = new ManagerService();
        
        Double thresholdgrow = Double.parseDouble((String)request.getParameter("thresholdgrow")) ;
        Double thresholdshrink = Double.parseDouble((String)request.getParameter("thresholdshrink"));
        int expandingratio = Integer.parseInt((String)request.getParameter("expandingratio"));
        int shrinkingratio = Integer.parseInt((String)request.getParameter("shrinkingratio"));
        String autoscaling = (String)request.getParameter("auto");
        if(autoscaling == null){
        	autoscaling = "NO";
        }
        
        ManagerMode manager = 
        		new ManagerMode(thresholdgrow,thresholdshrink,expandingratio,shrinkingratio,autoscaling);
//        System.out.println(manager.getAutoScaling());
        managerservice.updateAutoScalingParameter(manager);
        
        response.sendRedirect("Manager.jsp");
	}

}
