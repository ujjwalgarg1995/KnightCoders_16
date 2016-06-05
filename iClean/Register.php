<?php
require 'includes/connect.php';
if(isset($_POST['name']) && isset($_POST['mobile_no'])) 
{
	$responce=array();
	$name = $_POST['name'];
	$mobile_no = $_POST['mobile_no'];
	$email = $_POST['email'];
	$sql = "INSERT INTO appuser (name,mobile_no) VALUES ('$name','$mobile_no')";
	$result=$db->query($sql);
	if($result){
		$responce['error']=true;
		$responce['message']="Successfully registered";
	} else {
		$responce['error']=false;
		$responce['message']="Sorry unable to registered, please try again later";
	}
	echo json_encode($responce);
}
?>