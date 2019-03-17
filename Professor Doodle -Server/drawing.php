<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Content-Type: text/event-stream');
header('Cache-Control: no-cache');
// header('Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token');

require __DIR__ . '/vendor/autoload.php';
$m = new MongoDB\Client();
$hackathonDB = $m->hackathonDB;
$collection = $hackathonDB->drawing;
// ob_start();

 if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents("php://input"), true);
    // $data = array('image'=>'sfsdfdsfsdfsd', 'timestamp'=>'32323232');
    // if($data['first']==='1'){
    //     $result = $collection->insertOne([
    //         'drawing000'=>[
    //             'image'=> $data['image'],
    //             'timestamp'=>$data['timestamp']
    //         ]
    //     ]);
    //     echo $result->getInsertedId();
    // } else {
       $result =  $collection->updateOne(
            ['_id' => new \MongoDB\BSON\ObjectID($data['id'])],
            [ '$set' => ['image'=> $data['image']]]
        );
        echo $result->getUpsertedId();
    
} else {
    $id = $_GET['id'];
    $result =  $collection->find(['_id' => new \MongoDB\BSON\ObjectID($id)]);
    foreach($result as $object){
    // echo 'hello';
        echo "data: ".$object['image']."\n\n";
        flush();
        // echo $s['image'];
    }

//     $event = $changeStream->current();
//     // var_dump($event);
//     if ($event['operationType'] === 'invalidate') {
//         break;
//     }
//     $f = false;

//     $ns = sprintf('%s.%s', $event['ns']['db'], $event['ns']['coll']);
//     $id = json_encode($event['documentKey']['_id']);

//     switch ($event['operationType']) {
//         case 'delete':
//             printf("Deleted document in %s with _id: %s\n\n", $ns, $id);
//             break;

//         case 'insert':
//             printf("Inserted new document in %s\n", $ns);
//             $f=true;
//             echo json_encode($event['fullDocument']), "\n\n";
//             break;

//         case 'replace':
//             printf("Replaced new document in %s with _id: %s\n", $ns, $id);
//             echo json_encode($event['fullDocument']), "\n\n";
//             break;

//         case 'update':
//             printf("Updated document in %s with _id: %s\n", $ns, $id);
//             echo json_encode($event['updateDescription']), "\n\n";
//             break;
//     }
//     if($f==true){break;}
//     $changeStream->next();
// }
    
    
   
    
    
    // var_dump(is_array( array('_id'=>'ObjectId(5c8bcd87936ec823093acda2)')));
    //var_dump(is_object( array('_id'=>'ObjectId(5c8bcd87936ec823093acda2)')));
    // $data = array('image'=>'111111qsfsdfdsfsdfsd', 'timestamp'=>'11132323232');
    // $result = $collection->updateOne(
    //     ['_id' => new \MongoDB\BSON\ObjectID('5c8bcd07936ec82228620c73')],
    //     [ '$set' => [
    //      'drawing000'=>[
    //          'image'=> $data['image'],
    //          'timestamp'=>$data['timestamp']
    //     ] ]
    //     ]);
    // foreach ($result as $key){
        // echo $key['_id'];
    // }
    // var_dump($result);
    // echo $result->getUpsertedId();
    // echo $collection->findOne
}




// $data = array('type'=> 1, 'x'=>377.5, 'y'=>31.1111, 'break'=>1, 'n'=>0);
// $type = $data['type'];
// if($type==1){
//     $ar = array();
//     $ar['x'] = $data['x'];
//     $ar['y'] = $data['y'];
//     $ar['break'] = $data['break'];
//     // echo json_encode($ar);
//     $collection->insertOne($ar);
// }
// $cursor = $collection->find();
// foreach ($cursor as $restaurant) {
//     var_dump($restaurant);
// };
// $changeStream = $collection->watch();
//  var_dump($changeStream);
//  $changeStream->rewind();
// //  var_dump($changeStream->current());
//  while (true) {

    
// }
?>