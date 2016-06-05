<?php
session_start();
require 'includes/connect.php';
if(!isset($_SESSION['name']))
{
    header("Location: login.php");
}
if($_SESSION['role']!=1){
        header("Location: index.php");
}
if(isset($_POST['volreq'])){
	$volreqid = $_POST['volreqid'];
	 $sql = "UPDATE volunteer SET approved=1 WHERE id= $volreqid";
        $result = $db->query($sql);
       $sql = "select * from volunteer WHERE id= $volreqid";
        $result2 = $db->query($sql);
	   while($row=$result2->fetch_array(MYSQLI_ASSOC)){
		   $name = $row['name'];
		   $username = $row['phone'];
		   $pwd = md5('1234');
			$sql = "INSERT INTO user (name, username, password, role) VALUES ('$name','$username','$pwd',3)";
            $result2 = $db->query($sql) or die(mysqli_error($db));
			if($result2)
			header("Location: volunteer.php");
		}
		
    
	
    
    if(strlen($phone)==10 && ($phone['0']=='9' || $phone['0']=='8' || $phone['0']=='7'))
    {
        $sql = "SELECT * FROM user WHERE username='$phone'";
        $result = $db->query($sql);
        if(mysqli_num_rows($result)==0)
        {
            $sql = "INSERT INTO volunteer (name,dob,phone,email,job,area_code) VALUES ('$name','$dob','$phone','$email','$job', $area_code)";
            $result3 = $db->query($sql) or die(mysqli_error($db));            
            if($result3) {
                $pwd = md5('123456');
                $sql = "INSERT INTO user (name, username, password, role) VALUES ('$name','$phone','$pwd',3)";
                $result2 = $db->query($sql) or die(mysqli_error($db));
                if($result2)
                {
                    $insertC = true;
                } else {
                    $unknownE = true;
                }
            } else {
                $unknownE = true;
            }
        } else {
            $alreadyReg = true;
        }
    } 
    else 
    {
        $errorP = true;
    }




}
include_once 'phpelements.php';
$sql="SELECT * FROM area";
$result=$db->query($sql);
$area = array();
while ($row = $result->fetch_assoc()) {
    # code...
    $id = $row['area_code'];
    $area[$id]=$row['area_name'];
}
$sql="SELECT * FROM volunteer where approved=0 ORDER BY created_at";
$result=$db->query($sql);
$res = array();
$reqcount = mysqli_num_rows($result);
if($reqcount!=0) {
    while ($row = $result->fetch_assoc()) {
        # code...
        array_push($res, $row);
    }
}
?>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>iClean | Volunteer Request</title>

    <!-- Bootstrap core CSS -->

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <link href="fonts/css/font-awesome.min.css" rel="stylesheet">
    <link href="css/animate.min.css" rel="stylesheet">

    <!-- Custom styling plus plugins -->
    <link href="css/custom.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/maps/jquery-jvectormap-2.0.1.css" />
    <link href="css/icheck/flat/green.css" rel="stylesheet">
    <link href="css/floatexamples.css" rel="stylesheet" />

    <script src="js/jquery.min.js"></script>

    <!--[if lt IE 9]>
        <script src="../assets/js/ie8-responsive-file-warning.js"></script>
        <![endif]-->

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

</head>


<body class="nav-md">

    <div class="container body">


        <div class="main_container">

            <?php sidebar();?>

            <!-- top navigation -->
            <?php top_nav();?>
            <!-- /top navigation -->


            <!-- page content -->
            <div class="right_col" role="main">

                <br />
                <div class="sitecontainer" >
                    <div class="row">
                        <div class="col-md-12 col-sm-12 col-xs-12">
                            <div class="x_panel">
                                <div class="x_title">
                                    <h2>Volunteer Request</h2>
                                    <div class="clearfix"></div>
                                </div>
                                <div class="x_content">
                                    <?php if($reqcount==0) { ?>
                                    <div class="bs-example" data-example-id="simple-jumbotron">
                                        <div class="jumbotron">
                                            <h1>Sorry, <?php echo $_SESSION['name'];?></h1>
                                            <p>No request yet!</p>
                                        </div>
                                    </div>
                                    <?php } else { ?>

                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Name</th>
                                                <th>Date of Birth</th>
                                                <th>Mobile No</th>
                                                <th>Email</th>
                                                <th>Job</th>
                                                <th>Ward</th>
                                                <th>Date/Time</th>
                                                <th>Approve</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <?php $count=0; foreach ($res as $r) { ?>
                                            <tr>
                                                <th scope="row"><?php $count++; echo $count;?></th>
                                                <td><?php echo $r['name'];?></td>
                                                <td><?php echo $r['dob'];?></td>
                                                <td><?php echo $r['phone'];?></td>
                                                <td><?php echo $r['email'];?></td>
                                                <td><?php echo $r['job'];?></td>
                                                <td><?php echo $area[$r['area_code']];?></td>
                                                <td><?php echo $r['created_at'];?></td>
                                                <td>
                                                	<form action="req.php" method="post">
                                                    	<input type="hidden" name="volreq" value="true">
                                                        <input type="hidden" name="volreqid" value="<?php echo $r['id'];?>">
                                                        
                                                        <input type="submit" value="Approve">
                                                    </form>
                                                </td>
                                            </tr>
                                            <?php } ?>
                                        </tbody>
                                    </table>
                                    <?php } ?>

                                </div>
                            </div>
                        </div>

                    </div>

                    
                </div>
                <!-- footer content -->
                <?php footer();?>
                <!-- /footer content -->

            </div>
            <!-- /page content -->
        </div>


    </div>

    <div id="custom_notifications" class="custom-notifications dsp_none">
        <ul class="list-unstyled notifications clearfix" data-tabbed_notifications="notif-group">
        </ul>
        <div class="clearfix"></div>
        <div id="notif-group" class="tabbed_notifications"></div>
    </div>

    <script src="js/bootstrap.min.js"></script>
    <script src="js/nicescroll/jquery.nicescroll.min.js"></script>

    <!-- chart js -->
    <script src="js/chartjs/chart.min.js"></script>
    <!-- bootstrap progress js -->
    <script src="js/progressbar/bootstrap-progressbar.min.js"></script>
    <!-- icheck -->
    <script src="js/icheck/icheck.min.js"></script>
    <!-- daterangepicker -->
    <script type="text/javascript" src="js/moment.min2.js"></script>
    <script type="text/javascript" src="js/datepicker/daterangepicker.js"></script>
    <!-- sparkline -->
    <script src="js/sparkline/jquery.sparkline.min.js"></script>

    <script src="js/custom.js"></script>

    <!-- flot js -->
    <!--[if lte IE 8]><script type="text/javascript" src="js/excanvas.min.js"></script><![endif]-->
    <script type="text/javascript" src="js/flot/jquery.flot.js"></script>
    <script type="text/javascript" src="js/flot/jquery.flot.pie.js"></script>
    <script type="text/javascript" src="js/flot/jquery.flot.orderBars.js"></script>
    <script type="text/javascript" src="js/flot/jquery.flot.time.min.js"></script>
    <script type="text/javascript" src="js/flot/date.js"></script>
    <script type="text/javascript" src="js/flot/jquery.flot.spline.js"></script>
    <script type="text/javascript" src="js/flot/jquery.flot.stack.js"></script>
    <script type="text/javascript" src="js/flot/curvedLines.js"></script>
    <script type="text/javascript" src="js/flot/jquery.flot.resize.js"></script>

    <!-- /datepicker -->
</body>

</html>