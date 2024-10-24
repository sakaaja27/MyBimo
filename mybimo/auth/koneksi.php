<?php

$hostName = "localhost";
$userName = "root";
$password = "";
$dbName = "mybimo";

$connect = mysqli_connect($hostName, $userName, $password, $dbName);

if (!$connect){
    echo "koneksi Gagal";
}