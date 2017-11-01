<html>
    <head>
        <script>var clientIPAddress='<% request.getRemoteAddr(); %>';</script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/axway-weather-task.css"/>
		 
		<script type="text/javascript" src="../js/jquery-2.2.4.min.js"></script>
        <script type="text/javascript" src="../js/weather-axway-task.js"></script>
    </head>

    <body>

        <div id="titleDiv">Country: ######, City: ######, lat: ####, longt: #####</div>
        <div style="float:none; clear:both; height:0px;></div>

        <div id="daysCont">
            <table id="daysContainer" border="1">
                <tr>
                    <td class="day0">day current0</td>
                    <td class="day1">day 1</td>
                    <td class="day2">day 2</td>
                    <td class="day3">day 3</td>
                    <td class="day4">day 4</td>
                    <td class="day5">day 5</td>
                </tr>
            </table>
        </div>


        <div style="float:none; clear:both;></div>
    </body>
</html>
