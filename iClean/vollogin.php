<?php
require 'includes/connect.php';

	$responce=array();
	
	$username = $_POST['username'];
	$password = md5($_POST['password']);
	$sql = "Select * from user where username = '$username' and password = '$password'";
	$result=$db->query($sql);
	if(mysqli_num_rows($result)==1){
		$sql = "Select * from volunteer where mobile = '$username'";
		$result2=$db->query($sql);
		while($row=$result2->fetch_array(MYSQLI_ASSOC)) {
		$responce['error']=false;
		$responce['volid']=$row['id'];
		}
	} else {
		$responce['error']=true;
		$responce['message']="Sorry unable to login, please try again later";
	}
	echo json_encode($responce);

?>