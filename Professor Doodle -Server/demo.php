<?php
header('Access-Control-Allow-Origin: *');
// header('Access-Control-Allow-Method: GET');
header('Content-Type: text/event-stream');
header('Cache-Control: no-cache');


$time = date('r');
// require __DIR__ . '/vendor/autoload.php';
// $m = new MongoDB\Client();
// $hackathonDB = $m->hackathonDB;
// $collection = $hackathonDB->drawing;
// $id = $_GET['id'];
// $result =  $collection->find(['_id' => new \MongoDB\BSON\ObjectID($id)]);
    // foreach($result as $object){
        // echo "heloo\n\n";
        // flush();
        // echo "".$object['drawing000']['image']."\n\n";
        // flush();
    // }
echo "data: The server time is: {$time}\n\n";
flush();
?>