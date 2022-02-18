<?php

include 'conexion.php';
$codigoupn=$_GET['dni'];

$consulta = "select * from matricula where codigoupn = '$dni'";
$resultado = $conexion -> query($consulta);

while($fila=$resultado -> fetch_array()){
	$matricula[] = array_map('utf8_encode', $fila);
}
echo json_encode($matricula);
$resultado -> close();

?>