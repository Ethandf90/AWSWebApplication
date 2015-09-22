<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Login Page</title>
<script>
function myFunction() {
    document.getElementById('demo').style.display = "block";
document.getElementById('demo1').style.display = "none";
}
function myFunction1() {
    document.getElementById('demo').style.display = "none";
document.getElementById('demo1').style.display = "block";
}
</script>

<!-- Stylesheets -->
<link rel="stylesheet" href="styles/project.css">

</head>
<body>
<button type="button" onclick="myFunction()">Login</button>
<button type="button" onclick="myFunction1()">Register</button>

<div  id="demo" class="container">
<div class="form-bg">


<form action="userLogin" method="post">
<h2>Login</h2>
	<p><input type="text" placeholder="user ID" name="login" /></p>
	<p><input type="password" placeholder="password" name="password" /></p>
					<input type="hidden" name="tag" value="old">
	<input type="submit" value="Login"/>
	<input type="reset" value="Reset"/>
</form> 

</div>
</div>

<div id="demo1" hidden class="container">
<div class="form-bg1">



<form action="userLogin" method="post">
<h2>Create a new Account</h2>
	<p><input type="text" placeholder="user ID" name="login" /></p>
	<p><input type="password" placeholder="password" name="password" /></p>
	<p><input type="password" placeholder="password" name="Rpassword" /></p>
	<input type="hidden" name="tag" value="new">
	<br /><input type="submit" value="Register"/>
	<input type="reset" value="Reset" />

</form>
</div>
</div>

</body>
</html>