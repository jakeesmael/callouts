/**
 * Created by ken on 12/2/14.
 */
$(document).ready(function() {
    window.fbAsyncInit = function() {
        FB.init({
            appId      : '653906481394603',
            xfbml      : true,
            version    : 'v2.2'
        });

        FB.getLoginStatus(function(response) {
            console.log('statusChangeCallback');
            console.log(response);
            // The response object is returned with a status field that lets the
            // app know the current login status of the person.
            if (response.status === 'connected') {
                $("#fb-status").text("Your account is linked.");
            } else if (response.status === 'not_authorized') {
                // The person is logged into Facebook, but not the app.
                $("#fb-status").text("Your account is not linked.")
            } else {
                // The person is not logged into Facebook, so we're not sure if they are logged into this app or not.
                $("#fb-status").text("You're not logged into Facebook.")
            }
        });
    };

    (function(d, s, id){
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {return;}
        js = d.createElement(s); js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

});