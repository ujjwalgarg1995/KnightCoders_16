<?php
require 'includes/connect.php';

	$responce=array();
	
	$volid = $_POST['volid'];
	$complain_id = $_POST['complain_id'];
	$sql = "UPDATE 	complaint SET status=4 WHERE volid= $volid where id=$complain_id";
    $result = $db->query($sql);
	if(mysqli_num_rows($result)==1){
		
		$responce['error']=false;
		$responce['message']='Complain Alloted';
		
	} else {
		$responce['error']=true;
		$responce['message']="Sorry unable to allot the complaint";
	}
	echo json_encode($responce);

?>