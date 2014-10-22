/**
 * Created by ken on 10/21/14.
 */
$(document).ready(function() {
    console.log("challenge.js included");

    $('#challenge-button').click(function() {
        console.log("button clicked");
        $('#challenge-overlay').fadeToggle("fast");
    });

    $('#cancel-button').click(function() {
        $('#challenge-overlay').fadeToggle("fast");
    })
/*    $('#challenge-overlay').click(function() {
        $('#challenge-overlay').fadeToggle("fast");
    });*/

    //$('#datetimepicker1').datepicker();
});