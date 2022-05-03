<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body bgcolor="lightblue">
	
	<div style="width: 70%; margin:auto;">
	<h3>Result</h3>
		<form onsubmit="">
			<div id="imgDiv">
				
			</div>
		</form>
	</div>
	
	<script>
	   var imgString = "${urllist}";
	   
	   var str = imgString.substring(1,imgString.length-1);
	   var list = str.split(",");
	   console.log(list);
	   
	   var imgDiv = document.getElementById("imgDiv");
	   
	   for(var a=0; a<list.length; a++){
		  
		   var br1 = document.createElement('br');
		   imgDiv.appendChild(br1);
		   var img = document.createElement('img');
		   img.src = list[a];
		   img.onclick = '"shareFunc(this)"';
		   var imgID ='img'+a
		   var imgSrc = list[a];
		   img.id = imgID;
		   img.style.margin = "1rem";
		   
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
		   input.style.margin = "1.5rem";
		   imgDiv.appendChild(img);
		   imgDiv.appendChild(input);
		   
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
	
	<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v13.0&appId=988483798473577&autoLogAppEvents=1" nonce="91e8CQ5N">
	</script>
</body>
</html>