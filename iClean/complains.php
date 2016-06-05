<?php
session_start();
require 'includes/connect.php';
if(!isset($_SESSION['name']))
{
    header("Location: login.php");
}
if($_SESSION['role']==3){
    if($_SESSION['area_code']!=$_GET['area_code'])
        header("Location: complains.php?area_code=".$_SESSION['area_code']);
}
include_once 'phpelements.php';
$updated=false;
$upCom=false;
$updRj=false;
$updF=false;
$updRe=false;
if(isset($_POST['id']) && isset($_POST['upCom'])) {
    $id = $_POST['id'];
    $com = $_POST['upCom'];
    $sql = "UPDATE complaint SET comment = '$com' WHERE id=$id";
    $upCom = true;
    if($result = $db->query($sql))
    {
        $updated=true;
    }
} else if(isset($_POST['upRj']) || isset($_POST['upF']) || isset($_POST['upRe'])) {
    if(isset($_POST['upRj'])){
        $id=$_POST['upRj'];
        $sql = "UPDATE complaint SET status = 1 WHERE id=$id";
        $updRj=true;
    } else if(isset($_POST['upF'])) {
        $id=$_POST['upF'];
        $sql = "UPDATE complaint SET status = 2 WHERE id=$id";
        $updF=true;
    } else if(isset($_POST['upRe'])) {
        $id=$_POST['upRe'];
        $sql = "UPDATE complaint SET status = 3 WHERE id=$id";
        $updRe=true;
    }
    if($result = $db->query($sql))
    {
        $updated=true;
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
$url='complains.php?';
if(isset($_GET['area_code']))
{
    $url=$url.'area_code='.$_GET['area_code'];
    $area_code = $_GET['area_code'];
    if(isset($_GET['sts'])) {
        $status = $_GET['sts'];
        $sql = "SELECT * FROM complaint WHERE area_code = $area_code and status = $status ORDER BY id DESC";
    } else {
        $sql = "SELECT * FROM complaint WHERE area_code = $area_code ORDER BY id DESC";
    }
} else {
    if(isset($_GET['sts'])) {
        $status = $_GET['sts'];
        $sql = "SELECT * FROM complaint WHERE status = $status ORDER BY id DESC";
    } else {
        $sql = "SELECT * FROM complaint ORDER BY id DESC";
    }
}
$statustext = array('New','Rejected','Pending','Resolved');
$statuscolor = array('text-primary','text-danger','text-warning','text-success');
$result = $db->query($sql);
$newcom = 0;
$rejcom = 0;
$pencom = 0;
$rescom = 0;
$complains = array();
if(mysqli_num_rows($result))
{
    while ($row = $result->fetch_assoc()) {
        switch ($row['status']) {
            case 0:
                # code...
                $newcom++;
                break;
            case 1:
                # code...
                $rejcom++;
                break;
            case 2:
                # code...
                $pencom++;
                break;
            case 3:
                # code...
                $rescom++;
                break;
        }
        # code...
        array_push($complains, $row);
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

    <title>iClean | Complaints</title>

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

                    <!--Top Tiles Counter-->
                    <div class="row">
                        <div class="row top_tiles">
                            <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-blue">
                                    <div class="icon"><i class="fa fa-laptop"></i>
                                    </div>
                                    <div class="count"><?php echo $newcom;?></div>
                                    <h4>New Complains</h4>
                                </div>
                            </div>
                            <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-red">
                                    <div class="icon"><i class="fa fa-remove"></i>
                                    </div>
                                    <div class="count"><?php echo $rejcom;?></div>
                                    <h4>Rejected Complains</h4>
                                </div>
                            </div>
                            <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-orange">
                                    <div class="icon"><i class="fa fa-sort-amount-desc"></i>
                                    </div>
                                    <div class="count"><?php echo $pencom;?></div>
                                    <h4>Pending Complains</h4>
                                </div>
                            </div>
                            <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-green">
                                    <div class="icon"><i class="fa fa-check-square-o"></i>
                                    </div>
                                    <div class="count"><?php echo $rescom;?></div>
                                    <h4>Resolved Complains</h4>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <!--Top Tiles Counter-->
                    <div class="row">
                        <?php $a = 'All'; if(isset($_GET['area_code'])) $a=$area[$_GET['area_code']]; ?>
                        <div class="x_panel">
                            <div class="col-sm-4 col-xs-12"><h2>Complains: <?php echo $a; ?></h2></div>
                            <div class="col-sm-8 col-xs-12 text-right">
                                <a href=<?php echo '"'.$url.'"';?>><button type="button" class="btn btn-default btn-md"> All </button></a>
                                <a href=<?php echo '"'.$url.'&sts=0"';?>><button type="button" class="btn btn-primary btn-md"> New Only </button></a>
                                <a href=<?php echo '"'.$url.'&sts=1"';?>><button type="button" class="btn btn-danger btn-md"> Reject Only </button></a>
                                <a href=<?php echo '"'.$url.'&sts=2"';?>><button type="button" class="btn btn-warning btn-md"> Forward Only </button></a>
                                <a href=<?php echo '"'.$url.'&sts=4"';?>><button type="button" class="btn btn-info btn-md"> Accepted Only </button></a>
                                <a href=<?php echo '"'.$url.'&sts=3"';?>><button type="button" class="btn btn-success btn-md"> Resolved Only </button></a>
                            </div>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <!--Top 16 Complains-->
                    <div class="row">
                        <?php if($updated) { if($updRj) { ?>
                        <div class="alert alert-danger alert-dismissible fade in" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                            </button>
                            The complaint has been rejected.
                        </div>
                        <?php } else if($updF) { ?>
                        <div class="alert alert-warning alert-dismissible fade in" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                            </button>
                            The complain is forwarded and marked as pending.
                        </div>
                        <?php } else if($updRe) { ?>
                        <div class="alert alert-success alert-dismissible fade in" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                            </button>
                            The complain is marked as resolved.
                        </div>
                        <?php } else if($upCom) { ?>
                        <div class="alert alert-success alert-dismissible fade in" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                            </button>
                            Reply Submitted Successfully.
                        </div>
                        <?php }} ?>
                        <?php $i=0; foreach ($complains as $c) { ?>
                        <div class="col-md-4 col-sm-6 col-xs-12 animated fadeInDown">
                            <div class="well profile_view">
                                <div class="col-sm-12">
                                    <div class="col-xs-12">
                                        <div class="col-xs-6 text-left "><h5 class=<?php echo '"'.$statuscolor[$c['status']].'"';?>><strong><?php echo $statustext[$c['status']];?></strong><h5></div>
                                        <div class="col-xs-6 text-right"><?php echo $area[$c['area_code']];?><span> </span><i class="fa fa-map-marker red"></i></div>
                                    </div>
                                    <div class="right col-xs-12 text-center">
                                        <img src=<?php echo '"uploads/'.$c['photo'].'"';?> alt="" class="img-thumbnail img-responsive">
                                        </div>
                                    
                                    <div class="left col-xs-8">
                                        <h6><strong>Complain: </strong><?php echo $c['complain'];?></h6>
                                        <h6><strong>Address: </strong> <?php echo $c['address'];?></h6>
                                        <p><strong>Complain By</strong></p>
                                        <ul class="list-unstyled">
                                            <li><i class="fa fa-phone"></i> Mobile No: <?php echo$c['mobile_no'];?></li>
                                            <li><i class="fa fa-clock-o"></i> Time :<?php echo $c['time'];?></li>
                                        </ul>
                                        <?php if($c['status'] ==3 || $c['status'] ==4) { ?>
                                        <p><strong>Volunteer</strong></p>
                                        <ul class="list-unstyled">
                                            <li><i class="fa fa-phone"></i> Mobile No: <?php echo$c['mobile_no'];?></li>
                                            <li><i class="fa fa-clock-o"></i> Time :<?php echo $c['time'];?></li>
                                        </ul>
                                        <?php } ?>
                                        <form  action=<?php echo '"'.$url.'"';?> method="POST">
                                            <input type="hidden" name="id" value=<?php echo '"'.$c['id'].'"';?>>
                                            <div class="form-group">
                                                <label for="upCom">Report Back:</label>
                                                <textarea name="upCom" placeholder="Comment"><?php echo $c['comment']; ?></textarea>
                                            </div>
                                                <button type="submit" class="btn btn-info btn-sm"> POST </button>
                                        </form>
                                    </div>
                                    
                                    <div class="clearfix"></div>
                                    <div class="col-sm-12">
                                    <?php if($_SESSION['role']!=2) { ?>
                                        <?php if($c['status']==0) { ?>
                                        <div class="text-right">
                                            <form action=<?php echo '"'.$url.'"';?> method="POST" >
                                                <input type="hidden" name="upRj" value=<?php echo '"'.$c['id'].'"';?>>
                                                <button type="submit" class="btn btn-danger btn-sm"> Reject </button>
                                            </form>
                                        </div>    
                                        <?php } ?>
                                        <?php if($c['status']==0) { ?>
                                        <div class=" text-right">
                                            <form action=<?php echo '"'.$url.'"';?> method="POST" >
                                                <input type="hidden" name="upF" value=<?php echo '"'.$c['id'].'"';?>>
                                                <button type="submit" class="btn btn-info btn-sm"> Forward </button>
                                            </form>
                                        </div>    
                                        <?php } ?>
                                        
                                    <?php } ?>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <?php $i++; if($i%3==0){ echo '<div class="clearfix"></div>'; } } ?>
                    </div>
                    <!--Top 16 Complains-->
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