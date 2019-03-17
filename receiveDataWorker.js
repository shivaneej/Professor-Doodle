var theUrl="http://192.168.43.40:2234/drawing.php?id=";

var id="";
onmessage= function(event){
  id = event.data;
  theUrl += event.data;
  console.log("here"+id);
}
function getImage(){    	         
  console.log("inisde get image")
 		 var source = new EventSource(theUrl);

  	 source.onmessage = function(event) {  				
    	    // console.log(event);
    	    var d=event.data.split("|").join("\n");
    	    // console.log(d);
    	    // document.write(d);
  	       var pos = d.replace("\n\n", "");
          // console.log(d);
  	       
           postMessage(pos);
           // setTimeout("getImage()",1000);
            // var i=document.getElementById('img');
  	        // i.src=pos;	
  		}; 	
}


// function lll(){}
function lol(){
  if(id!=="") {
    console.log("get image first time");
    getImage(); 
    console.log("yes:"+id);
  } else {
    setTimeout("lol()", 200);  
    console.log("no"+id);
  }
}

lol();
  //document.getElementById("result").innerHTML = "Sorry, your browser does not support server-sent events...";

