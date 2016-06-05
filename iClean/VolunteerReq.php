<?php
require 'includes/connect.php';
if(isset($_POST['name']) && isset($_POST['dob']) && isset($_POST['mobile_no']) && isset($_POST['area_code']) && isset($_POST['job'])) 
{
	$responce=array();
	$name = $_POST['name'];
	$dob = $_POST['dob'];
	$mobile_no = $_POST['mobile_no'];
	$email = $_POST['email'];
	$area_code = $_POST['area_code'];
	$job = $_POST['job'];
	$sql = "INSERT INTO volunteer (name,dob,phone,email,job,area_code,approved) VALUES ('$name','$dob','$mobile_no','$email','$job',$area_code,0)";
	$result=$db->query($sql);
	if($result){
		$responce['error']=true;
		$responce['message']="Successfully registered for Volunteer";
	} else {
		$responce['error']=false;
		$responce['message']="Sorry unable to registered for Volunteer, please try again later";
	}
	echo json_encode($responce);
}
?>