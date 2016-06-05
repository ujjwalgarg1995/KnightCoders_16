<?php 
require 'includes/connect.php';
$complains = array();
if(isset($_POST['volid'])){
	$volid = $_POST['volid'];
	$sql="SELECT * FROM volunteer where id = $volid";
    $result=$db->query($sql);
   	while ($row = $result->fetch_array(MYSQLI_ASSOC)) {
    
	$area = $row['area_code'];
	                       
		$sql="SELECT * FROM complaint where status = 2 and volid = 0 and area_code = $area";
    	$result=$db->query($sql);
   		while ($row2 = $result->fetch_array(MYSQLI_ASSOC)) {
			array_push($complains,$row2);
			
		}
	}
	
}

echo json_encode($complains);

?>