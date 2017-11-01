package org.patladj.weatheraxway.model;

/**
 * Created by PatlaDJ on 28.10.2017 г..
 */
public class DayFloatTemps {
	public String date;
	public String latitude;
	public String longitude;

	public float maxTemp;
	public float minTemp;
	public float currentTemp = Float.NaN;
	public float averageTemp = Float.NaN;
	public String icon;

	public DayFloatTemps(String date, String latitude, String longitude, float maxTemp, float minTemp, float currentTemp, float averageTemp, String icon) {
		this.date=date;
		this.latitude=latitude;
		this.longitude=longitude;
		this.maxTemp=maxTemp;
		this.minTemp=minTemp;
		this.currentTemp = currentTemp;
		this.averageTemp = averageTemp;
		this.icon=icon;
	}
}
