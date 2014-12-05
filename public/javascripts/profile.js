/**
 * Created by ken on 10/21/14.
 */
$(document).ready(function() {
    window.fbAsyncInit = function() {
        FB.init({
            appId      : '653906481394603',
            xfbml      : true,
            version    : 'v2.2'
        });

        console.log(getFbFriends($('#fb-id').val()));
    };

    (function(d, s, id){
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {return;}
        js = d.createElement(s); js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

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
});

function getFbFriends(userId) {
    FB.getLoginStatus(function(response) {
        // The response object is returned with a status field that lets the
        // app know the current login status of the person.
        if (response.status === 'connected') {
            /* find all friends */
            FB.api(
                "/me/friends", { scope: "user_friends" },
                function (response) {
                    if (response && !response.error) {
                        console.log(response);
                        var friendIds = [];
                        for (var i = 0; i < response.data.length; i++) {
                            friendIds.push(response.data[i].id);
                        }
                        console.log(friendIds);
                        return friendIds;
                    } else {
                        return -1;
                    }
                }
            );
        } else {
            // The person is not logged into Facebook, so we're not sure if they are logged into this app or not.
            console.log("You're not connected to Facebook!");
            return -1;
        }
    });
}