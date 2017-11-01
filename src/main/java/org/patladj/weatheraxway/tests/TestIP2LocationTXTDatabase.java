package org.patladj.weatheraxway.tests;

import org.patladj.weatheraxway.database.drivers.TextfileDatabaseIP2Location;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by PatlaDJ on 27.10.2017 г..
 */
public class TestIP2LocationTXTDatabase {
	public static void main(String[] args) {
//		writeTestData();
//
//		TextfileDatabaseIP2Location anotherInstance = TextfileDatabaseIP2Location.getAnotherInstance();
//		Map<String,HashMap<String,String>> rdtm = anotherInstance.readData();
//		HashMap<String,String> ipprops = new HashMap<String,String>();
//		ipprops.put("country", "Nicaragua");
//		ipprops.put("city", "Solnich");
//		ipprops.put("latlong", "21.7|24.88");
//		rdtm.put("113.8.88.88", ipprops);
//
//		anotherInstance.writeData(rdtm);
//		anotherInstance.instanceDispose();


		readTestData();


//		Map<String,HashMap<String,String>> rdtm = TextfileDatabaseIP2Location.getAnotherInstance().readData();
	}

	private static void readTestData() {
		TextfileDatabaseIP2Location anotherInstance = TextfileDatabaseIP2Location.getAnotherInstance();
		Map<String,HashMap<String,String>> rdtm = anotherInstance.readData();
		System.out.println("rdtm=|"+rdtm+"|");

		Iterator it2 = rdtm.entrySet().iterator();
		while (it2.hasNext()) {
			Map.Entry pair2 = (Map.Entry)it2.next();
			System.out.println("\n	ip: |"+pair2.getKey() + "|:");

			Iterator it3 = ((Map<String,String>)pair2.getValue()).entrySet().iterator();
			while (it3.hasNext()) {
				Map.Entry pair3 = (Map.Entry)it3.next();
				System.out.println("\n			|"+pair3.getKey() + "| = " + "|"+pair3.getValue()+"|");
			}

			System.out.println("\n");

		}

		anotherInstance.instanceDispose();
	}


	private static final void writeTestData() {
		//Test Writing to DB


		HashMap<String,HashMap<String,String>> ips=null;




		ips = new HashMap<String,HashMap<String,String>>();

		//IP #1
		HashMap<String,String> ipprops = new HashMap<String,String>();

		ipprops.put("country", "Bulgaria");
		ipprops.put("city", "Sofia");
		ipprops.put("latlong", "42.7|23.3333");
		ips.put("85.118.83.127", ipprops);

		//IP #2
		ipprops = new HashMap<String,String>();

		ipprops.put("country", "Bulgaria");
		ipprops.put("city", "Varna");
		ipprops.put("latlong", "40.1|21.417");
		ips.put("41.201.45.202", ipprops);


		//IP #3
		ipprops = new HashMap<String,String>();

		ipprops.put("country", "Germany");
		ipprops.put("city", "Munich");
		ipprops.put("latlong", "48.1|22.17");
		ips.put("73.110.14.107", ipprops);

		TextfileDatabaseIP2Location wtFileDb = TextfileDatabaseIP2Location.getAnotherInstance();
		wtFileDb.writeData(ips);
		wtFileDb.instanceDispose();

	}

}
