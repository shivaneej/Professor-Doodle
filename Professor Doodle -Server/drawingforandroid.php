<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
// header('Content-Type: ');
header('Cache-Control: no-cache');
// header('Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token');

require __DIR__ . '/vendor/autoload.php';
$m = new MongoDB\Client();
$hackathonDB = $m->hackathonDB;
$collection = $hackathonDB->drawing;
$id = $_GET['id'];

$result =  $collection->find(['_id' => new \MongoDB\BSON\ObjectID($id)]);
foreach($result as $object){
    echo $object['image'];
}

?>