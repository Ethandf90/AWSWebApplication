<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import ="java.util.*" %>
<%@ page import ="ece1779.ec2.mode.WorkerMode" %>
<%@ page import ="ece1779.ec2.mode.ManagerMode" %>
<%@ page import ="ece1779.ec2.service.ManagerService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Stylesheets -->
<link rel="stylesheet" href="styles/project.css">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<title>ManagerUI</title>

</head>
<body>
	<% 
		String login = (String)request.getSession().getAttribute("ManagerLogin");
		String input = (String)request.getSession().getAttribute("input");
		if(login.equals("loggedIn")){	
			if(input.equals("valid")){
	%>
	
	<h3>WorkerPool(the running instances):</h3>
	<%
		ManagerService managerservice = new ManagerService();
		List<WorkerMode> workers = managerservice.getRunningWorkers();
	%>
	<table id="tableData">
  	<thead>
	  	<tr>
	    <td>ID</td>
	    <td>CPU utilization</td> 
	    <td>Manage</td>
	    </tr>
  	</thead>
  	<%	
  		for(int i=0; i<workers.size();i++){
  			String id = workers.get(i).getId();
  	%>
  	<tr>
    <td><%=id %></td>
    <td><%=workers.get(i).getCpuUsage() %>%</td> 
    <td>
    	<form action="ManualAdjust" method ="post">
    	<input type="hidden" name= "tag" value="singleDelete">
    	<input type="hidden" name="id" value=<%=id %> >
    	<input type="submit" value="Stop">
    	</form>
    </td>
  	</tr>
  	<% 
  		}
  	%>
  	<tfoot>
		<tr>
		<td colspan="3">Average CPU utilization: <%=managerservice.averageCPU() %>%</td>
		</tr>
	</tfoot>
	</table>
	
	<hr>
	
	<h3>Adjust workerpool </h3>
	<form action="ManualAdjust" method="post">
	Increase worker by number:   <input id="IW" type="text" name="addNum">
	<input type="hidden" name="tag" value="add">
	<input type="submit" value="Increase"><br><br />
	</form>
	
	<form action="ManualAdjust" method="post">
	Decrease worker by number:   <input id="DW" type="text" name="deleteNum">
	<input type="hidden" name="tag" value="delete">
	<input type="submit" value="Decrease">
	</form>

	<hr>
	<%
		ManagerMode manager = new ManagerMode();
		manager = managerservice.getAutoScalingParameter();	
		
	%>
	
	
	<h3>Auto-Scaling Activated: <%=manager.getAutoScaling() %></h3>
	
	<form action="AutoScaling" method="post">
	ThresholdGrow: <input id="TG" type="text" name="thresholdgrow" placeholder=<%=manager.getThresholdGrow() %>>%<br><br />
	ThresholdShrink: <input id="TS" type="text" name="thresholdshrink" placeholder=<%=manager.getThresholdShrink() %>>%<br><br />
	Expanding Ratio: <input id="ER" type="text" name="expandingratio" placeholder=<%=manager.getRatioExpand() %>><br><br />
	Shrinking Ratio: <input id="SR" type="text" name="shrinkingratio" placeholder=<%=manager.getRatioShrink() %>><br><br />
	Activate Autoscaling? <input type="checkbox" name="auto" value= "YES" ><br>
	<input type="submit" value="Set">
	</form>
	
	
	<hr> 
	
	<h3>Clean the bucket</h3>
	<form action="Delete" method="post">
		<input type="submit" value = "Delete">
	</form>
	<br><br>
	
	<% 
		}
		else{
	%>
		<h2>Invalid input! Do it again!</h2>
		<a href="Manager.jsp">Back</a>
		
	<% 
		request.getSession().setAttribute("input", "valid");
		}
		}
		else{
	%>
		Please login!
		<a href="ManagerLogin.jsp">Back</a>
	<%
		}
	%>
</body>
</html>