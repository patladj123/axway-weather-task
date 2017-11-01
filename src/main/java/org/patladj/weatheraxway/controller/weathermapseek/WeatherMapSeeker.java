package org.patladj.weatheraxway.controller.weathermapseek;

import org.patladj.weatheraxway.model.DayList;

/**
 * Created by PatlaDJ on 29.10.2017 г..
 */
public class WeatherMapSeeker implements Runnable {
	private volatile Thread theThread;

	private volatile DayList dayList=null;
	private volatile String cityName=null;

	public DayList getDayList() {
		return dayList;
	}
	public String getCityName() {
		return cityName;
	}

	public static synchronized WeatherMapSeeker getAnotherInstance() {
		return new WeatherMapSeeker();
	}

	private WeatherMapSeeker() {
		this.theThread=new Thread(this, "WeatherMapSeeker "+Math.random());
		this.theThread.start();
	}

	private static final int CMD_GET_5_DAYS_WEATHER_DATA_FORLATLONG = 1;
	private volatile int currentCMD=0;
	private volatile Object currentNotifyWhenCompleted =null;
	private volatile String currentLatitude =null;
	private volatile String currentLongtitude =null;

	private volatile boolean isRunning=true;

	public void initiateObtainWeatherDataForLatLong_NonBlocking(String latitude, String longtitude, Object notifyWhenCompleted) {
		this.currentCMD=CMD_GET_5_DAYS_WEATHER_DATA_FORLATLONG;
		this.currentLatitude =latitude;
		this.currentLongtitude =longtitude;
		this.currentNotifyWhenCompleted=notifyWhenCompleted;
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	public void run() {
		System.out.println("Thread |"+this.theThread.getName()+"| has started at "+System.currentTimeMillis());
		while (this.isRunning) {
			switch (currentCMD) {
				case CMD_GET_5_DAYS_WEATHER_DATA_FORLATLONG:
					this.obtain5DaysWeatherDataForLatLong(this.currentLatitude, this.currentLongtitude);
					if (this.currentNotifyWhenCompleted!=null) {
						synchronized (this.currentNotifyWhenCompleted) {
							this.currentNotifyWhenCompleted.notifyAll();
						}
					}
					this.currentCMD=0;
					this.currentNotifyWhenCompleted=null;
					this.currentLatitude =null;
					this.currentLongtitude =null;
					break;
				default:
					currentCMD=0;
					break;
			}
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					this.isRunning=false;
				}
			}
		}

		System.out.println("Thread |"+this.theThread.getName()+"| exited safely at "+System.currentTimeMillis());
	}

	private void obtain5DaysWeatherDataForLatLong(String latitude, String longtitude) {
		System.out.println("Method obtain5DaysWeatherDataForLatLong executed at "+System.currentTimeMillis());

		org.patladj.weatheraxway.thirdpartyapi.openweathermap.UponDemand api = org.patladj.weatheraxway.thirdpartyapi.openweathermap.UponDemand.getInstance();

		this.dayList=api.getDataNext5Days(latitude, longtitude); //You must always invoke first this method before getCityName(), if you want to receive correct city/neighbourhood name
		this.cityName=api.getCityName();

		System.out.println("Method obtain5DaysWeatherDataForLatLong finished execution at "+System.currentTimeMillis());
	}

	public synchronized void disposeIt() {
		this.isRunning=false;
		this.theThread.interrupt();
	}
}
