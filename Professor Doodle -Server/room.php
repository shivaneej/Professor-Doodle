<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Content-Type: text/event-stream');
header('Cache-Control: no-cache');

require __DIR__ . '/vendor/autoload.php';
$m = new MongoDB\Client();
$hackathonDB = $m->hackathonDB;
$collection = $hackathonDB->drawing;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $result = $collection->insertOne([
        'image'=>""
    ]);
    echo $result->getInsertedId();
} else {
    $id = $_GET['id'];
    $result =  $collection->find(['_id' => new \MongoDB\BSON\ObjectID($id)]);
    foreach($result as $object){
        echo $object['image'];
    }
   // echo 'hello';
    // echo $result->getUpsertedId();
}

?>