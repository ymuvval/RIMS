<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>RIMS</title>

<!-- Font Icon -->
<link rel="stylesheet"
	href="fonts/material-icon/css/material-design-iconic-font.min.css">

<!-- Main css -->
<!-- <link rel="stylesheet" href="css/style.css"> -->
</head>
<style>
body {font-family: Arial, Helvetica, sans-serif;}
/* form {border: 3px solid #f1f1f1;} */

input[type=text], input[type=password] {
  border-radius: 25px;
  width: 33%;
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
  width: 33%;
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
<body>

<input type="hidden" id="status" value="<%= request.getAttribute("status") %>>">
	<div>
		<!-- Sign up form -->
		<section>
			<div class="container">
				<div>
					<div>
						<h2 align="center">Sign up</h2>
					
						<form align="center" method="post" action="signup">
							<div>
								<label for="name"></label>
								<input type="text" name="name" id="name" placeholder="Your Name" />
							</div>
							<div>
								<label for="email"></label>
								<input type="text" name="email" id="email" placeholder="Your Email" />
							</div>
							<div>
								<label for="name"></label>
								<input type="text" placeholder="Enter Question" name="question" id="question" required>
							</div>
							<div>
								<label for="name"></label>
								<input type="text" placeholder="Enter Answer" name="answer" id="answer" required>
							</div>
							<div class="form-group">
								<label for="pass"></label>
								<input type="password" name="pass" id="pass" placeholder="Password" />
							</div>
							<div>
								<label for="re-pass"></label>
								<input type="password" name="re_pass" id="re_pass"
									placeholder="Repeat your password" />
							</div>
							<div>

								<button type="submit">Register</button>
							</div>
							<div>
								<a href="login.jsp">I am already member </a>
							</div>
						</form>
						
					</div>
				</div>
			</div>
		</section>


	</div>
	<script type="text/javascript">
		var status = document.getElementById("status").value;
		if (status == "failed") {
			swal("Sorry", "Wrong username and password", "failed");
		}
	</script>
</body>
</html>
	<!-- JS -->
	<!-- <script src="vendor/jquery/jquery.min.js"></script>
	<script src="js/main.js"></script> -->



