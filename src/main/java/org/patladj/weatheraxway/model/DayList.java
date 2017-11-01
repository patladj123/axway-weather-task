package org.patladj.weatheraxway.model;

import java.util.ArrayList;


/**
 * Created by PatlaDJ on 29.10.2017 г..
 */
public class DayList extends ArrayList<DayStringTemps> {

	@Override
	public boolean add(DayStringTemps day) {
		return super.add(day);
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();

		for (DayStringTemps day : this) {
			sb.append("\n\nDayStringTemps |"+day.date+"|");
			sb.append("\n	currentTemp=|"+day.currentTemp +"|");
			sb.append("\n	averageTemp=|"+day.averageTemp +"|");
			sb.append("\n	minTemp=|"+day.minTemp+"|");
			sb.append("\n	maxTemp=|"+day.maxTemp+"|");
			sb.append("\n	icon=|"+day.icon+"|");

		}

		return sb.toString();
	}


}
