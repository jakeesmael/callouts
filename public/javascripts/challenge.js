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
});