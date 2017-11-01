package org.patladj.weatheraxway.controller.geoipseek;

import org.patladj.weatheraxway.model.IP2Location;
import org.patladj.weatheraxway.thirdpartyapi.freegeoip.UponDemand;
//import org.patladj.weatheraxway.thirdpartyapi.freegeoip.UponDemand;

/**
 * Created by PatlaDJ on 28.10.2017 г..
 */
public class GeoIPSeeker implements Runnable {
	private volatile Thread theThread;
	private volatile IP2Location ip2Location=null;

	public IP2Location getIp2Location() {
		return ip2Location;
	}

	public static synchronized GeoIPSeeker getAnotherInstance() {
		return new GeoIPSeeker();
	}

	private GeoIPSeeker() {
		this.theThread=new Thread(this, "GeoIPSeeker "+Math.random());
		this.theThread.start();
	}

	private static final int CMD_GET_LOCATION_FORIP = 1;
	private volatile int currentCMD=0;
	private volatile Object currentNotifyWhenCompleted =null;
	private volatile String currentIp=null;

	private volatile boolean isRunning=true;

	public void initiateObtainLocationForIP_NonBlocking(String ip, Object notifyWhenCompleted) {
		this.currentCMD=CMD_GET_LOCATION_FORIP;
		this.currentIp=ip;
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
				case CMD_GET_LOCATION_FORIP:
					this.obtainLocationForIP(this.currentIp);
					if (this.currentNotifyWhenCompleted!=null) {
						synchronized (this.currentNotifyWhenCompleted) {
							this.currentNotifyWhenCompleted.notifyAll();
						}
					}
					this.currentCMD=0;
					this.currentNotifyWhenCompleted=null;
					this.currentIp=null;
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

	private void obtainLocationForIP(String ip) {
		System.out.println("Method obtainLocationForIP executed at "+System.currentTimeMillis());
		UponDemand.IPInfo ipinfo = UponDemand.getIPInfo(ip);
		IP2Location iploc=new IP2Location(ip, ipinfo.getCity(), ipinfo.getCountry_name(), ipinfo.getLatitude()+"", ipinfo.getLongitude()+"");

//		String statusLine="";
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet("http://www.websitedevguide.com/about-nikolay-tokludzhanov");
//		CloseableHttpResponse response1 = null;
//		try {
//			response1 = httpclient.execute(httpGet);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// The underlying HTTP connection is still held by the response object
//		// to allow the response content to be streamed directly from the network socket.
//		// In order to ensure correct deallocation of system resources
//		// the user MUST call CloseableHttpResponse#close() from a finally clause.
//		// Please note that if response content is not fully consumed the underlying
//		// connection cannot be safely re-used and will be shut down and discarded
//		// by the connection manager.
//		try {
//			statusLine=response1.getStatusLine()+"";
//			System.out.println(statusLine);
//			HttpEntity entity1 = response1.getEntity();
//			// do something useful with the response body
//			// and ensure it is fully consumed
//
//			EntityUtils.consume(entity1);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				response1.close();
//			} catch (IOException e) {  }
//		}



//		IP2Location iploc=new IP2Location(ip, statusLine, "", "", "");
		this.ip2Location=iploc;
		System.out.println("Method obtainLocationForIP finished execution at "+System.currentTimeMillis());
	}

	public synchronized void disposeIt() {
		this.isRunning=false;
		this.theThread.interrupt();
	}
}
