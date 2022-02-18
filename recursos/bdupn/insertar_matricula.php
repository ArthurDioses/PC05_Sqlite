<?php

include 'conexion.php';
$codigoupn=$_POST['codigoupn'];
$dni=$_POST['dni'];
$nombre=$_POST['nombre'];
$genero=$_POST['genero'];
$curso=$_POST['curso'];

$consulta="insert into matricula values(null,'".$codigoupn."','".$dni."','".$nombre."','".$genero."','".$curso."')";

mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);
?>