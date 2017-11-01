package org.patladj.weatheraxway.database.usagepool;

import org.patladj.weatheraxway.database.drivers.TextfileDatabaseIP2Location;
import org.patladj.weatheraxway.database.drivers.TextfileDatabaseLatLongWeather;
import org.patladj.weatheraxway.model.DayFloatTemps;
import org.patladj.weatheraxway.model.DayList;
import org.patladj.weatheraxway.model.DayStringTemps;
import org.patladj.weatheraxway.model.IP2Location;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PatlaDJ on 28.10.2017 г..
 */
public class TextfileDatabasesImpl implements InterfaceToTheDatabase {
	TextfileDatabaseIP2Location ip2LocationInstance = null;
	TextfileDatabaseLatLongWeather latLongWeatherInstance = null;

	private Pattern SPLIT_LATLONG= Pattern.compile("^\\s*([\\d\\.-]+)\\s*\\|\\s*([\\d\\.-]+)\\s*$",Pattern.DOTALL);

	@Override
	public void openIP2LocationTransaction() {
		ip2LocationInstance = TextfileDatabaseIP2Location.getAnotherInstance();
	}

	@Override
	public void commitIP2LocationTransaction() {
		ip2LocationInstance.instanceDispose();
		ip2LocationInstance=null;
	}

	@Override
	public void openLatLongWeatherTransaction() {
		latLongWeatherInstance = TextfileDatabaseLatLongWeather.getAnotherInstance();
	}

	@Override
	public void commitLatLongWeatherTransaction() {
		latLongWeatherInstance.instanceDispose();
		latLongWeatherInstance=null;
	}

	@Override
	public void insertIP2Location(IP2Location ip2loc) {
		Map<String,HashMap<String,String>> iphash = ip2LocationInstance.readData();
		HashMap<String, String> lochash = new HashMap<String, String>();
		lochash.put("country",ip2loc.country);
		lochash.put("city",(ip2loc.city==null?"":ip2loc.city));
		lochash.put("latlong",ip2loc.latitude+"|"+ip2loc.longitude);
		iphash.put(ip2loc.ip, lochash);
		ip2LocationInstance.writeData(iphash);
	}

	@Override
	public void updateIP2Location(IP2Location ip2loc) {

	}

	@Override
	public IP2Location getLocationByIP(String ip) {
		Map<String,HashMap<String,String>> iphash = ip2LocationInstance.readData();
		HashMap<String,String> lochash=null;
		if ( null != (lochash = iphash.get(ip)) ) {
			Matcher m=null;
			if ( (null != (m=SPLIT_LATLONG.matcher(lochash.get("latlong")))) && m.find() ) {
				return new IP2Location(ip, lochash.get("city"), lochash.get("country"), m.group(1), m.group(2));
			}
		}

		return null;
	}

	@Override
	public void insertDays(DayList dayList, IP2Location ip2loc) {
		Map<String, HashMap<String, HashMap<String, String>>> weHash = latLongWeatherInstance.readData();

		HashMap<String, HashMap<String, String>> dl=new HashMap<String, HashMap<String, String>>();
		weHash.put(ip2loc.latitude+"|"+ip2loc.longitude,dl);

		for (DayStringTemps dst: dayList) {
			HashMap<String, String> day=new HashMap<String, String>();
//			day.put("date",dst.date);
			day.put("maxTemp",dst.maxTemp);
			day.put("minTemp",dst.minTemp);
			day.put("currentTemp",dst.currentTemp);
			day.put("averageTemp",dst.averageTemp);
			day.put("icon",dst.icon);

			dl.put(dst.date,day);
		}

		latLongWeatherInstance.writeData(weHash);
	}

	@Override
	public void updateDays(DayList dayList, IP2Location ip2loc) {

	}

	@Override
	public DayList getDaysByLocation(IP2Location ip2loc) {
		Map<String, HashMap<String, HashMap<String, String>>> weHash = latLongWeatherInstance.readData();
		DayList dayList=new DayList();

		HashMap<String, HashMap<String, String>> dlst = weHash.get(ip2loc.latitude + "|" + ip2loc.longitude);

		//If this lat|long isnt in the database, in this case we return null
		if (dlst==null) {
			return null;
		}

		for (Map.Entry<String, HashMap<String, String>> entry : dlst.entrySet()) {
			dayList.add(new DayStringTemps(entry.getKey(), entry.getValue().get("maxTemp"), entry.getValue().get("minTemp"), entry.getValue().get("currentTemp"), entry.getValue().get("averageTemp"), entry.getValue().get("icon") ));
		}

		return dayList;
	}
}
