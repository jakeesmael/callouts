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
		var input = "<input placeholder='" + current + "'>";
		$(this).parent().prev().html(input);
		$(this).hide();
		$(this).next().show();
		$(this).next().next().show();
	});

	/* Save */
	$("btn-primary").click(function() {

	});

	/* Cancel */
	$(".btn-default").click(function() {
		$(this).parent().prev().html($(this).parent().prev().attr("data-def"));
		$(this).hide();
		$(this).prev().hide();
		$(this).prev().prev().show();
	});
}