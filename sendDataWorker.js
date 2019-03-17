// var canvas = document.createElement('canvas'); 
//   canvas.id = "CursorLayer"; 
//   canvas.width = 400; 
//   canvas.height = 600; 
//   canvas.style.zIndex = 8; 
//   canvas.style.position = "absolute"; 
//   canvas.style.border = "1px solid"; 
//   var body = document.getElementsByTagName("body")[0]; 
//   body.appendChild(canvas);
//   var ctx2 = canvas.getContext('2d');
//   var myImage = new Image();
//   myImage.src = im; //imgData is base 64 string
//   ctx2.drawImage(myImage, 0, 0,myImage.width,myImage.height,0, 0,400,600);  
//   var canvas2 = document.getElementById('mycanvas2'); //canvas where image has to be plotted

// postMessage("1");

// Worker.terminate();


onmessage = function(e){
  var data = e.data;
  //console.log(data);
  var xhr = new XMLHttpRequest();
  var url = "http://192.168.29.40:2234/drawing.php";
  xhr.open("POST", url, true);
  //xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
       console.log("data sent");
        // var json = JSON.parse(xhr.responseText);
        // console.log(json);
    }
  };
  xhr.send(data);
}