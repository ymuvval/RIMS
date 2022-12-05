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
	background-color: #E7EEBE;
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
</head>
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
					out.print("<a class='active' href=" + request.getContextPath() + "/supplier/list>Suppliers</a>");
					out.print("<a href=" + request.getContextPath() + "/user/list>Employees</a>");
				}
			%>
			<a href="<%=request.getContextPath()%>/item/list">Items</a>
			<a href="<%=request.getContextPath()%>/report.jsp">Reports</a>
			<a href="<%=request.getContextPath()%>/leave/list">Leave Requests</a>
			<a href="<%=request.getContextPath()%>/shift/list">Shift Requests</a>
			<a href="<%=request.getContextPath()%>/task/list">Tasks</a>
			<a href="<%=request.getContextPath()%>/logout" class="split">LogOut</a>
			<a href="#updateprofile" class="split">UpdateProfile</a>
			<%
				if (role.equals("EMPLOYEE")) {
					String managerName = (String)session.getAttribute("manager_name");
					out.print("<a class='split1'> Your Manager " + managerName + "</a>");
				}
			%>
			
		</div>
</header>
	<br>
	<br>

	<form method="post" action="supplier/edit">
		<div class="container">
			<h1>Edit Supplier</h1>
			<hr>
			<label for="name"><b>Name</b></label>
			<br>
			<input type="text" placeholder="Enter Name" name="name" id="name" required value="${suppliername}">
			<br>
			<label for="name"><b>Phone</b></label>
			<br>
			<input type="text" placeholder="Enter Phone" name="phone" id="phone" required value="${supplierphone}">
			<br>
			<label for="name"><b>Address</b></label>
			<br>
			<input type="text" placeholder="Enter Address" name="address" id="address" required value="${supplieraddress}">
			<br>
			<select class="selectstatus" name="status" id="status">
				  <option
				    	value="ACTIVE"
				    	${"ACTIVE" == supplierstatus ? 'selected="selected"' : ''}
				    >
				        ACTIVE
				  </option>
				  <option
				    	value="DEACTIVATED"
				    	${"DEACTIVATED" == supplierstatus ? 'selected="selected"' : ''}
				    >
				        DEACTIVATED
				  </option>
			</select>
			<hr>
			<input type="hidden" id="id" name="id" value="${supplierid}" />
			<button type="submit" class="create">Edit</button>
		</div>

	</form>


</body>
</html>