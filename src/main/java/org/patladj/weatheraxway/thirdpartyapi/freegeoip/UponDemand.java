package org.patladj.weatheraxway.thirdpartyapi.freegeoip;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class UponDemand {

	private static HttpClient HTTP_CLIENT = new DefaultHttpClient();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	static {
		try {
			// Add a handler to handle unknown properties (in case the API adds new properties to the response)
			MAPPER.addHandler(new DeserializationProblemHandler() {

				@Override
				public boolean handleUnknownProperty(DeserializationContext context,
													 JsonParser jp, JsonDeserializer<?> deserializer,
													 Object beanOrClass, String propertyName) throws IOException {
					// Do not fail - just log
					String className = (beanOrClass instanceof Class) ?
							((Class) beanOrClass).getName() : beanOrClass.getClass().getName();
					System.out.println("Unknown property while de-serializing: " +
							className + "." + propertyName);
					context.getParser().skipChildren();
					return true;
				}
			});
		}
		catch (Throwable t) { /*Ignore*/ }
	}

	public static IPInfo getIPInfo(HttpServletRequest request) {
		String url = "http://freegeoip.net/json/" + getClientIP(request); // Using the API
		IPInfo ipResponse = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = HTTP_CLIENT.execute(httpGet, new BasicHttpContext());
			String responseString;
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("Sorry! Response Error. Status Code: " + httpResponse.getStatusLine().getStatusCode());
			}
			responseString = EntityUtils.toString(httpResponse.getEntity());
			ipResponse = MAPPER.readValue(responseString, IPInfo.class);
			return ipResponse;
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return ipResponse;
	}

	public static IPInfo getIPInfo(String ip) {
		String url = "http://freegeoip.net/json/" + ip; // Using the API
		IPInfo ipInfo = null;
		try {
			HTTP_CLIENT = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = HTTP_CLIENT.execute(httpGet, new BasicHttpContext());
			String responseString;
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("Sorry! Response Error. Status Code: " + httpResponse.getStatusLine().getStatusCode());
			}
			responseString = EntityUtils.toString(httpResponse.getEntity());
			ipInfo = MAPPER.readValue(responseString, IPInfo.class);
			return ipInfo;
		} catch (Exception ex) {
			System.out.println(ex.toString());
		} finally {
			HTTP_CLIENT.getConnectionManager().shutdown();
		}
		return ipInfo;
	}

	public static String getClientIP(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");

		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}

		return ipAddress;
	}

	public static class IPInfo {

		private String ip;
		private String country_code;
		private String country_name;
		private String region_code;
		private String region_name;
		private String city;
		private String zipcode;
		private Double latitude;
		private Double longitude;
		private String metro_code;
		private String area_code;

		/**
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}

		/**
		 * @param ip the ip to set
		 */
		public void setIp(String ip) {
			this.ip = ip;
		}

		/**
		 * @return the country_code
		 */
		public String getCountry_code() {
			return country_code;
		}

		/**
		 * @param country_code the country_code to set
		 */
		public void setCountry_code(String country_code) {
			this.country_code = country_code;
		}

		/**
		 * @return the country_name
		 */
		public String getCountry_name() {
			return country_name;
		}

		/**
		 * @param country_name the country_name to set
		 */
		public void setCountry_name(String country_name) {
			this.country_name = country_name;
		}

		/**
		 * @return the region_code
		 */
		public String getRegion_code() {
			return region_code;
		}

		/**
		 * @param region_code the region_code to set
		 */
		public void setRegion_code(String region_code) {
			this.region_code = region_code;
		}

		/**
		 * @return the region_name
		 */
		public String getRegion_name() {
			return region_name;
		}

		/**
		 * @param region_name the region_name to set
		 */
		public void setRegion_name(String region_name) {
			this.region_name = region_name;
		}

		/**
		 * @return the city
		 */
		public String getCity() {
			return city;
		}

		/**
		 * @param city the city to set
		 */
		public void setCity(String city) {
			this.city = city;
		}

		/**
		 * @return the zipcode
		 */
		public String getZipcode() {
			return zipcode;
		}

		/**
		 * @param zipcode the zipcode to set
		 */
		public void setZipcode(String zipcode) {
			this.zipcode = zipcode;
		}

		/**
		 * @return the latitude
		 */
		public Double getLatitude() {
			return latitude;
		}

		/**
		 * @param latitude the latitude to set
		 */
		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}

		/**
		 * @return the longitude
		 */
		public Double getLongitude() {
			return longitude;
		}

		/**
		 * @param longitude the longitude to set
		 */
		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}

		/**
		 * @return the metro_code
		 */
		public String getMetro_code() {
			return metro_code;
		}

		/**
		 * @param metro_code the metro_code to set
		 */
		public void setMetro_code(String metro_code) {
			this.metro_code = metro_code;
		}

		/**
		 * @return the area_code
		 */
		public String getArea_code() {
			return area_code;
		}

		/**
		 * @param area_code the area_code to set
		 */
		public void setArea_code(String area_code) {
			this.area_code = area_code;
		}

		@Override
		public String toString() {
			return "IPInfo{"
					+ "\"ip\":\"" + ip + "\""
					+ ",\"country_code\":\"" + country_code + "\""
					+ ",\"country_name\":\"" + country_name + "\""
					+ ",\"region_code\":\"" + region_code + "\""
					+ ",\"region_name\":\"" + region_name + "\""
					+ ",\"city\":\"" + city + "\""
					+ ",\"zipcode\":\"" + zipcode + "\""
					+ ",\"latitude\":" + latitude
					+ ",\"longitude\":" + longitude
					+ ",\"metro_code\":\"" + metro_code + "\""
					+ ",\"area_code\":\"" + area_code + "\""
					+ '}';
		}
	}
}