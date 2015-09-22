<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ece1779.ec2.mode.imageMode" %>
<%@ page import="ece1779.ec2.service.ImageService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Images</title>

<!-- Stylesheets -->
<link rel="stylesheet" href="styles/project.css">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
</head>
<body>
	<%
		String key1 = request.getParameter("key1");
		ImageService imageservice = new ImageService();
		imageMode image = imageservice.getImage(key1);
		String url1 = "https://s3.amazonaws.com/ece1779bucket/" + image.getKey1();
		String url2 = "https://s3.amazonaws.com/ece1779bucket/" + image.getKey2();
		String url3 = "https://s3.amazonaws.com/ece1779bucket/" + image.getKey3();
		String url4 = "https://s3.amazonaws.com/ece1779bucket/" + image.getKey4();
	%>
	
	<h2>Here shows the original and transformed images</h2>
	<img src=<%=url1 %> height=150px width=150px class="img-rounded">
	<img src=<%=url2 %> height=150px width=150px class="img-rounded">
	<img src=<%=url3 %> height=150px width=150px class="img-rounded">
	<img src=<%=url4 %> height=150px width=150px class="img-rounded">
	<br><br>
	<form><input Type="button" VALUE="Back" onClick="history.go(-1);return true;"></form>
</body>
</html>