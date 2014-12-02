/**
 * Created by ken on 10/21/14.
 */
$(document).ready(function() {

    $('#challenge-button').click(function() {
        $('.overlay').fadeToggle("fast");
    });

    $('#cancel-button').click(function() {
        $('.overlay').fadeToggle("fast");
    })

    $('#datetimepicker').datetimepicker({
        format: "dd MM yyyy - hh:ii",
        autoclose: true,
        startDate: Date.toString(Date.now())
    });
});