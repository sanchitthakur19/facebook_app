<!DOCTYPE html>
<html>
<head>
<title>Facebook Login JavaScript Example</title>
<meta charset="UTF-8">
</head>
<body>
<script>
  function statusChangeCallback(response) {  // Called with the results from FB.getLoginStatus().
    console.log('statusChangeCallback');
    console.log(response);                   // The current login status of the person.
    if (response.status === 'connected') {   // Logged into your webpage and Facebook.
      testAPI();  
    } else {                                 // Not logged into your webpage or we are unable to tell.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into this webpage.';
    }
  }
  
  function test(){
	  FB.login(function()
	{   FB.api('/me/feed', 'post', {message: 'Hello, world!'});  }, {scope: 'publish_actions'});
	  document.getElementById('status').innerHTML = 'The publish_actions permission was deprecated and removed'
	  + ' 3 years ago now. <br>Im not going to spend the time to look for an alternative to a 3 years outdated assignment that should be up to date.<br>'
	  + 'see <a href="https://developers.facebook.com/blog/post/2018/04/24/new-facebook-platform-product-changes-policy-updates/">this link</a>'
	  + ' under Facebook Login section for details.';
  }
  function checkLoginState() {               // Called when a person is finished with the Login Button.
    FB.getLoginStatus(function(response) {   // See the onlogin handler
      statusChangeCallback(response);
    });
  }
  window.fbAsyncInit = function() {
    FB.init({
      appId      : '657925941938704',
      cookie     : true,                     // Enable cookies to allow the server to access the session.
      xfbml      : true,                     // Parse social plugins on this webpage.
      version    : 'v13.0'           // Use this Graph API version for this call.
    });
    FB.getLoginStatus(function(response) {   // Called after the JS SDK has been initialized.
      statusChangeCallback(response);        // Returns the login status.
    });
    
  };
 
  function testAPI() {                      // Testing Graph API after login.  See statusChangeCallback() for when this call is made.
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me', {fields: 'name,email,last_name,first_name,birthday,gender'}, function(response) {
      console.log('Successful login for: ' + response.name);
      document.getElementById('status').innerHTML =
        'Thanks for logging in, ' + response.name + '!<br>'
        + 'Step 1: '+ '<br>'
        + 'Email: ' + response.email + '<br>'
        + 'First: ' + response.first_name + '<br>'
        + 'Last: ' + response.last_name + '<br><br>'
        + 'Step 4: ' + '<br>'
        + 'Birthday: ' + response.birthday + '<br>'
        + 'Gender: ' + response.gender;
    });
  }
</script>


<!-- The JS SDK Login Button -->

<fb:login-button scope="public_profile,email" onlogin="checkLoginState();">
</fb:login-button>

<div id="status">
</div>

<br>
Step 3:
<button onclick="test()">Publish_Actions was deprecated 3 years ago</button>

<!-- Load the JS SDK asynchronously -->
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js"></script>
</body>
</html>