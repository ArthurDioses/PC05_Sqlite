<?php

include 'conexion.php';
$codigoupn=$_POST['codigoupn'];

$consulta="delete from matricula where codigoupn = '".$codigoupn."'";

mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);
?>