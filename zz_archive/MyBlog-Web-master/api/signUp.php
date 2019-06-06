<?php
/**
 * Created by PhpStorm.
 * User: Admin
 * Date: 1/22/2019
 * Time: 2:53 AM
 */

include "functions.php";

if (empty($_REQUEST['name']) || empty($_REQUEST['email']) || empty($_REQUEST['password'])){
    header('Location: ../index.php');
}
else{

    $name = $_REQUEST['name'];
    $email = $_REQUEST['email'];
    $password = $_REQUEST['password'];
    $created_Date = date('Y-m-d H:i:s');

    $emailExists = isEmailExist($email);
    if ($emailExists){
        echo "Email already exists";
    }else{
        $result = signUp($name,$email,$password,$created_Date);
        if ($result){
            echo json_encode($result);
        }
    }

}