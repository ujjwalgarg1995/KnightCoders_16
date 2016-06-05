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

include_once 'phpelements.php';
$delete=false;
if(isset($_POST['del'])) {
    $id = $_POST['del'];
    $sql="SELECT phone FROM media WHERE id=$id";
    $result=$db->query($sql);
    if(mysqli_num_rows($result)==1) {
        $res = $result->fetch_assoc();
        $phone = $res['phone'];
        $sql="DELETE FROM user WHERE username='$phone'";
        if($result=$db->query($sql)) {
            $sql="DELETE FROM media WHERE phone='$phone'";
            if($result=$db->query($sql)) {
                $delete=true;
            }
        }
    }
}
$sql="SELECT * FROM area";
$result=$db->query($sql);
$area = array();
while ($row = $result->fetch_assoc()) {
    # code...
    $id = $row['area_code'];
    $area[$id]=$row['area_name'];
}
$sql="SELECT * FROM area";
$result=$db->query($sql);
$areaArray = array();
while ($row = $result->fetch_assoc()) {
    # code...
    array_push($areaArray, $row);
}
$sql = "SELECT * FROM media";
$result = $db->query($sql);
$media = array();
$medcount = mysqli_num_rows($result);
if($medcount>0) {
    while ($row=$result->fetch_assoc()){
        array_push($media, $row);
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

    <title>iClean | Media Person</title>

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
                        <div class="clearfix"></div>
                        <?php if($delete) { ?>
                        <div class="alert alert-warning alert-dismissible fade in" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                            </button>
                            Media deleted successfully.
                        </div>
                        <?php } ?>
                        <?php foreach ($media as $m) { ?>
                        <div class="col-md-4 col-sm-6 col-xs-12 animated fadeInDown">
                            <div class="well profile_view">
                                <div class="col-sm-12">
                                    <div class="left col-xs-8">
                                        <h4>Name:<?php echo $m['name'];?></h4>
                                        <ul class="list-unstyled">
                                            <li><i class="fa fa-phone"></i> : <?php echo $m['phone'];?></li>
                                            <li><i class="fa fa-envelope"></i> : <?php echo $m['email'];?></li>
                                            <li><i class="fa fa-suitcase"></i> : <?php echo $m['company']?></li>
                                        </ul>
                                    </div>
                                    <div class="right col-xs-4 text-center">
                                        <img src="images/user.png" alt="" class="img-thumbnail img-responsive">
                                    </div>
                                    <div class="col-xs-12 text-right">
                                        <form action="media.php"  method="POST" data-parsley-validate class="form-horizontal form-label-left">
                                            <input type="hidden" name="del" value=<?php echo '"'.$m['id'].'"';?> />
                                            <button type="submit" class="btn btn-danger btn-sm"> Remove </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <?php } ?>
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