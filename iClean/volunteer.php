<?php
session_start();
require 'includes/connect.php';
if(!isset($_SESSION['name']))
{
    header("Location: login.php");
}
if($_SESSION['role']==2){
        header("Location: index.php");
}


include_once 'phpelements.php';
$delete=false;
if(isset($_POST['del'])) {
    $id = $_POST['del'];
    $sql="SELECT phone FROM volunteer WHERE id=$id";
    $result=$db->query($sql);
    if(mysqli_num_rows($result)==1) {
        $res = $result->fetch_assoc();
        $phone = $res['phone'];
        $sql="DELETE FROM user WHERE username='$phone'";
        if($result=$db->query($sql)) {
            $sql="DELETE FROM volunteer WHERE phone='$phone'";
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
if(!isset($_GET['area_code'])) {
    $sql = "SELECT * FROM volunteer where approved = 1 ORDER BY area_code";
} else {
    if($_GET['area_code']==0){
        $sql = "SELECT * FROM volunteer where approved = 1 ORDER BY area_code";
    } else {
        $ac = $_GET['area_code'];
        $sql = "SELECT * FROM volunteer WHERE area_code = $ac and approved = 1";
    }
}
$result = $db->query($sql);
$volunteer = array();
$volcount = mysqli_num_rows($result);
if($volcount>0) {
    while ($row=$result->fetch_assoc()){
        array_push($volunteer, $row);
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

    <title>IClean | Volunteer</title>

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
                        <?php $a = 'All'; if(isset($_GET['area_code']) && $_GET['area_code']!=0) $a=$area[$_GET['area_code']]; ?>
                        <div class="x_panel">
                            <div class="col-sm-6 col-xs-12"><h2>Complains: <?php echo $a; ?></h2></div>
                            <div class="col-sm-6 col-xs-12 pull-right">
                                <div class="animated flipInY col-md-offset-6 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-blue">
                                    <div class="icon"><i class="fa fa-group"></i>
                                    </div>
                                    <div class="count"><?php echo $volcount;?></div>

                                    <h4>No of Volunteers</h4>
                                </div>
                            </div>
                            </div>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <div class="row">
                        <div class="x_panel">
                            <form class="form-horizontal form-label-left" action="volunteer.php" method="GET">
                                <div class="item form-group">
                                    <label class="col-md-3 col-sm-3 col-xs-12 text-right" for="area_code">Area 
                                    </label>
                                    <div class="text-right col-md-6 col-sm-6 col-xs-12">
                                        <select id = "area_code" name= "area_code" class="form-control" required="">
                                            <option value="0"> All </option>
                                            <?php foreach($areaArray as $a) { ?> 
                                            <option value=<?php echo '"'.$a['area_code'].'"';?>><?php echo $a['area_name']; ?> </option>
                                            <?php } ?>
                                        </select>
                                    </div>
                                    <div class="col-md-3 col-sm-3 col-xs-12">
                                        <button type="submit" class="btn btn-success">Submit</input>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="row">
                        <?php if($delete) { ?>
                        <div class="alert alert-warning alert-dismissible fade in" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                            </button>
                            Volunteer deleted successfully.
                        </div>
                        <?php } ?>
                        <?php foreach ($volunteer as $v) { ?>
                        <div class="col-md-4 col-sm-6 col-xs-12 animated fadeInDown">
                            <div class="well profile_view">
                                <div class="col-sm-12">
                                    <div class="col-xs-12">
                                        <div class="col-xs-12 text-right"><?php echo $area[$v['area_code']];?><span> </span><i class="fa fa-map-marker red"></i></div>
                                    </div>
                                    <div class="left col-xs-8">
                                        <h4>Name:<?php echo $v['name'];?></h4>
                                        <ul class="list-unstyled">
                                            <li><i class="fa fa-phone"></i> : <?php echo $v['phone'];?></li>
                                            <li><i class="fa fa-envelope"></i> : <?php echo $v['email'];?></li>
                                            <li><i class="fa fa-suitcase"></i> : <?php echo $v['job']?></li>
                                        </ul>
                                    </div>
                                    <div class="right col-xs-4 text-center">
                                        <img src="images/user.png" alt="" class="img-thumbnail img-responsive">
                                    </div>
                                    <?php if($_SESSION['role']==1)  { ?>
                                    <div class="col-xs-12 text-right">
                                        <?php $url='volunteer.php'; if(isset($_GET['area_code']) && $_GET['area_code']!=0) $url=$url.'?area_code='.$_GET['area_code']; ?>
                                        <form action=<?php echo $url; ?>  method="POST" data-parsley-validate class="form-horizontal form-label-left">
                                            <input type="hidden" name="del" value=<?php echo '"'.$v['id'].'"';?> />
                                            <button type="submit" class="btn btn-danger btn-sm"> Remove </button>
                                        </form>
                                    </div>
                                    <?php } ?>
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