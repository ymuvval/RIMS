<!DOCTYPE html>

<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>RIMS</title>

<!-- Font Icon -->

<!-- Main css -->
<!-- <link rel="stylesheet" href="css/style.css"> -->
<style>
body {font-family: Arial, Helvetica, sans-serif;}

input[type=text], input[type=password] {
  border-radius: 25px;
  width: 25%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid green;
  box-sizing: border-box;
}

button {
  border-radius: 25px;
  background-color: #04AA6D;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 25%;
}

button:hover {
  opacity: 0.8;
}

.container {
  padding: 16px;
}

span.psw {
  float: center;
  padding-top: 16px;
}

</style>
</head>
<body>

<input type="hidden" id="status" value="<%= request.getAttribute("status") %>">
<input type="hidden" id="loginErr" value="<%= request.getAttribute("loginErr") %>">

	<div>
		<section>
			<div class="container">
				<div>

					<div>
						<h2 align="center">Login</h2>
						<span style="color: red;">${loginError}</span>
						<form align="center" method="post" action="login">
							<div>
								<label for="username"></label>
								<input
									type="text" name="username" id="username"
									placeholder="Your Name" />
							</div>
							<div >
								<label for="password"></label> <input
									type="password" name="password" id="password"
									placeholder="Password" />
							</div>
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
							<div>
								<button type="submit">Login</button>
							</div>
							<div>
								<a href="forgot-pass.jsp">Forgot Password</a>
								<br>
								<a href="signup.jsp">Create an account</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</section>

	</div>

	<!-- JS -->
	<script src="vendor/jquery/jquery.min.js"></script>
	<script src="js/main.js"></script>
</body>
<!-- This templates was made by Colorlib (https://colorlib.com) -->
	<script type="text/javascript">
		var status = document.getElementById("status").value;
		if (status == "failed") {
			swal("Sorry", "Wrong username and password or user not found", "failed");
			throw new ServletException("Mandatory Parameter missing");
		}
	</script>
</html>