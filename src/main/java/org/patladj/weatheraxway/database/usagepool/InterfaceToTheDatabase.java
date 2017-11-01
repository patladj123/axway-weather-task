package org.patladj.weatheraxway.database.usagepool;

import org.patladj.weatheraxway.model.DayList;
import org.patladj.weatheraxway.model.IP2Location;
import org.patladj.weatheraxway.model.DayFloatTemps;

/**
 * Created by PatlaDJ on 28.10.2017 г..
 */
public interface InterfaceToTheDatabase {

	public void openIP2LocationTransaction();

	public void commitIP2LocationTransaction();

	public void openLatLongWeatherTransaction();

	public void commitLatLongWeatherTransaction();

	public void insertIP2Location(IP2Location ip2loc);

	public void updateIP2Location(IP2Location ip2loc);

	public IP2Location getLocationByIP(String ip);

	public void insertDays(DayList dayList, IP2Location ip2loc);

	public void updateDays(DayList dayList, IP2Location ip2loc);

	public DayList getDaysByLocation(IP2Location ip2loc);
}

