<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>

	<head>
    <meta charset="UTF-8">
    <title>Facebook</title>
    <link type="text/css" rel="stylesheet" href="stylesheets/main.css" />
	
</head>

	<body BGCOLOR="lightblue">
		<h1 ALIGN="CENTER">Companion App</h1>
		<script>
			// This is called with the results from FB.getLoginStatus().
			function statusChangeCallback(response) {
				console.log('statusChangeCallback');
				console.log(response);
				// The response object is returned with a status field that lets the
				// app know the current login status of the person.
				// Full docs on the response object can be found in the documentation
				// for FB.getLoginStatus().
				if (response.status === "connected") {
					// Logged into your app and Facebook.
					var authResponse = response.authResponse;
					var accessToken = authResponse.accessToken;
					//response.addCookie("accessToken",accessToken);
					console.log(accessToken)
					//Cookie at = new Cookie("accessToken", accessToken);
					//response.addCookie(at);
					//response.sendRedirect("https://fbapp-348423.appspot.com/home.jsp");
					testAPI();
					console.log(response);
					/* var authResponse = response.authResponse;
					var accessToken = authResponse.accessToken;
					console.log(accessToken)
					sesson.setAttribute("accessToken", accessToken) */
					
					
				} else {
					//The person is not logged into your app or we are unable to tell.
					/* document.getElementById('status').innerHTML = 'Please log ' +
						'into this app.'; */
				}
			}

			// This function is called when someone finishes with the Login
			// Button.  See the onlogin handler attached to it in the sample
			// code below.
			function checkLoginState() {
				FB.getLoginStatus(function (response) {
					statusChangeCallback(response);
					console.log(response);
					var authResponse = response.authResponse;
					var accessToken = authResponse.accessToken;
					console.log(accessToken)
					session.setAttribute("accessToken", accessToken)
				});
			}

			window.fbAsyncInit = function () {
				FB.init({
					appId: '988483798473577',
					cookie: true,  // enable cookies to allow the server to access
								   // the session
					xfbml: true,  // parse social plugins on this page
					version: 'v13.0' // Specify the Graph API version to use
				});
				FB.AppEvents.logPageView();

				FB.getLoginStatus(function (response) {
					statusChangeCallback(response);
				});

			};

			// Load the SDK asynchronously
			(function (d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id)) return;
				js = d.createElement(s);
				js.id = id;
				js.src = "https://connect.facebook.net/en_US/sdk.js";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));

			// Here we run a very simple test of the Graph API after login is
			// successful.  See statusChangeCallback() for when this call is made.
			function testAPI() {
				console.log('Welcome!  Fetching your information.... ');
				FB.api("/me", function (response) {
					console.log(response);
					
					console.log('Successful login for: ' + response.name);
					document.getElementById('status').innerHTML =
						'Thanks for logging in, ' + response.name + '!';
				});
				//List<String> listURL = new ArrayList<String>();
				//List<String> listID = new ArrayList<String>();
				FB.api("/me?fields=albums{photos{link,images}}", function (response) {

					var albums = response["albums"]["data"];
					var imageLinks = new Array();
		            var imageID = new Array();
		            
					console.log(albums);
					for (var i =0; i< albums.length; i++){
						var albumId = albums[i]["id"]
						var photos = albums[i]["photos"]["data"]
						for( var j = 0; j < photos.length; j++){
							var id = photos[j]["id"]
							var link = photos[j]["link"]
							var image = photos[j]["images"]
							var url = image[6]["source"]
							
							//var img = document.createElement("img");
							var img = new Image();
							img.src = url;
							
							//listID.add(id);
							//listURL.add(url);
							//InputStream in = new URL(url).openStream();
				            //Files.copy(in, Paths.get(id + ".jpg"));
				            imageLinks.push(url)
                            imageID.push(id)
							//document.getElementById('status').appendChild(img);
				            
						}
						
					}
					console.log(imageID);
	                console.log(imageLinks);
	                //session.setAttribute("imageID", imageID)
	                //session.setAttribute("imageLinks", imageLinks)
	                var url_val = document.getElementById('url_val')
	                var id_val = document.getElementById('id_val')
	                url_val.value = imageLinks
	                id_val.value = imageID
				});
			}
			
			function logout() {
				FB.logout(function (response) {
					location.reload(true);
				});
			}
			

		</script>
		<script async src="https://www.googletagmanager.com/gtag/js?id=G-9E7CKGNNYW"></script>
		<script>
		  window.dataLayer = window.dataLayer || [];
		  function gtag(){dataLayer.push(arguments);}
		  gtag('js', new Date());
		
		  gtag('config', 'G-9E7CKGNNYW');
		</script>
		
		<!--
		  Below we include the Login Button social plugin. This button uses
		  the JavaScript SDK to present a graphical Login button that triggers
		  the FB.login() function when clicked.
		-->
		
		<div style="margin: auto; width:12%;">
			
			<fb:login-button scope="public_profile,email,user_photos" onlogin="checkLoginState();"></fb:login-button>
			
	        <form action="/Result" method="post">
	        	
	        	<input type ="hidden" name="url" id = "url_val" value="")>
	        	<input type ="hidden" name="id" id = "id_val" value="")>
	        	<div style="margin-top: 2rem;">
	        		<input type="submit" name = "result" value="Upload">
	        	</div>
	        	
	        </form>
	        
	        <div style="margin-top: 2%;" id="status"></div>
		
		</div>
		
	</body>
</html>
