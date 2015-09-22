<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ece1779.ec2.mode.userMode" %>
<%@ page import="ece1779.ec2.mode.imageMode" %>
<%@ page import="ece1779.ec2.service.UserService" %>
<%@ page import="ece1779.ec2.service.ImageService" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>

<!-- Stylesheets -->
<link rel="stylesheet" href="styles/project.css">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
</head>
<body onunload="">
	<div>
	
	<% 
		String name = (String)request.getSession().getAttribute("login");
		if(name == null){
			response.sendRedirect("UserLogin.jsp");
		}
		else{
		userMode user = new userMode();
		UserService userservice = new UserService();
		user = userservice.getUser(name);
	%>
		<h3>Welcome, <%=name %> !  Your userId is <%= user.getId() %> </h3> 
		<form action="UserLogout" method="get">
			<input id="logout" type="submit" value="Logout">
		</form> 
		<br><br>
	</div>
	
	<div>
		Here lists the images you have uploaded:<br>
		<form action="showImages.jsp" method="post" >
	<%
		ImageService imageservice = new ImageService();
		List<imageMode> images = new ArrayList<imageMode>();
		images = imageservice.getUserImage(user.getId());
		Iterator<imageMode> imagesIterator = images.iterator();
		while(imagesIterator.hasNext()){
			String key1 = imagesIterator.next().getKey1();
			String url = "https://s3.amazonaws.com/ece1779bucket/" + key1;
			/* System.out.println(url); */
		/* 	int imageId = imagesIterator.next().getId(); */
	%>
		<input type="image" class="img-thumbnail" name="key1" value=<%=key1%> src='<%=url%>' height=120px width=120px/>
	<%
		}
	%>
	</form>
	</div>
	<br><br>
	<b>You can upload more images here, make sure to input the right id!</b><br /><br />
	<div>
		<form action="FileUpload"  enctype="multipart/form-data" method="post">
		UserID	<input id="user" type="text" name="userID" ><br /><br />
	   	What is the image files to upload? <br /><input type="file" name="theFile" accept="image/*">
	   	<input id="send" type="submit" value="Send">
		<input id="reset" type="reset">
	    </form>
    </div>
    <br><br>
    
    <%
    	} 
    %>
</body>
</html>