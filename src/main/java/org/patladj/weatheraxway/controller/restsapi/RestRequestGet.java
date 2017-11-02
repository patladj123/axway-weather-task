package org.patladj.weatheraxway.controller.restsapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.patladj.weatheraxway.controller.geoipseek.GeoIPSeeker;
import org.patladj.weatheraxway.controller.weathermapseek.WeatherMapSeeker;
import org.patladj.weatheraxway.database.usagepool.InterfaceToTheDatabase;
import org.patladj.weatheraxway.database.usagepool.TextfileDatabasesImpl;
import org.patladj.weatheraxway.model.DayList;
import org.patladj.weatheraxway.model.IP2Location;
import org.patladj.weatheraxway.model.RestResponseGet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PatlaDJ on 27.10.2017 г..
 */
public class RestRequestGet {
	// Accommodate two requests, one for all resources, another for a specific resource (in this case a specific IP address)
	private Pattern regExAllPattern = Pattern.compile("/?weatheraxway-rest-service");
	private Pattern regExPatternWithData = Pattern.compile("/?weatheraxway-rest-service/(get-data-for-ip)/([\\d\\.]+)");
	private Pattern MATCH_VALID_IP = Pattern.compile("^\\s*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s*$", Pattern.DOTALL);

	private String ip;

	public RestRequestGet(String pathInfo, PrintWriter out, HttpServletResponse resp) throws ServletException {
		// regex parse pathInfo
		Matcher matcher;

		// Check for IP case first, since the All pattern would also match
		matcher = regExPatternWithData.matcher(pathInfo);

		System.out.println("pathInfo=|"+pathInfo+"|");

		if (matcher.find()) {
			System.out.println("match-va");
			if ("get-data-for-ip".equals(matcher.group(1))) {
				Matcher m2=null;
				if ( (null != (m2= MATCH_VALID_IP.matcher(matcher.group(2))) && m2.find()) ) {
					ip = m2.group(1); //Valid IP address

					if (ip!=null) {
						//We've got an IP address, so we serve the weather data and location info for it in JSON format

						InterfaceToTheDatabase itdb = new TextfileDatabasesImpl();
						IP2Location ip2Location;

						//First check if the IP address isnt already in our database
						itdb.openIP2LocationTransaction();
						ip2Location = itdb.getLocationByIP(ip);
						itdb.commitIP2LocationTransaction();

						if (ip2Location==null) {
							//If it isn't there, then we do geolocate it using our GeoIPSeeker module
							GeoIPSeeker geoipSeeker = GeoIPSeeker.getAnotherInstance();
							Object lockOnto = new Object();
							geoipSeeker.initiateObtainLocationForIP_NonBlocking(ip,lockOnto);
							System.out.println("FreeGeoIP Request started...");
							long start=System.currentTimeMillis();
							synchronized (lockOnto) { try { lockOnto.wait(); } catch (InterruptedException e) { /* Ignore */ } } //Patience. Simply wait for the parallel execution to finish
							ip2Location=geoipSeeker.getIp2Location(); //Should be available by now, after we waited
							System.out.println("FreeGeoIP Request completed in "+(System.currentTimeMillis()-start)+"ms");
							geoipSeeker.disposeIt();

							//Then we save it in our database for later (cached) usage
							itdb.openIP2LocationTransaction();
							itdb.insertIP2Location(ip2Location);
							itdb.commitIP2LocationTransaction();
						}





						//Implement the logic that gets the weather data for the current (and next 5 days) as per specification of the Developer_Task
						DayList dayList=null;
						String cityName=null;

						//First check if we don't already have Days data in the database for this lat/long
						itdb.openLatLongWeatherTransaction();
						dayList = itdb.getDaysByLocation(ip2Location);
						itdb.commitLatLongWeatherTransaction();

						if (dayList==null) {
							//If we don't then we query it using the WeatherMapSeeker module
							WeatherMapSeeker weatherMapSeeker = WeatherMapSeeker.getAnotherInstance();
							Object lockOnto = new Object();
							weatherMapSeeker.initiateObtainWeatherDataForLatLong_NonBlocking(ip2Location.latitude, ip2Location.longitude,lockOnto);
							System.out.println("OpenWeatherMap Request started...");
							long start=System.currentTimeMillis();
							synchronized (lockOnto) { try { lockOnto.wait(); } catch (InterruptedException e) { /* Ignore */ } } //Patience. Simply wait for the parallel execution to finish
							dayList=weatherMapSeeker.getDayList(); //Should be available by now, after we waited
							cityName=weatherMapSeeker.getCityName();
							System.out.println("OpenWeatherMap Request completed in "+(System.currentTimeMillis()-start)+"ms");
							weatherMapSeeker.disposeIt();

							//Sometimes FreeGeoIP does not locate the cityName, so we take it from the OpenWeatherMap API response
							ip2Location.city=cityName;
							//We need to save the location DB again, only because of potentially blank city
							itdb.openIP2LocationTransaction();
							itdb.insertIP2Location(ip2Location);
							itdb.commitIP2LocationTransaction();

							//Then we save it in our database for later (cached) usage
							itdb.openLatLongWeatherTransaction();
							itdb.insertDays(dayList,ip2Location);
							itdb.commitLatLongWeatherTransaction();
						}


						RestResponseGet rrg=new RestResponseGet(ip2Location,dayList);


						String json = new Gson().toJson(rrg);
						resp.setContentType("application/json");
						resp.setCharacterEncoding("UTF-8");
						out.write(json);
					}

				}
				else {
					throw new ServletException("Unknown REST ResourceRequested. IP pattern is invalid");
				}
			}
			else {
				throw new ServletException("Unknown REST ResourceRequested. Unexpected URI after /weatheraxway-rest-service");
			}

			return;
		}
		else {
			System.out.println("NE match-va");
		}

		matcher = regExAllPattern.matcher(pathInfo);
		if (matcher.find()) return;

		throw new ServletException("Invalid URI");
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}