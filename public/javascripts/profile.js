/**
 * Created by ken on 10/21/14.
 */
$(document).ready(function() {

    $('#challenge-button').click(function() {
        $('.overlay').fadeToggle("fast");
        $('.modal').fadeToggle("fast");
    });

    $('#cancel-button').click(function() {
        $('.overlay').fadeToggle("fast");
        $('.modal').fadeToggle("fast");
    })

    var nowDate = new Date();
    $('#datetimepicker').datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
        autoclose: true,
        startDate: nowDate
    });

    window.fbAsyncInit = function() {
        FB.init({
            appId      : '653906481394603',
            xfbml      : true,
            version    : 'v2.2'
        });

        FB.getLoginStatus(function(response) {
            // The response object is returned with a status field that lets the
            // app know the current login status of the person.
            if (response.status === 'connected') {
                /* make the API call */
                FB.api(
                    "/{user-id-a}/friends/{user-id-b}",
                    function (response) {
                        if (response && !response.error) {
                            /* handle the result */
                        }
                    }
                );
            } else {
                // The person is not logged into Facebook, so we're not sure if they are logged into this app or not.
                console.log("You're not connected to Facebook!");
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

    function getUsersByFacebookIds(facebookIds) {
        var data = {
            facebookIds: facebookIds
        }
        $.ajax({
        url: "/getFriends",
        type: "POST",
        data: JSON.stringify(facebookIds),
        contentType: "application/json"
        });
    }
    getUsersByFacebookIds([3251335,2353253,12512521]);
});