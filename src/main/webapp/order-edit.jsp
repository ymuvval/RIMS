<%@ page import = "java.io.*,java.util.*" %>
<%@page import="model.Invoice"%>
<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="model.Category"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

.container {
	padding: 16px;
	background-color: white;
}

input[type=text] {
	width: 15%;
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
	padding: 15px;
	margin: 8px 0;
	border: none;
	cursor: pointer;
	width: 10%;
	opacity: 0.9;
}

.create:hover {
	opacity: 1;
}

a {
	color: dodgerblue;
}

.delete {
	background-color: #04AA6D;
	color: white;
	padding: 15px;
	margin: 8px 0;
	border: none;
	cursor: pointer;
	width: 10%;
	opacity: 0.9;
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
			<a class="active" href="<%=request.getContextPath()%>/order/list">Orders</a>
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

	<form method="post" action="order/edit">
		<div class="container">
			<h1>Edit Item</h1>
				<label for="name"><b>Name</b></label>
				<input type="text" placeholder="Enter Type" name="name" id="name" required value="<%=request.getAttribute("ordername") %>">

				<label for="name"><b>Phone</b></label>
				<input type="text" placeholder="Enter Type" name="phone" id="phone" required value="<%=request.getAttribute("orderphone") %>">
				
				<label for="name"><b>Discount</b></label>
				<input type="text" placeholder="Enter Type" name="discount" id="discount" required value="<%=request.getAttribute("orderdiscount") %>">
				
				<input type="hidden" id="id" name="id" value="<%=request.getAttribute("orderid") %>" />
				<input type="hidden" id="invoiceid" name="invoiceid" value="<%=request.getAttribute("invoiceid") %>" />
				
				&nbsp;&nbsp;
				<button type="submit" class="create">Update Order</button>

		</div>

	</form>
	
	<form method="post" action="order/edit-invoice">
		<div class="container">
			<h1>Generate Invoice</h1>
			<%
				Invoice invoice = (Invoice)request.getAttribute("invoice");
				Integer orderid = (Integer)request.getAttribute("orderid");
				out.print("<label for='name'><b>Bill Amount</b></label> ");
				out.print(" <input type='text' placeholder='Generate Invoice' name='bill_amount' id='bill_amount' readonly='readonly' value=" + invoice.getBill_amount() + ">");
				
				out.print(" <label for='name'><b>Discount</b></label> ");
				out.print(" <input type='text' placeholder='Generate Invoice' name='discount' id='discount' readonly='readonly' value=" + invoice.getDiscount() + ">");
				
				out.print(" <label for='name'><b>Final Amount</b></label> ");
				out.print(" <input type='text' placeholder='Generate Invoice' name='final_amount' id='final_amount' readonly='readonly' value=" + invoice.getFinal_bill() + ">");

				out.print("<input type='hidden' id='id' name='id' value=" + orderid + " />");
				out.print("&nbsp;&nbsp;<button type='submit' class='create'>Generate Invoice</button>&nbsp;&nbsp;");
			%>
			<a class="delete" href="<%=request.getContextPath()%>/order/delete-invoice?invoiceid=<c:out value='${invoiceid}'/>">
				Delete Invoice
			</a>

		</div>

	</form>
	<div class="container">
		<h1>Order Items</h1>
	<table class="table table-bordered">
			<thead>
				<tr>
					<th>ID</th>
					<th>Order Id</th>
					<th>Item Id</th>
					<th>Quantity</th>
					<th>Price</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach var="orderitem"
					items="<%=request.getAttribute(\"orderitems\")%>">

					<tr>
						<td><c:out value="${orderitem.id}" /></td>
						<td><c:out value="${orderitem.order_id}" /></td>
						<td><c:out value="${orderitem.item_id}" /></td>
						<td>
							<form method="post" action="order/edit-quantity?id=${orderitem.id}&orderid=<c:out value='${orderitem.order_id}' />&itemid=<c:out value='${orderitem.item_id}' />&price=<c:out value='${orderitem.price}' />">
								<input type="text" name="quantity" id="quantity" required value="${orderitem.quantity}">
								<button type="submit" class="create">update</button>
							</form>
						</td>
						<td><c:out value="${orderitem.price}" /></td>
						<td>
						<a href="<%=request.getContextPath()%>/order/remove-item?orderid=${orderid}&id=<c:out value='${orderitem.id}' />">
							remove
						</a>
						</td>
					</tr>
				</c:forEach>

			</tbody>

		</table>
	</div>
	
	<div class="container">
	<h1>Items Catalogue</h1>
	<table class="table table-bordered">
			<thead>
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th>Category</th>
					<th>Quantity</th>
					<th>Expiry</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach var="item"
					items="<%=request.getAttribute(\"items\")%>">

					<tr>
						<td><c:out value="${item.id}" /></td>
						<td><c:out value="${item.name}" /></td>
						<td><c:out value="${item.categoryType}" /></td>
						<td><c:out value="${item.quantity}" /></td>
						<td><c:out value="${item.expiry}" /></td>
						<td>
						<form method="post" action="order/add-item?orderid=<c:out value='${orderid}' />&itemid=<c:out value='${item.id}' />&price=<c:out value='${item.price}' />&quantity=1">
							<button type="submit">Add</button>
						</form>
						<%-- <a href="<%=request.getContextPath()%>/order/add-item?orderid=<c:out value='${orderid}' />&itemid=<c:out value='${item.id}' />&price=<c:out value='${item.price}' />&quantity=1">
							Add
						</a> --%>

						</td>
					</tr>
				</c:forEach>

			</tbody>

		</table>
	</div>

</body>
</html>