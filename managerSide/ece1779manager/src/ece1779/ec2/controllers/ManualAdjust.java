package ece1779.ec2.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ece1779.ec2.service.ManagerService;

public class ManualAdjust extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tag = request.getParameter("tag");
		PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        ManagerService managerservice = new ManagerService();
        
        if(tag.equals("add")){
			String increaseNum = request.getParameter("addNum");
			int num = Integer.parseInt(increaseNum);
			if(num > 0 && num < 6){
				int runningNum = managerservice.getRunningWorkers().size();
//				System.out.println("running size:" + runningNum);
				if(runningNum + num >20){
					num = 20 - runningNum;
				}
				managerservice.increaseWorkers(num);
				response.sendRedirect("Manager.jsp");
			}
			else{
		        request.getSession().setAttribute("input", "invalid");
		        response.sendRedirect("Manager.jsp");
			}
		}
        else if(tag.equals("delete")){
			String decreaseNum = request.getParameter("deleteNum");
			int num = Integer.parseInt(decreaseNum); 
			int runningNum = managerservice.getRunningWorkers().size();
			if((runningNum-num)>1){
				managerservice.reduceWorkers(num);
				response.sendRedirect("Manager.jsp");
			}
			else{
		        request.getSession().setAttribute("input", "invalid");
		        response.sendRedirect("Manager.jsp");
			}
		}
        else if(tag.equals("singleDelete")){
			String id = request.getParameter("id");
			List<String> ids = new ArrayList<String>();
			ids.add(id);
			managerservice.stopInstances(ids);
			response.sendRedirect("Manager.jsp");
		}
	}

}
