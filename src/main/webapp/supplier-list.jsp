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

.mainnav a.active {
	background-color: #04AA6D;
	color: white;
}

table {
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 100%;
}

td, th {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 8px;
}

tr BUTTON {
	border: 1px solid #dddddd;
	color: black;
	padding: 15px 32px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	width: 25%;
	cursor: pointer;
	margin-right: 12.5%;
}

.createcategory {
	color: white;
	background-color: #04AA6D;
	padding: 14px 25px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	width: 10%;
	margin-left: 2%;
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
			<a href="<%=request.getContextPath()%>/user/update-profile" class="split">UpdateProfile</a>
			<%
				if (role.equals("EMPLOYEE")) {
					String managerName = (String)session.getAttribute("manager_name");
					out.print("<a class='split1'> Your Manager " + managerName + "</a>");
				}
			%>
			
		</div>
</header>
	<br>
	<div>
		<a class="createcategory"
			href="<%=request.getContextPath()%>/supplier-create.jsp">Create</a>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th>Phone</th>
					<th>Address</th>
					<th>Status</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach var="supplier"
					items="<%=request.getAttribute(\"suppliers\")%>">

					<tr>
						<td><c:out value="${supplier.id}" /></td>
						<td><c:out value="${supplier.name}" /></td>
						<td><c:out value="${supplier.phone}" /></td>
						<td><c:out value="${supplier.address}" /></td>
						<td><c:out value="${supplier.status}" /></td>
						<td>
						<a
							href="<%=request.getContextPath()%>/supplier/edit?id=<c:out value='${supplier.id}' />&name=<c:out value='${supplier.name}' />&phone=<c:out value='${supplier.phone}' />&address=<c:out value='${supplier.address}' />">
								Edit
						</a> &nbsp;&nbsp;&nbsp;&nbsp;
						<a
							href="<%=request.getContextPath()%>/supplier/delete?id=<c:out value='${supplier.id}' />">
								Delete
						</a>
						</td>
					</tr>
				</c:forEach>

			</tbody>

		</table>
	</div>
	<br>
	<br>
</body>
</html>

