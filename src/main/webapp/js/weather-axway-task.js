/**
 * Created by PatlaDJ on 1.11.2017 г..
 */


//On Websocket open
ws.onopen = function() {
};

//Websocket receive control
ws.onmessage = function(message) {
    var aJson=JSON.parse(message.data);


    if (aJson.cmd=="WaitGeoLocating") {
        $('#titleDiv').html('Please wait while detecting your geographic location...');
    }
    //####################################################################################################################
    // <-----------------------------------------------------------
    else if (aJson.cmd=="WaitWeatherDataSeek") {
        $('#titleDiv').html('Please while weather forecasting for your geographic location...');
    }
    //####################################################################################################################
    // <-----------------------------------------------------------
    else if (aJson.cmd=="FillGeoData") {
        console.log(message.data);
    }
    //####################################################################################################################
    // <-----------------------------------------------------------
    else if (aJson.cmd=="FillWeatherData") {
        console.log(message.data);
    }
    //####################################################################################################################
    // <-----------------------------------------------------------
};

//Websocket shutdown
function closeConnect(){
    ws.close();
}