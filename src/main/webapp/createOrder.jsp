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

input[type=number], input[type=text] {
	width: 50%;
	padding: 15px;
	margin: 5px 0 22px 0;
	display: inline-block;
	border: none;
	background: #f1f1f1;
}

input[type=text]:focus, input[type=number]:focus {
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

.signin {
	background-color: #f1f1f1;
	text-align: center;
}
</style>


<script type="text/JavaScript">
	function createNewElement() {
		var txtNewInputBox1 = document.createElement('div');
		var txtNewInputBox2 = document.createElement('div');
		var txtNewInputBox3 = document.createElement('div');
		txtNewInputBox1.innerHTML = "<input type='text' id='item' placeholder = 'Enter Item Name'>";
		txtNewInputBox2.innerHTML = "<input type='number' id='price' placeholder = 'Enter Item Price'>";
		txtNewInputBox3.innerHTML = "<input type='number' id='quantity' placeholder = 'Enter Item Quantity'>";
		document.getElementById("dynamicCheck").appendChild(txtNewInputBox1);
		document.getElementById("dynamicCheck").appendChild(txtNewInputBox2);
		document.getElementById("dynamicCheck").appendChild(txtNewInputBox3);
	}
</script>
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

	<form method = "post" action="createOrder">
		<div class="container">
			<h1>Create Order</h1>
			<p>Please fill these order details to create an order.</p>
			<hr>
			<label for="name"><b>Name</b></label> <br> <input type="text"
				placeholder="Enter Name" name="name" id="name" required> <br>
			<label for="phone"><b>Phone</b></label> <br> <input
				type="number" placeholder="Enter Phone number" name="phone"
				id="phone" required> <br> <label for="discount"><b>Discount</b></label>
			<br> <input type="text" placeholder="Enter Discount"
				name="discount" id="discount" required> <br>

			<div id="dynamicCheck" class="dynamicCheck">
				<input class="create" type="button" value="Create Item"
					onclick="createNewElement();" />
				<div id="item"></div>
				<div id="price"></div>
				<div id="quantity"></div>
			</div>



			<hr>

			<button type="submit" class="create">Create</button>
		</div>

	</form>


</body>
</html>