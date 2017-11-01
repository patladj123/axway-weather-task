package org.patladj.weatheraxway.thirdpartyapi.openweathermap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

/**
 * Created by PatlaDJ on 21.10.2017 г..
 */
public class IndependentWorker implements Runnable {
	private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private Thread theThread;
	private volatile boolean isRunning;

	public IndependentWorker() {
		this.isRunning=true;
		this.theThread=new Thread(this, "openWeatherMapWorker Thread");
		this.theThread.start();
	}

	@Override
	public void run() {
		while (this.isRunning && !this.theThread.isInterrupted()) {
			System.out.println("One iteration to get the weather data for all previously requested locations, so we have them cached. will not be on 6 seconds, will be on every 3hrs while the app server is running...");

			//Utilize the OpenWeatherMap API
			String url = "http://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}"; // Using the API
			try {
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = HTTP_CLIENT.execute(httpGet, new BasicHttpContext());
				String responseString;
				if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					throw new RuntimeException("Sorry! Response Error. Status Code: " + httpResponse.getStatusLine().getStatusCode());
				}
				responseString = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("responseString=|"+responseString+"|\n");
			} catch (Exception ex) {
				System.out.println(ex.toString());
			} finally {
				HTTP_CLIENT.getConnectionManager().shutdown();
			}

			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				this.isRunning=false;
			}
		}
	}

	public void stopIt() {
		this.isRunning=false;
		this.theThread.interrupt();
	}
}
