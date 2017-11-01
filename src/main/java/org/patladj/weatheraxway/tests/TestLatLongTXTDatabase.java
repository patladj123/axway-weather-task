package org.patladj.weatheraxway.tests;

import org.patladj.weatheraxway.database.drivers.TextfileDatabaseLatLongWeather;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by PatlaDJ on 24.10.2017 г..
 */
public class TestLatLongTXTDatabase {
	public static void main(String[] args) {
		writeTestData();

//		TextfileDatabaseLatLongWeather anotherInstance = TextfileDatabaseLatLongWeather.getAnotherInstance();
//		//Latlong #1
//		Map<String,HashMap<String,HashMap<String,String>>> latlongs = anotherInstance.readData();
//		HashMap<String,HashMap<String,String>> dates = new HashMap<String,HashMap<String,String>>();
//
//		latlongs.put("411.7|4.090909",dates);
//		HashMap<String, String> props = new HashMap<String, String>();
//		props.put("minTemp", "1226C");
//		props.put("maxTemp", "6662677C");
//		dates.put("2017-10-28", props);
//
//
//		props = new HashMap<String, String>();
//		props.put("minTemp", "111112C");
//		props.put("maxTemp", "222222C");
//		dates.put("2017-10-23", props);
//
//
//		props = new HashMap<String, String>();
//		props.put("minTemp", "111111C");
//		props.put("maxTemp", "3333321C");
//		dates.put("2017-10-21", props);
//
//		anotherInstance.instanceDispose();


//		readTestData();
	}

	private static void readTestData() {
		TextfileDatabaseLatLongWeather anotherInstance = TextfileDatabaseLatLongWeather.getAnotherInstance();
		Map<String,HashMap<String,HashMap<String,String>>> rdtm = anotherInstance.readData();

		Iterator it = rdtm.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println("\nlatlong: |"+pair.getKey() + "|: \n");

			Iterator it2 = ((Map<String,HashMap<String,String>>)pair.getValue()).entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry pair2 = (Map.Entry)it2.next();
				System.out.println("\n	date: |"+pair2.getKey() + "|:");

				Iterator it3 = ((Map<String,String>)pair2.getValue()).entrySet().iterator();
				while (it3.hasNext()) {
					Map.Entry pair3 = (Map.Entry)it3.next();
					System.out.println("\n			|"+pair3.getKey() + "| = " + "|"+pair3.getValue()+"|");
				}

				System.out.println("\n");

			}

			System.out.println("\n\n");

		}

		anotherInstance.instanceDispose();
	}


	private static final void writeTestData() {
		//Test Writing to DB


		HashMap<String, HashMap<String,HashMap<String,String>>> latlongs = new HashMap<String, HashMap<String,HashMap<String,String>>>();
		HashMap<String,HashMap<String,String>> dates=null;





		//Latlong #1
		dates = new HashMap<String,HashMap<String,String>>();

		latlongs.put("42.7|23.3333",dates);
		HashMap<String, String> props = new HashMap<String, String>();
		props.put("minTemp", "16C");
		props.put("maxTemp", "2677C");
		dates.put("2017-10-24", props);


		props = new HashMap<String, String>();
		props.put("minTemp", "12C");
		props.put("maxTemp", "22C");
		dates.put("2017-10-25", props);


		props = new HashMap<String, String>();
		props.put("minTemp", "11C");
		props.put("maxTemp", "21C");
		dates.put("2017-10-26", props);




		//Latlong #2
		dates = new HashMap<String,HashMap<String,String>>();

		latlongs.put("41.7|21.1",dates);
		props = new HashMap<String, String>();
		props.put("minTemp", "11C");
		props.put("maxTemp", "2377C");
		dates.put("2017-10-24", props);


		props = new HashMap<String, String>();
		props.put("minTemp", "33C");
		props.put("maxTemp", "21C");
		dates.put("2017-10-25", props);


		props = new HashMap<String, String>();
		props.put("minTemp", "11C");
		props.put("maxTemp", "21C");
		dates.put("2017-10-26", props);









		//Latlong #3
		dates = new HashMap<String,HashMap<String,String>>();

		latlongs.put("48.7|41.1",dates);
		props = new HashMap<String, String>();
		props.put("minTemp", "41C");
		props.put("maxTemp", "1377C");
		dates.put("2017-10-24", props);


		props = new HashMap<String, String>();
		props.put("minTemp", "28C");
		props.put("maxTemp", "321C");
		dates.put("2017-10-25", props);


		props = new HashMap<String, String>();
		props.put("minTemp", "17C");
		props.put("maxTemp", "31C");
		dates.put("2017-10-26", props);


		TextfileDatabaseLatLongWeather wtFileDb = TextfileDatabaseLatLongWeather.getAnotherInstance();
		wtFileDb.writeData(latlongs);
		wtFileDb.instanceDispose();

	}

}
