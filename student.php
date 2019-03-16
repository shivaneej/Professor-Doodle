<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

		<style>
			

			.split 
			{
  				height: 100%;
  				width: 50%;
  				position: fixed;
				z-index: 1;
				top: 0;
				overflow-x: hidden;
				padding-top: 20px;
			}

/* Control the left side */
			.left 
			{
			  left: 0;
			  /*background-color: #111;*/
			}

/* Control the right side */
			.right {
			  right: 0;
			  /*background-color: red;*/
			}

			

			/* If you want the content centered horizontally and vertically */
			.centered {
			  position: absolute;
			  top: 50%;
			  left: 50%;
			  transform: translate(-50%, -50%);
			  text-align: center;
			}

			/* Style the image inside the centered container, if needed */
			.centered img {
			  width: 150px;
			  border-radius: 50%;
			}

		</style>
		<title>PDoodle</title>
	</head>
	<body>

		<div class="split left">
  			<div class="centered">
    			<form method="POST" action="#">
    				<h4>Login</h4>
  					<div class="form-group-1">
    					<label for="loginEmail">Email</label>
    					<input type="email" name="loginEmail" class="form-control" id="loginEmail" aria-describedby="emailHelp" placeholder="Enter email">
    					<small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
  					</div>
 					<div class="form-group">
    					<label for="loginPassword">Password</label>
    					<input type="password" name="loginPassword" class="form-control" id="loginPassword" placeholder="Password">
  					</div>
  					<button type="submit" name="Login" class="btn btn-primary">Submit</button>
				</form>
  			</div>
		</div>

		<div class="split right">
  			<div class="centered">
    			<form method="POST" action="#">
    				<h4>Sign Up</h4>
  					<div class="form-group-2">	
    					<label for="fn">First Name</label>
    					<input type="name" name="fn" class="form-control" id="fn" aria-describedby="emailHelp" placeholder="Enter First Name">
  					</div>
  					<div class="form-group">	
    					<label for="ln">Last Name</label>
    					<input type="name" name="ln" class="form-control" id="ln" aria-describedby="emailHelp" placeholder="Enter Last Name">
  					</div>
  					<div class="form-group">	
    					<label for="regEmail">Email</label>
    					<input type="name" name="regEmail" class="form-control" id="regEmail" aria-describedby="emailHelp" placeholder="Enter email">
    					<small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
  					</div>
 					<div class="form-group">
    					<label for="regPwd">Password</label>
    					<input type="password" name="regPwd" class="form-control" id="regPwd" placeholder="Password">
  					</div>
  					<div class="form-group">
    					<label for="cnfPwd">Confirm Password</label>
    					<input type="password" name="cnfPwd" class="form-control" id="cnfPwd" placeholder="Password">
  					</div>
  					<button type="submit" name="Reg" class="btn btn-primary">Submit</button>
				</form>

  			</div>
	</div>


	<?php
		require 'vendor/autoload.php';

		$manager = (new MongoDB\Client)->newlol;
		$collection = $manager->testing;




		if (isset($_POST['Reg'])) 
		{
			# code...
			$fn= $_POST['fn'];
			$ln= $_POST['ln'];
			$email2= $_POST['regEmail'];
			$pass2= $_POST['regPwd'];
			$cn= $_POST['cnfPwd'];
			if ($cn==$pass2) 
			{
				$insertOneResult = $collection->insertOne([
		    	'First Name' => $fn,
		    	'Last Name' => $ln,
		   		'Email' => $email2,
		   		'Password' => $pass2,
				]);
				session_start();
				$_SESSION['stuEmail']=$email2;
				$_SESSION['stuPwd']=$pass2;			
				$_SESSION['status']="loggedin";
				echo "<script type='text/javascript'>alert('Registration Successful');</script>";
				header("teacher.php");	
			}	 
			else
			{
				echo '<script>alert("Passwords dont match");</script>';
			}

	
		}
		if(isset($_POST['Login']))
		{
			

			$loginEmail=$_POST['loginEmail'];
		    $pass1=$_POST['loginPassword'];
		    $cursor = $collection->findOne(array('Email'=>$loginEmail));
		    if($cursor["Password"]==$pass1)			
		    {
					session_start();
					$_SESSION['stuEmail']=$loginEmail;
					$_SESSION['stuPwd']=$pass1;			
					$_SESSION['status']="loggedin";
					echo "<script type='text/javascript'>alert('Login Successful'); window.location('teacher.php');</script>"; 

			}
			else			
			{
		    	echo "<script type='text/javascript'>alert('Invalid Login Credentials');</script>";      
			}

		}

	?>
	</body>
</html>
