$(document).ready(function() {
	addListeners();
});

function checkLoginState() {
    FB.getLoginStatus(function(response) {
        console.log('statusChangeCallback');
        console.log(response);
        // The response object is returned with a status field that lets the
        // app know the current login status of the person.
        if (response.status === 'connected') {
            saveSettings("facebookId", response.authResponse.userID);
            FB.api(
                "/me/picture",
                {
                    "redirect": false,
                    "height": "250",
                    "type": "normal",
                    "width": "250"
                },
                function(response) {
                    if (response && !response.error) {
                        saveSettings("pictureUrl", response.data.url);
                    } else {
                        return ""
                    }
                }
            );
        } else if (response.status === 'not_authorized') {
            // The person is logged into Facebook, but not the app.
            $("#fb-status").text("Your account is not linked.")
        } else {
            // The person is not logged into Facebook, so we're not sure if they are logged into this app or not.
            $("#fb-status").text("You're not logged into Facebook.")
        }
    });
}

function addListeners() {
	/* Edit */
	$(".btn-link").click(function() {
		var current = "";
		if ($(this).parent().attr("id") == "edit-password")
			current = "New Password";
		else
			current = $(this).parent().prev().html();
		var type = $(this).attr("data-edit");
		var input = "<input placeholder='" + current + "' id='" + type + "'>";
		$(this).parent().prev().html(input);
		$(this).hide();
		$(this).next().show();
		$(this).next().next().show();
	});

	/* Save */
	$(".btn-primary").click(function() {
		var type = $(this).attr("data-edit");
		type = "#" + type;
		var input = $(type).val();
		if (input != "") {
			saveSettings(type.substring(1), input);
		}
		if (type.substring(1) != "password")
			$(this).parent().prev().html(input);
		else
			$(this).parent().prev().html("Omitted for security");
		$(this).hide();
		$(this).next().hide();
		$(this).prev().show();
	});

	/* Cancel */
	$(".btn-default").click(function() {
		$(this).parent().prev().html($(this).parent().prev().attr("data-def"));
		$(this).hide();
		$(this).prev().hide();
		$(this).prev().prev().show();
	});

    /* Facebook Link */
    $(".fb-login").click(function() {
        FB.getLoginStatus(function(response) {
            console.log('statusChangeCallback');
            console.log(response);
            // The response object is returned with a status field that lets the
            // app know the current login status of the person.
            if (response.status === 'connected') {
                saveSettings("facebookId", response.authResponse.userID);
            } else if (response.status === 'not_authorized') {
                // The person is logged into Facebook, but not the app.
                $("#fb-status").text("Your account is not linked.")
            } else {
                // The person is not logged into Facebook, so we're not sure if they are logged into this app or not.
                $("#fb-status").text("You're not logged into Facebook.")
            }
        });
    });
}

function saveSettings(type, input) {
	var data = {
		type: type,
		input: input
	}
	$.ajax({
		url: "/editSettings",
		type: "POST",
		data: JSON.stringify(data),
		contentType: "application/json"
	});
}

