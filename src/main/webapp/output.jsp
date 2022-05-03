<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	
	<form onsubmit="">
		<div id="imgDiv">
			
		</div>
	</form>
	<script>
	   var imgString = "${urllist}";
	   
	   var str = imgString.substring(1,imgString.length-1);
	   var list = str.split(",");
	   console.log(list);
	   
	   var imgDiv = document.getElementById("imgDiv");
	   
	   for(var a=0; a<list.length; a++){
		  /*  var form = document.createElement('form');
		   form.id = 'imgform'+a; */
		   var br1 = document.createElement('br');
		   imgDiv.appendChild(br1);
		   var img = document.createElement('img');
		   img.src = list[a];
		   img.onclick = '"shareFunc(this)"';
		   var imgID ='img'+a
		   var imgSrc = list[a];
		   img.id = imgID;
		   img.style = '"margin-vertical: 1rem"';
		   
		   var br2 = document.createElement('br');
		   imgDiv.appendChild(br2);
		   var input = document.createElement('input');
		   input.type = "button";
		   input.name = "img"+a;
		   input.id = "name"+a;
		   input.onclick = function (imgSrc) {
			    console.log("Onclick",imgSrc);
			    var imgID = event.target.name;
			    var imgURL = document.getElementById(imgID).src;
			    console.log('image URL: ',imgURL);
			    
			    var imgOrigin = imgURL.origin;
			    var finalURL = encodeURIComponent(imgOrigin+'?img='+imgURL);
			    //window.open ('http://www.facebook.com/sharer.php?u='+finalURL,'','width=500, height=500, scrollbars=yes, resizable=no');
			    window.open('http://www.facebook.com/sharer.php?u='+encodeURIComponent(imgURL),'sharer','toolbar=0,status=0,width=626,height=436');
		   };
		   input.value = "Share on Facebook";
		   /*var divVar = document.createElement('div');
		   //divVar.class = "fb-share-button"
		   divVar.data-href = "https://developers.facebook.com/docs/plugins/";
		   divVar.type="button";
		   //divVar.data-size="large";
		   var a = document.createElement('a');
		   a.target="_blank" ;
		   a.href="https://www.facebook.com/sharer/sharer.php?u=https%3A%2F%2Fdevelopers.facebook.com%2Fdocs%2Fplugins%2F&amp;src=sdkpreparse" ;
		   //a.class="fb-xfbml-parse-ignore";
		   imgDiv.appendChild(a);
		   imgDiv.appendChild(divVar);*/
		   /////
		   imgDiv.appendChild(img);
		   imgDiv.appendChild(input);
		   //imgDiv.appendChild(form);
		   var br3 = document.createElement('br');
		   imgDiv.appendChild(br3);
	   }
	   
	</script>
	<script async src="https://www.googletagmanager.com/gtag/js?id=G-9E7CKGNNYW"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-9E7CKGNNYW');
</script>
	<div id="fb-root"></div>
	<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v13.0&appId=988483798473577&autoLogAppEvents=1" nonce="91e8CQ5N"></script>
</body>
</html>