package org.patladj.weatheraxway.tests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.patladj.weatheraxway.thirdpartyapi.freegeoip.UponDemand;

import java.io.IOException;

/**
 * Created by PatlaDJ on 24.10.2017 г..
 */
public class TestFreeGeoIP {
	public static void main(String[] args) {
		testFreeGeoIP();
	}

	private static void testFreeGeoIP() {
		UponDemand.IPInfo ipinfo = UponDemand.getIPInfo("85.118.83.127");

		System.out.println("lat=|"+ipinfo.getLatitude()+"|, long=|"+ipinfo.getLongitude()+"|");


		ipinfo = UponDemand.getIPInfo("104.27.155.211");

		System.out.println("lat=|"+ipinfo.getLatitude()+"|, long=|"+ipinfo.getLongitude()+"|");


		ipinfo = UponDemand.getIPInfo("194.145.63.12");

		System.out.println("lat=|"+ipinfo.getLatitude()+"|, long=|"+ipinfo.getLongitude()+"|");



		/*String statusLine="";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://www.websitedevguide.com/about-nikolay-tokludzhanov");
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager.
		try {
			statusLine=response1.getStatusLine()+"";
			System.out.println(statusLine);
			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed

			EntityUtils.consume(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response1.close();
			} catch (IOException e) {  }
		}

*/

	}

}
