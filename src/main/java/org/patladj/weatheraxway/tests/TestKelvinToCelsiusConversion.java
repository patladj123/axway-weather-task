package org.patladj.weatheraxway.tests;

/**
 * Created by PatlaDJ on 25.10.2017 г..
 */

import org.patladj.weatheraxway.util.Util;

public class TestKelvinToCelsiusConversion {

	public static void main(String[] args) {
		new TestKelvinToCelsiusConversion();
	}

	public TestKelvinToCelsiusConversion() {
		System.out.println(Util.kelvinToCelsiusRoundedToPrecisionOne_FromString("281.96"));
	}
}
