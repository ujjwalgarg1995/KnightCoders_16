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
if(!isset($_GET['x']))
{
	header("Location: add.php?x=vol");
} else {
    if($_GET['x']!='vol' && $_GET['x']!='med') {
        header("Location: add.php?x=vol");
    }
}
include_once 'phpelements.php';
$sql="SELECT * FROM area";
$result=$db->query($sql);
$area = array();
while ($row = $result->fetch_assoc()) {
    # code...
    array_push($area, $row);
}
$errorP = false;
$insertC = false;
$unknownE = false;
$alreadyReg = false;
if(isset($_POST['name']) && isset($_POST['dob']) && isset($_POST['phone']) && isset($_POST['job']) && isset($_POST['area_code']))
{
	$name=$_POST['name'];
    $dob=$_POST['dob'];
    $phone=$_POST['phone'];
    $job=$_POST['job'];
    $area_code=$_POST['area_code'];
    $email=$_POST['email'];
    if(strlen($phone)==10 && ($phone['0']=='9' || $phone['0']=='8' || $phone['0']=='7'))
    {
        $sql = "SELECT * FROM user WHERE username='$phone'";
        $result = $db->query($sql);
        if(mysqli_num_rows($result)==0)
        {
            $sql = "INSERT INTO volunteer (name,dob,phone,email,job,area_code,approved) VALUES ('$name','$dob','$phone','$email','$job', $area_code,1)";
            $result3 = $db->query($sql) or die(mysqli_error($db));            
            if($result3) {
                $pwd = md5('1234');
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
if(isset($_POST['mname']) && isset($_POST['mphone']) && isset($_POST['mcompany'])) 
{
    $name=$_POST['mname'];
    $phone=$_POST['mphone'];
    $email=$_POST['memail'];
    $company=$_POST['mcompany'];
    if(strlen($phone)==10 && ($phone['0']=='9' || $phone['0']=='8' || $phone['0']=='7'))
    {
        $sql = "SELECT * FROM user WHERE username='$phone'";
        $result = $db->query($sql);
        if(mysqli_num_rows($result)==0)
        {
            $sql = "INSERT INTO media (name,phone,email,company) VALUES ('$name','$phone','$email','$company')";
            $result3 = $db->query($sql) or die(mysqli_error($db));            
            if($result3) {
                $pwd = md5('123456');
                $sql = "INSERT INTO user (name, username, password, role) VALUES ('$name','$phone','$pwd',2)";
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
?>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>iClean</title>

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
                        <?php if($_GET['x']=='vol') $x = 'Volunteer'; else $x = 'Media Person'; ?>
                        <div class="x_panel"><h2>Add : <?php echo $x;?></h2></div>
                    </div>
                    <div class="row">
                        <div class="x_panel">
                            <div class="x_content">
                                <?php if($errorP) { ?>
                                    <div class="alert alert-danger alert-dismissible fade in" role="alert">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                                        </button>
                                        Invalid phone no, please enter a valid mobile no
                                    </div>
                                    <?php } ?>
                                    <?php if($insertC) { ?>
                                    <div class="alert alert-success alert-dismissible fade in" role="alert">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                                        </button>
                                        <?php if($_GET['x']=='vol') { ?>
                                        Volunteer added successfully.
                                        <?php } else { ?>
                                        Media Person added successfully.
                                        <?php } ?>
                                    </div>
                                    <?php } ?>
                                    <?php if($alreadyReg) { ?>
                                    <div class="alert alert-warning alert-dismissible fade in" role="alert">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                                        </button>
                                        Another member with the same mobile no is already registered, please use another phone no.
                                    </div>
                                    <?php } ?>
                                    <?php if($unknownE) { ?>
                                    <div class="alert alert-warning alert-dismissible fade in" role="alert">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                                        </button>
                                        Something went wrong please try after sometime.
                                    </div>
                                    <?php } ?>
                                    <?php if($_GET['x']=='vol') { ?>
                                <form id="addVolForm" action=<?php if($_GET['x']=='vol') { echo "add.php?x=vol"; } else { echo "add.php?x=med"; }?>  method="POST" data-parsley-validate class="form-horizontal form-label-left"> 
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Name <span class="required">*</span>
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input id="name" class="form-control col-md-7 col-xs-12" data-validate-length-range="3" data-validate-words="2" name="name" placeholder="Full Name (required)" required type="text">
                                            </div>
                                        </div>
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" required="required" for="dob">Date of Birth <span class="required">*</span>
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" class="form-control active" id="single_cal1" name="dob" placeholder="Date of Birth" aria-describedby="inputSuccess2Status">
                                            </div>
                                        </div>
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="phone">Phone <span class="required">*</span>
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="tel" id="phone" name="phone" required placeholder="Phone" class="form-control col-md-7 col-xs-12">
                                            </div>
                                        </div>
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="email">Email
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="email" id="email" name="email" placeholder="Email" class="form-control col-md-7 col-xs-12">
                                            </div>
                                        </div>                                        
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="job">Job</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <select id = "job" name= "job" class="form-control" required="">
                                                    <option value="Service">Service </option>
                                                    <option value="Student">Student </option>
                                                    <option value="House Wife">House Wife </option>
                                                    <option value="Business">Business</option>
                                                    <option value="Other">Other</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="area_code">Area/Ward</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <select id = "area_code" name= "area_code" class="form-control" required="">
                                                    <?php foreach($area as $a) { ?> 
                                                    <option value=<?php echo '"'.$a['area_code'].'"';?>><?php echo $a['area_name']; ?> </option>
                                                    <?php } ?>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="ln_solid"></div>
                                        <div class="form-group">
                                            <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                                <button type="submit" class="btn btn-success">Submit</input>
                                            </div>
                                        </div>
                                    </form>
                                    <?php } else if($_GET['x']=='med') { ?>
                                    <form id="addVolForm" action=<?php if($_GET['x']=='vol') { echo "add.php?x=vol"; } else { echo "add.php?x=med"; }?>  method="POST" data-parsley-validate class="form-horizontal form-label-left">
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="mname">Name <span class="required">*</span>
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input id="mname" class="form-control col-md-7 col-xs-12" data-validate-length-range="3" data-validate-words="2" name="mname" placeholder="Full Name (required)" required type="text">
                                            </div>
                                        </div>
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="mphone">Phone <span class="required">*</span>
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="tel" id="mphone" name="mphone" required placeholder="Phone" class="form-control col-md-7 col-xs-12">
                                            </div>
                                        </div>
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="memail">Email 
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="email" id="memail" name="memail" placeholder="Email" class="form-control col-md-7 col-xs-12">
                                            </div>
                                        </div>                                        
                                        <div class="item form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="mcompany">Organisation <span class="required">*</span>
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="mcompany" id="mcompany" name="mcompany" placeholder="Organization" required class="form-control col-md-7 col-xs-12">
                                            </div>
                                        </div>                                        
                                        <div class="ln_solid"></div>
                                        <div class="form-group">
                                            <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                                <button type="submit" class="btn btn-success">Submit</input>
                                            </div>
                                        </div>
                                    </form>
                                    <?php }?>
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
    <?php datepicker(); ?>
</body>

</html>