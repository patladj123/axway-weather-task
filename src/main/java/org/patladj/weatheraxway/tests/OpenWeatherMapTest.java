package org.patladj.weatheraxway.tests;

/**
 * Created by PatlaDJ on 25.10.2017 г..
 */

import org.patladj.weatheraxway.model.DayList;
import org.patladj.weatheraxway.thirdpartyapi.openweathermap.IndependentWorker;
import org.patladj.weatheraxway.thirdpartyapi.openweathermap.UponDemand;

public class OpenWeatherMapTest {

	public static void main(String[] args) {
		new OpenWeatherMapTest();
	}

	public OpenWeatherMapTest() {
//		IndependentWorker owmAPI=new IndependentWorker();
//		try {
//			Thread.sleep(20000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		owmAPI.stopIt();

		UponDemand api = UponDemand.getInstance();
		DayList dayList = api.getDataNext5Days("42.7", "23.3333");
//		DayList dayList = api.getDataNext5Days("42.6833", "23.3167");

		System.out.println("cityName="+api.getCityName());
		System.out.println(dayList.toString());



	}
}
