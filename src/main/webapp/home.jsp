<%
	if (session.getAttribute("email") == null) {
		response.sendRedirect("login.jsp");
 		final String email = (String)session.getAttribute("email");
 		final String name = (String)session.getAttribute("name");
 		final String role = (String)session.getAttribute("role");
	}
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

.mainnav a.split1 {
  float: right;
  background-color: #04AA6D;
  color: white;
}

.mainnav a.active {
	background-color: #04AA6D;
    color: white;
}
.text-center {
    margin: 0;
    padding:0;
    text-align: center;
    position: absolute;
    top: 50%;
    left:50%;
    transform: translateX(-50%) translateY(-50%);
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
					out.print("<a href=" + request.getContextPath() + "/supplier/list>Suppliers</a>");
					out.print("<a href=" + request.getContextPath() + "/user/list>Employees</a>");
				}
			%>
			<a href="<%=request.getContextPath()%>/item/list">Items</a>
			<a href="<%=request.getContextPath()%>/report.jsp">Reports</a>
			<a href="<%=request.getContextPath()%>/leave/list">Leave Requests</a>
			<a href="<%=request.getContextPath()%>/shift/list">Shift Requests</a>
			<a href="<%=request.getContextPath()%>/task/list">Tasks</a>
			<a href="<%=request.getContextPath()%>/logout" class="split">LogOut</a>
			<a href="<%=request.getContextPath()%>/user/update-profile" class="split">UpdateProfile</a>
			<%
				if (role.equals("EMPLOYEE")) {
					String managerName = (String)session.getAttribute("manager_name");
					out.print("<a class='split1'> Your Manager " + managerName + "</a>");
				}
			%>
			
		</div>
</header>

<div class="text-center">
	<h2>Hello ${name},</h2>
    <h1>WELCOME</h1>
    <h1>TO</h1>
    <h1>RETAIL INVENTORY MANAGEMENT SYSTEM</h1>
</div>

</body>
</html>