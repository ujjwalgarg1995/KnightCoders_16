<?php
require 'includes/connect.php';
if(isset($_POST['mobileNo']))
{
	$mobileNo = $_POST['mobileNo'];
	$sql = "SELECT id, status,comment FROM complaint WHERE mobile_no = '$mobileNo'";
	$result = $db->query($sql) or die(mysqli_error($db));
	$res = array();
	while($row = $result->fetch_assoc())
	{
		array_push($res, $row);
	}
	if (empty($res)) {
		# code...
		echo "no";
	}
	else
		echo json_encode($res);
}
?>