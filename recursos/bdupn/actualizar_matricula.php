<?php

include 'conexion.php';
$codigoupn=$_POST['codigoupn'];
$dni=$_POST['dni'];
$nombre=$_POST['nombre'];
$genero=$_POST['genero'];
$curso=$_POST['curso'];

$consulta="update matricula set dni = '".$dni."', nombre = '".$nombre."', genero = '".$genero."', curso = '".$curso."' where codigoupn = '".$codigoupn."'";

mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);
?>