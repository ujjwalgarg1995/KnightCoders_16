<?php
session_start();
require 'includes/connect.php';

if(!isset($_SESSION['name']))
{
    header("Location: login.php");
}
include_once 'phpelements.php';					
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>iClean | Dashboard</title>

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
                <div class="">

                    <div class="row row">
                        <div class="row top_tiles">
                            <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-blue text-center">
                                    <h4>New Complains</h4>
                                </div>
                            </div>
                            <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-red text-center">
                                    <h4>Rejected Complains</h4>
                                </div>
                            </div>
                            <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-orange text-center">
                                    
                                    <h4>Pending Complains</h4>
                                </div>
                            </div>
                            <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                                <div class="tile-stats bg-green text-center">
                                    <h4>Resolved Complains</h4>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row top_tiles">
                     <?php 
					 							$countpend =0;
					 							$countrej =0;
					 							$countproc =0;
					 							$countres =0;
                                                $sql = 'SELECT * FROM area,wardmanager where area.area_code = wardmanager.wid';
										$result = $db->query($sql) or die(mysqli_error($db));
										if(mysqli_num_rows($result)!=0)
										{
												while($row=$result->fetch_array(MYSQLI_ASSOC))
												{
													$sql2 = 'SELECT status FROM complaint where area_code='.$row['area_code'];
													$result2 = $db->query($sql2) or die(mysqli_error($db));
													if(mysqli_num_rows($result)!=0)
													{
														while($row2=$result2->fetch_array(MYSQLI_ASSOC))
														{
															if($row2['status']==0)
																$countpend ++;
															if($row2['status']==1)
																$countrej ++;
															if($row2['status']==2)
																$countproc ++;
															if($row2['status']==3)
																$countres ++;
														}
														
														echo ' <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">';
														echo '<div class="tile-stats">';
														echo '<div class="icon">';
														echo ' </div>';
														
														echo '<div class="count">';
														echo '<div class="col-lg-3 blue  col-md-3">'.$countpend.'</div>';
														echo '<div class="col-lg-3 red  col-md-3">'.$countrej.'</div>';
														echo '<div class="col-lg-3 warning col-md-3">'.$countproc.'</div>';
														echo '<div class="col-lg-3 green col-md-3">'.$countres.'</div>';
														echo  '</div>';
														
														echo '<h3>'.$row['area_name'].'</h3>';
														echo '<p>'.$row['fname'].' '.$row['lname'].'</p>' ;
														echo ' </div>';
														echo ' </div>';
														$countpend =0;
														$countrej =0;
														$countproc =0;
														$countres = 0;
													}
												}
										} 
						?>
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