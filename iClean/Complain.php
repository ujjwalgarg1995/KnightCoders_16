<?php
 
// Path to move uploaded files
require 'includes/connect.php';
$sql = "SELECT * FROM complaint";
$result = $db->query($sql) or die(mysqli_error($db));
while($row = $result->fetch_assoc()){
    $p=$row['photo'];
    echo '<pre>'.$row['id'].' '.$row['complain'].'   '.$row['address'].'   '.$row['area_code'].'   '.$row['mobile_no'].'   '.$row['time']."</pre><img src='uploads/".$p."'/><br>";
} 
// Echo final json response to client
?>
