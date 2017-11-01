package org.patladj.weatheraxway.model;

/**
 * Created by PatlaDJ on 28.10.2017 г..
 */
public class DayStringTemps {
	public String date;
//	public String latitude;
//	public String longitude;

	public String maxTemp;
	public String minTemp;
	public String currentTemp = "";
	public String averageTemp = "";
	public String icon;

	public static final DayStringTemps fromFloatTemps(DayFloatTemps fTemps) {
		return new DayStringTemps(fTemps.date, fTemps.maxTemp+"", fTemps.minTemp+"", fTemps.currentTemp+"", fTemps.averageTemp+"", fTemps.icon);
	}

	public DayStringTemps(String date, String maxTemp, String minTemp, String currentTemp, String averageTemp, String icon) {
		this.date=date;
//		this.latitude=latitude;
//		this.longitude=longitude;
		this.maxTemp=maxTemp;
		this.minTemp=minTemp;
		this.currentTemp = currentTemp;
		this.averageTemp = averageTemp;
		this.icon=icon;
	}
}
