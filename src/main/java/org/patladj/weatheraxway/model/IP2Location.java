package org.patladj.weatheraxway.model;

/**
 * Created by PatlaDJ on 28.10.2017 г..
 */
public class IP2Location {
	public String ip;
	public String city;
	public String country;
	public String latitude;
	public String longitude;

	public IP2Location(String ip, String city, String country, String latitude, String longitude) {
		this.ip=ip;
		this.city=city;
		this.country=country;
		this.latitude=latitude;
		this.longitude=longitude;
	}
}
