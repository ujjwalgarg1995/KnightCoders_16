<?php
 
// Path to move uploaded files
require 'includes/connect.php';
$target_path = "uploads/";
 
// array for final json respone
$response = array();

 
 
if (isset($_POST['mobileNo']) && isset($_POST['complain']) && isset($_POST['address']) && isset($_POST['ward']) && isset($_POST['img64']) && isset($_POST['imgName'])) {


    if (!file_exists($target_path)) {
        mkdir($target_path, 0777, true);
    }
    $img = base64_decode($_POST['img64']);
    $imgName = $_POST['imgName'];
    $fp = fopen($target_path.$imgName, 'w');
    fwrite($fp, $img);               
    
    $mobileNo = $_POST['mobileNo'];
    $complain = $_POST['complain'];
    $address = $_POST['address'];
    $time = $_POST['time'];
    $ward = $_POST['ward'];
    if(fclose($fp)){
        $sql = "INSERT INTO complaint(complain, address, area_code, mobile_no, photo, time) VALUES ('$complain','$address', '$ward', '$mobileNo','$imgName', '$time')";
        $result = $db->query($sql) or die(mysqli_error($db));
        if($result ==  TRUE)
        {
            $id = mysqli_insert_id($db);
            $response['error'] = false;
            $response['complainID'] = $id;
            $response['imgName'] = $imgName;
            $response['status'] = "0";
        }
    } else {
        $response['error'] = "error";
        $response['message'] = "Something went wrong please try after sometime";
    }
 
    // reading other post parameters
} else {
    // File parameter is missing
    $response['error'] = true;
    $response['message'] = 'Not received any file!';
}
 
// Echo final json response to client
echo json_encode($response);
?>
