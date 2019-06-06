<?php
/**
 * Created by PhpStorm.
 * User: Admin
 * Date: 1/22/2019
 * Time: 6:41 PM
 */

include "functions.php";

if (empty($_REQUEST['email']) || empty($_REQUEST['password'])){
    header('Location: ../index.php');
}
else{
     $email = $_REQUEST['email'];
     $password = $_REQUEST['password'];

    $emailExists = isEmailExist($email);
    if ($emailExists){

        $result = logIn($email,$password);
        echo json_encode($result);
        /*if ($result){
            echo  json_encode($result);
        }else{
            echo  json_encode($result);
        }*/

    }
    else{
         echo json_encode("Email does not exists");
    }
}