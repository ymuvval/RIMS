<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>RIMS</title>
<style>
body {
	margin: 0;
	font-family: Arial, Helvetica, sans-serif;
}

.mainnav {
	overflow: hidden;
	background-color: #333;
}

.mainnav a {
	float: left;
	color: #f2f2f2;
	text-align: center;
	padding: 14px 16px;
	text-decoration: none;
	font-size: 17px;
}

.mainnav a:hover {
	background-color: #ddd;
	color: black;
}

.mainnav a.split {
	float: right;
	background-color: #04AA6D;
	color: white;
}

.mainnav a.active {
	background-color: #04AA6D;
	color: white;
}

body {
	font-family: Arial, Helvetica, sans-serif;
	background-color: white;
}

* {
	box-sizing: border-box;
}

.container {
	padding: 16px;
	background-color: white;
}

input[type=text] {
	width: 50%;
	padding: 15px;
	margin: 5px 0 22px 0;
	display: inline-block;
	border: none;
	background: #f1f1f1;
}

input[type=text]:focus{
	background-color: #ddd;
	outline: none;
}

input[type=password] {
	width: 50%;
	padding: 15px;
	margin: 5px 0 22px 0;
	display: inline-block;
	border: none;
	background: #f1f1f1;
}

input[type=password]:focus{
	background-color: #ddd;
	outline: none;
}

hr {
	border: 1px solid #f1f1f1;
	margin-bottom: 25px;
}

.create {
	background-color: #04AA6D;
	color: white;
	padding: 16px 20px;
	margin: 8px 0;
	border: none;
	cursor: pointer;
	width: 25%;
	opacity: 0.9;
}

.create:hover {
	opacity: 1;
}

a {
	color: dodgerblue;
}
.login {
	background-color: #04AA6D;
	color: white;
	padding: 14px 20px;
	margin: 18px 0;
	border: none;
	cursor: pointer;
	width: 25%;
	opacity: 0.9;
}
.login:hover {
	opacity: 1;
}

</style>
</head>
<body>
<%-- <header>
		<div class="mainnav">
			<%
				String role = (String)session.getAttribute("role");
				if (role.equals("MANAGER")) {
					out.print("<a class='active' href='home.jsp'>MANAGER</a>");
				} else {
					out.print("<a class='active' href='home.jsp'>EMPLOYEE</a>");
				}
			%>
			<a href="<%=request.getContextPath()%>/order/list">Orders</a>
			<a href="<%=request.getContextPath()%>/category/list">Categories</a>
			<a href="suppliers.jsp">Suppliers</a>
			<%
				if (role.equals("MANAGER")) {
					out.print("<a class='active' href=" + request.getContextPath() + "/user/list>Subordinates</a>");
				}
			%>
			<a href="<%=request.getContextPath()%>/item/list">Items</a>
			<a href="<%=request.getContextPath()%>/report.jsp">Reports</a>
			<a href="<%=request.getContextPath()%>/leave/list">Leave Requests</a>
			<a href="<%=request.getContextPath()%>/shift/list">Shift Requests</a>
			<a href="<%=request.getContextPath()%>/logout" class="split">LogOut</a>
			<a href="#updateprofile" class="split">UpdateProfile</a>
			<%
				if (role.equals("EMPLOYEE")) {
					String managerName = (String)session.getAttribute("manager_name");
					out.print("<a class='split1'> Your Manager " + managerName + "</a>");
				}
			%>
			
		</div>
</header> --%>
	<br>
	<br>
	<form method="post" action="user/forgot-pass">
		<div class="container">
			<h1>Forgot Password</h1>
			<hr>
				<label for="name"><b>Email</b></label>
			<br>
				<input type="text" placeholder="Enter Email" name="email" id="email" required="required">
			<br>
			<br>
				<label for="name"><b>Question</b></label>
			<br>
				<input type="text" placeholder="Enter Question" name="question" id="question" required="required">
			<br>
				<label for="name"><b>Answer</b></label>
			<br>
				<input type="text" placeholder="Enter Answer" name="answer" id="answer" required>
			<br>
				<label for="name"><b>Password</b></label>
			<br>
				<input type="password" placeholder="Enter New Password" name="password" id="password" required>
			<br>
			<hr>
			<%
				String err = (String) request.getAttribute("error");
				String message = (String) request.getAttribute("message");
				if (err != null && !err.equals("")) {
					out.print("<text style='color:red'>" + "   " + err + "</text>");
				}
				if (message != null && !message.equals("")) {
					out.print("<text style='color:green'>" + "   " + message + "</text>");
				}
			%>
			<br>

			<button type="submit" class="create">Update Password</button>
			<a class="login" href="<%=request.getContextPath()%>/login.jsp">Log In</a>
		</div>

	</form>


</body>
	<script type="text/javascript">
		var error = document.getElementById("status").value;
		if (error != "" || error != null) {
			swal("Sorry", "Wrong username and password or user not found", "failed");
			throw new ServletException("Mandatory Parameter missing");
		}
	</script>
</html>