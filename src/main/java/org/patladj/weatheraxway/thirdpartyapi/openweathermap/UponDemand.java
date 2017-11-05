package org.patladj.weatheraxway.thirdpartyapi.openweathermap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.patladj.weatheraxway.model.DayFloatTemps;
import org.patladj.weatheraxway.model.DayList;
import org.patladj.weatheraxway.model.DayStringTemps;
import org.patladj.weatheraxway.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PatlaDJ on 29.10.2017 г..
 */
public class UponDemand {
	private volatile static UponDemand instance=null;
	private String latitude;
	private String longitude;
	private UponDemand() {  }
	public static final synchronized UponDemand getInstance() {
		if (instance==null) {
			instance=new UponDemand();
		}
		return instance;
	}

	private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String OPEN_WEATHER_MAP_ACCOUNT_API_ID ="caf9114a77f4b5ccb0cc56d403c96ec4";
	private static final Pattern MATCH_DATE_AND_TIME = Pattern.compile("^\\s*(\\d{4})-(\\d{2})-(\\d{2})\\s+(.*)$",Pattern.DOTALL);

	private String cityName=null;

	public String getCityName() {
		return cityName;
	}

	public DayList getDataNext5Days(String latitude, String longitude) {
		this.latitude=latitude;
		this.longitude=longitude;

		DayList dayList=new DayList(); //Prepare this object to be returned by this method after all the json parsing and logic processing

		//Utilize the OpenWeatherMap API with the present account for acquiring access to it
		String responseString=null;
		String url = "http://api.openweathermap.org/data/2.5/forecast?lat="+this.latitude+"&lon="+this.longitude+"&appid="+ OPEN_WEATHER_MAP_ACCOUNT_API_ID; // Using the API
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = HTTP_CLIENT.execute(httpGet, new BasicHttpContext());

			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("Sorry! Response Error. Status Code: " + httpResponse.getStatusLine().getStatusCode());
			}
			responseString = EntityUtils.toString(httpResponse.getEntity());
		} catch (Exception ex) {
			System.err.println(ex.toString());
		} finally {
//			HTTP_CLIENT.getConnectionManager().shutdown();
		}

		//If we have a valid json response content
		if (responseString != null && responseString.length()>0) {
			//Parse the json string into gson JsonElement and JsonArray objects again using google Gson lib


//			System.out.println(responseString+"\n\n\n");

			JsonElement root = new JsonParser().parse(responseString);
			String value1 = root.getAsJsonObject().get("cod").getAsString();
			this.cityName = root.getAsJsonObject().get("city").getAsJsonObject().get("name").getAsString();

			JsonArray value2 = root.getAsJsonObject().getAsJsonArray("list");

			/**
			 * Get only Today Plus 5
			 */

			// Match only dates for Right Now (today) and 5 days after today (after Right Now). I do not take into account any timezones whatsoever because
			// OpenWeatherMap API returns the forecast for the specific (lat/long request (the request which I do)) The lat/long request usually carries a proper timezone attachment to it.
			// And that is exactly what I need to satisfy my database where the main keys for the forecast
			// are lat/long -> date -> weather_props


//			final int limit5Days=5;
			Map<String, DayFloatTemps> daysHash = new HashMap<String, DayFloatTemps>();
			boolean isTheCurrentDay=true;
			boolean leaveTheFirstCurrentDayTempIntact=true;
			int daysCntr=0; //Montor when the nex day has came
			float[] accumulatedDailyAverages = new float[8];
			int dailyAvaragesCounter=0;
			DayFloatTemps dayToUpdate=null;
			DayFloatTemps prevDayToUpdate=null;
			for (JsonElement pa : value2) {
				String dt_txt = pa.getAsJsonObject().get("dt_txt").getAsString();
				JsonObject main = pa.getAsJsonObject().get("main").getAsJsonObject();
				JsonObject weather = pa.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject();

				Matcher m=null;
				if ( (null != (m= MATCH_DATE_AND_TIME.matcher(dt_txt))) && m.find() ) {
					// We've got a date from the API list, unificating it by date. For the purpose of unification I do use again a hash. I love HashMaps.
					// As they are so basic and useful for such algorithms in all languages.

					// While collecting the data, convert all the temperatures from Strings to floats
					// Then convert them 2 Celsius. Round them to nn.n and compare them.
					String temp_max = main.get("temp_max").getAsString();
					String temp_min = main.get("temp_min").getAsString();
					String temp = main.get("temp").getAsString();
					String icon = weather.get("icon").getAsString();
					String theDateAsString = m.group(1) + "-" + m.group(2) + "-" + m.group(3);

					if (null != (dayToUpdate = daysHash.get(theDateAsString))) {
						//It's already a known day, we are only updating it

						//First updating the min-max amplidute for a DayFloatTemps
						float newMaxTempForADay = Util.kelvinToCelsiusRoundedToPrecisionOne_FromString(temp_max);
						float newMinTempForADay = Util.kelvinToCelsiusRoundedToPrecisionOne_FromString(temp_min);

						if (newMinTempForADay < dayToUpdate.minTemp) {
							dayToUpdate.minTemp = newMinTempForADay;
						}
						if (newMaxTempForADay > dayToUpdate.maxTemp) {
							dayToUpdate.maxTemp = newMaxTempForADay;
						}

						float currTempSample = Util.kelvinToCelsiusRoundedToPrecisionOne_FromString(temp);
						if (isTheCurrentDay /*&& !leaveTheFirstCurrentDayTempIntact*/) {
//							leaveTheFirstCurrentDayTempIntact=false;
//
//							//Just assign the current temperature for the present day
//							dayToUpdate.currentTemp = currTempSample;
						}
						else {
							//Calc an avarage for dayToUpdate.averageTemp depending on multiple daily cases of 'temp'
							accumulatedDailyAverages[dailyAvaragesCounter++]=currTempSample; //We depend on 8 times a day 3hourly forecasts by OpenWeatherMap. Otherwise this algo will fail.
							dayToUpdate.currentTemp = Float.NaN;
						}

						prevDayToUpdate=dayToUpdate;
					}
					else {
						//Before abandoning the previous complete day: CALC an average of its 'temp' values and SET it
//						System.out.println("\n\nisTheCurrentDay=|"+isTheCurrentDay+"|, prevDayToUpdate.date=|"+(prevDayToUpdate!=null?prevDayToUpdate.date:"null")+"|, datetime=|"+(theDateAsString+" "+m.group(4))+"|");
						if (!isTheCurrentDay) {
//							System.out.println("... vlza");
							calcAndSetTheAvarageTempForAPassedCompleteDay(accumulatedDailyAverages, prevDayToUpdate);
						}

						daysHash.put( //Adding a new unknown day forecast for this lat/long
								  theDateAsString /* The date as a String */
								, new DayFloatTemps(theDateAsString, this.latitude, this.longitude, Util.kelvinToCelsiusRoundedToPrecisionOne_FromString(temp_max), Util.kelvinToCelsiusRoundedToPrecisionOne_FromString(temp_min), Util.kelvinToCelsiusRoundedToPrecisionOne_FromString(temp), Float.NaN, icon) /* The date weather props */
						);
						daysCntr++;
						dailyAvaragesCounter=0; //Starting a new day
						accumulatedDailyAverages = new float[8];

						if (daysCntr > 1) {
							isTheCurrentDay=false;
						}
					}
				}
			}

			//After completion of all the days: CALC and SET the average of the temp values of the last remaining day
//			System.out.println("\n\n Finalno vliza prevDayToUpdate.date=|"+(prevDayToUpdate!=null?prevDayToUpdate.date:"null")+"|");
			calcAndSetTheAvarageTempForAPassedCompleteDay(accumulatedDailyAverages, prevDayToUpdate);


			//After compacting the forcasted days using the daysHash, we do fill up the model DayList with data for returning
			for (Map.Entry<String, DayFloatTemps> entry : daysHash.entrySet())
			{
				dayList.add(DayStringTemps.fromFloatTemps(entry.getValue()));
			}


		}

		return dayList;
	}

	private void calcAndSetTheAvarageTempForAPassedCompleteDay(float[] accumulateDailyAverages, DayFloatTemps dayToUpdate) {

			//We rely on accumulateDailyAverages array[8] full of data for the passing full day
			if (dayToUpdate !=null) {
				float acc=0.0f;
				for (int i=0; i<accumulateDailyAverages.length; i++) {
					acc+=accumulateDailyAverages[i];
				}
				dayToUpdate.averageTemp = Util.roundToPrec((acc/8.0f),1);
			}


	}
}
