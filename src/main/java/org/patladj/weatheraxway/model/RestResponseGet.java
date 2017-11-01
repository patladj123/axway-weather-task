package org.patladj.weatheraxway.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PatlaDJ on 21.10.2017 г..
 */
public class RestResponseGet {
	public IP2Location location;
	public DayList days;

	public RestResponseGet(IP2Location location, DayList days) {
		this.location=location;
		this.days=days;
	}
}
