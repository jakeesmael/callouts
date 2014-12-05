/**
 * Created by jakeesmael on 12/4/14.
 */

$(document).ready(function() {
	addListeners();
});

function addListeners() {
	/* Choose winner button */
	$(".winner").click(function() {
		var winner = $(this).html();
		$(".declare").attr("data-winner", winner);
	});

	/* Declare winner */
	$(".declare").click(function() {
		var challengeId = parseInt($(this).attr("data-id"));
		var winner = $(this).attr("data-winner");
		var data = {
			challengeId: challengeId,
			winner: winner
		}
		$.ajax({
			url: "/challenge/declare",
			type: "POST",
			data: JSON.stringify(data),
			contentType: "application/json"
		});
		removeDeclareButtons(winner);
	});
}

function removeDeclareButtons(winner) {
	var h2 = "<h2>" + winner + " is the winner!</h2>";
	$(".btn-group").after(h2);
	$(".declare").remove();
	$(".btn-group").remove();
}