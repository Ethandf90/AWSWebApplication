<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manager Login</title>

<!-- Stylesheets -->
<link rel="stylesheet" href="styles/project.css">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
</head>
<body>
	<h3>Welcome back Manager</h3>
	
	<div class="form-bg">
	<form action="ManagerLogin" method="post">
	<h2>Login</h2>
	<input type="text" placeholder="ID: ece1779" name="managerID"><br /><br />
	<input type="password" placeholder="Pwd: ece1779" name="managerPassword"><br /><br />
			<input type="submit" value="Login">
			<input type="reset" value = "Reset">
	</form>
	</div>
</body>
</html>