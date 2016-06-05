<?php
require 'includes/connect.php';
$errorMsg = '';
if(isset($_POST['username']) && isset($_POST['password']))
{
    $username = $_POST['username'];
    $password = md5($_POST['password']);
    $sql = "SELECT * FROM user WHERE username = '$username' AND  password = '$password' ";
    $result=$db->query($sql);
    mysqli_num_rows($result);
    if(mysqli_num_rows($result)!=0)
    {
        $res = $result->fetch_array(MYSQLI_ASSOC);
        session_start();
        $_SESSION['name'] = $res['name'];
        $_SESSION['id'] = $res['id'];
        $_SESSION['role'] = $res['role'];
        if($res['role']==3){
            $sql = "SELECT area_code FROM volunteer WHERE phone = '$username'";
            $result=$db->query($sql);
            if(mysqli_num_rows($result)==1){
                $row=$result->fetch_array(MYSQLI_ASSOC);
                $_SESSION['area_code']=$row['area_code'];
            }
        }        
        header('Location: index.php');
    } else {
        $errorMsg = 'Invalid Username or Password';
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

    <title>iClean | Login </title>

    <!-- Bootstrap core CSS -->

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <link href="fonts/css/font-awesome.min.css" rel="stylesheet">
    <link href="css/animate.min.css" rel="stylesheet">

    <!-- Custom styling plus plugins -->
    <link href="css/custom.css" rel="stylesheet">
    <link href="css/icheck/flat/green.css" rel="stylesheet">
        <link href="css/mycss.css" rel="stylesheet">


    <script src="js/jquery.min.js"></script>
        
        </script>

    <!--[if lt IE 9]>
        <script src="../assets/js/ie8-responsive-file-warning.js"></script>
        <![endif]-->

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

</head>

<body style="background:#F7F7F7; background-image:url(images/bgimage.jpg); background-size:cover; background-repeat= norepeat">
    
    <div class="">
        <a class="hiddenanchor" id="toregister"></a>
        <a class="hiddenanchor" id="tologin"></a>

        <div id="wrapper">
            <div id="login" class="animate form">
                <section class="login_content">
                    <img src="images/sgzplogo.png" width="125px" height="125px" >
                    <form id="loginForm" action="login.php" method="post" >
                        <h1>Login</h1>
                        <div>
                            <input type="text" class="form-control" placeholder="Username" name="username" required/>
                                                            
                        </div>
                                                        
                                                        
                        <div>
                            <input type="password" class="form-control" placeholder="Password" name="password"  required />
                        </div>
                                                        <div>
                                                        <p id="loginError" class="myError"><?php echo $errorMsg;?></p>
                                                        </div>
                        <div>
                                                                <input type="submit" class="btn btn-round btn-primary submit"/>
                            <br>

                        </div>
                        <div class="clearfix"></div>
                        <div class="separator">

                            
                            <div>

                                <p>©2016 All Rights Reserved.<br></p>
                            </div>
                        </div>
                    </form>
                    <!-- form -->
                </section>
                <!-- content -->
            </div>
        </div>
    </div>

</body>

</html>