<?php
/**
 * Created by PhpStorm.
 * User: Admin
 * Date: 1/21/2019
 * Time: 11:35 PM
 */
define("DB_HOST","localhost");
define("DB_USER","root");
define("DB_PASS","123456");
define("DB_NAME","myblog_db");

$con = mysqli_connect(DB_HOST,DB_USER,DB_PASS,DB_NAME);

if (!$con)
{
    echo "ERROR!" . mysqli_connect_error();
}
//echo "connected";