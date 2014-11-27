$(document).ready(function() {
	addListeners();
});

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