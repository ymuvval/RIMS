<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="model.Category"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- <%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%> --%>
<html>
<head>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
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
.mainnav a.split1 {
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

.selectstatus {
	width: 50%;
	padding: 15px;
	margin: 5px 0 22px 0;
	display: inline-block;
	border: none;
	background: #f1f1f1;
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

.selectstatus {
	width: 20%;
	padding: 15px;
	margin: 5px 0 22px 0;
	display: inline-block;
	border: none;
	background: #f1f1f1;
}

a {
	color: dodgerblue;
}
</style>
<body>
<header>
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
			
			<%
				if (role.equals("MANAGER")) {
					out.print("<a href=" + request.getContextPath() + "/supplier/list>Suppliers</a>");
					out.print("<a href=" + request.getContextPath() + "/user/list>Subordinates</a>");
				}
			%>
			<a href="<%=request.getContextPath()%>/item/list">Items</a>
			<a href="<%=request.getContextPath()%>/report.jsp">Reports</a>
			<a href="<%=request.getContextPath()%>/leave/list">Leave Requests</a>
			<a href="<%=request.getContextPath()%>/shift/list">Shift Requests</a>
			<a href="<%=request.getContextPath()%>/task/list">Tasks</a>
			<a href="<%=request.getContextPath()%>/logout" class="split">LogOut</a>
			<a href="<%=request.getContextPath()%>/user/update-profile" class="split1">UpdateProfile</a>
			<%
				if (role.equals("EMPLOYEE")) {
					String managerName = (String)session.getAttribute("manager_name");
					out.print("<a class='split1'> Your Manager " + managerName + "</a>");
				}
			%>

		</div>
</header>
	<form method="post" action="user/update-profile">
		<div class="container">
			<h1>Update Profile</h1>
			<hr>
				<label for="name"> Name </label>
			<br>
				<input type="text" name="name" id="name" placeholder="Your Name" value="<%=request.getAttribute("username") %>"/>
			<br>
				<label for="email">Email</label>
			<br>
				<input type="text" name="email" id="email" placeholder="Your Email" value="<%=request.getAttribute("useremail") %>"/>
			<br>
				<label for="pass">Password</label>
			<br>
				<input type="password" name="pass" id="pass" placeholder="Password" value="<%=request.getAttribute("userpass") %>"/>
			<br>	
				<label for="shift">Shift</label>
			<br>
				<c:if test="${role=='EMPLOYEE'}">
					<input type="text" name="shift" id="shift" placeholder="Shift" readonly="readonly" value="<%=request.getAttribute("usershift") %>"/>
				</c:if>
				<c:if test="${role=='MANAGER'}">
					<select name="shift" id="shift", class="selectstatus">
					  <option
					    	value="MORNING"
					    	${usershift == 'MORNING' ? 'selected="selected"' : ''}
					    >
					        MORNING
					  </option>
					  <option
					    	value="AFTERNOON"
					    	${usershift == 'AFTERNOON' ? 'selected="selected"' : ''}
					    >
					        AFTERNOON
					  </option>
			    	<option
				    	value="EVENING"
				    	${usershift == 'EVENING' ? 'selected="selected"' : ''}
				    >
				        EVENING
				  	</option>
			    	<option
				    	value="NIGHT"
				    	${usershift == 'NIGHT' ? 'selected="selected"' : ''}
				    >
				        NIGHT
				  	</option>
					</select>
				</c:if>
			<br>
				<label for="name">Question</label>
			<br>
				<input type="text" placeholder="Enter Question" name="question" id="question" required value="<%=request.getAttribute("userquestion") %>">
			<br>
				<label for="name">Answer</label>
			<br>
				<input type="text" placeholder="Enter Answer" name="answer" id="answer" required value="<%=request.getAttribute("useranswer") %>">
			<br>
				<label for="name">Status</label>
			<br>
			<select class="selectstatus" name="status" id="status">
				  <option
				    	value="ACTIVE"
				    	${"ACTIVE" == userstatus ? 'selected="selected"' : ''}
				    >
				        ACTIVE
				  </option>
				  <option
				    	value="DEACTIVATED"
				    	${"DEACTIVATED" == userstatus ? 'selected="selected"' : ''}
				    >
				        DEACTIVATED
				  </option>
			</select>
			<hr>
			<input type="hidden" id="id" name="id" value="<%=request.getAttribute("userid") %>"/>
			<button class="create" type="submit">Update</button>
		</div>

	</form>
	
	<script type="text/javascript">
		var status = document.getElementById("status").value;
		if (status == "failed") {
			swal("Sorry", "Wrong username and password", "failed");
		}
	</script>
</body>
</html>




