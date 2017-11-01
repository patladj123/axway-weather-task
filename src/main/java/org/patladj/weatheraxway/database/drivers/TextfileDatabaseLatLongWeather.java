package org.patladj.weatheraxway.database.drivers;


import org.patladj.weatheraxway.exceptions.UnableToOpenFileForIOException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PatlaDJ on 21.10.2017 г..
 */
public class TextfileDatabaseLatLongWeather {
	private BufferedWriter writer;
	private static String theFileName;
	private static final Pattern MATCH_LATLONG= Pattern.compile("^(.+?)\\[\\[\\[LATLONG\\]\\]\\]\\s*$",Pattern.DOTALL);
	private static final Pattern MATCH_EOLATLONG= Pattern.compile("^\\[\\[\\[EOLATLONG\\]\\]\\]\\s*$",Pattern.DOTALL);
	private static final Pattern MATCH_DATE= Pattern.compile("^(.+?)\\[\\[\\[DATE\\]\\]\\]\\s*$",Pattern.DOTALL);
	private static final Pattern MATCH_END_OF_DATE= Pattern.compile("^\\[\\[\\[EODATE\\]\\]\\]\\s*$",Pattern.DOTALL);
	private static final Pattern MATCH_DATE_PROP= Pattern.compile("^(.+?)\\{\\{\\{EQUALS\\}\\}\\}(.*?)\\{\\{\\{EOL\\}\\}\\}\\s*$",Pattern.DOTALL);
	private Map<String, HashMap<String, HashMap<String, String>>> theData;

	private volatile boolean isInstanceDisposed=false;

	private static Object lock = new Object();
	private static volatile boolean isThereAnInstance=false;

	private TextfileDatabaseLatLongWeather() {
		theFileName = "location2weather-data.db.txt";
		this.theData=new HashMap<String, HashMap<String, HashMap<String, String>>>();
		this.actualReadData(this.theData);
	}

	public static synchronized TextfileDatabaseLatLongWeather getAnotherInstance() {
		//Prevent simultaneous usage of more than 1 instance of this class within the app server. Needed for ensuring no simultaneous read/write opps to the underlying txt database
		synchronized (lock) {
			if (!isThereAnInstance) {
				return createTheInstance();
			}
			else {
				try {
					lock.wait();
					return createTheInstance();
				} catch (InterruptedException e) {
					throw new RuntimeException("InterruptedException is thrown while waiting to obtain a lock on the TextfileDatabaseLatLongWeather ("+theFileName+")");
				}
			}
		}
	}

	private static TextfileDatabaseLatLongWeather createTheInstance() {
		isThereAnInstance=true;
		return new TextfileDatabaseLatLongWeather();
	}

	public synchronized void instanceDispose() {
		actualWriteData(this.theData);
		this.isInstanceDisposed=true;

		//Prevent simultaneous usage of more than 1 instance of this class within the app server. Needed for ensuring no simultaneous read/write opps to the underlying txt database
		synchronized (lock) {
			lock.notifyAll();
			isThereAnInstance=false;
		}
	}

	public synchronized void writeData(Map<String,HashMap<String,HashMap<String,String>>> data) {
		this.theData=data;
		if (this.isInstanceDisposed) actualWriteData(this.theData);
	}

	private void actualWriteData(Map<String, HashMap<String, HashMap<String, String>>> data) {
		try {
			openFileForWrite();
		} catch (UnableToOpenFileForIOException e) {
			e.printStackTrace();
			return;
		}

		try {
			Iterator it0 = data.entrySet().iterator();
			while (it0.hasNext()) {
				Map.Entry pair0 = (Map.Entry)it0.next();
				writer.write(pair0.getKey()+ "[[[LATLONG]]]"+System.getProperty("line.separator"));

				Iterator it1 = ((Map<String,String>)pair0.getValue()).entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry pair2 = (Map.Entry)it1.next();
					writer.write(pair2.getKey()+ "[[[DATE]]]"+System.getProperty("line.separator"));

					Iterator it2 = ((Map<String,String>)pair2.getValue()).entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry pair3 = (Map.Entry) it2.next();
						writer.write(pair3.getKey() + "{{{EQUALS}}}" + pair3.getValue() + "{{{EOL}}}" + System.getProperty("line.separator"));
					}

					writer.write("[[[EODATE]]]"+System.getProperty("line.separator"));
				}
				writer.write("[[[EOLATLONG]]]"+System.getProperty("line.separator"));
			}
		}
		catch (IOException oie) {
			closeFile();
			oie.printStackTrace();
		}
		finally {
			closeFile();
		}
	}


	private void ifTheresNoFileCreateIt() {
		File f = new File(theFileName);
		if(!f.exists() && !f.isDirectory())
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized Map<String,HashMap<String,HashMap<String,String>>> readData() {
		if (this.isInstanceDisposed) {
			if (actualReadData(theData)) return null; /* true returned from actualReadData actually means an error reading */
		}

		return theData;
	}

	private boolean actualReadData(Map<String, HashMap<String, HashMap<String, String>>> data) {
		this.ifTheresNoFileCreateIt();

		try (BufferedReader br = new BufferedReader(new FileReader(theFileName))) {

			String line;
			while ((line = br.readLine()) != null) {
				Matcher m1 = null;
				if (null != (m1 = MATCH_LATLONG.matcher(line)) && m1.find()) {

					HashMap<String, HashMap<String, String>> iteratingLatLong = new HashMap<String, HashMap<String, String>>();
					data.put(m1.group(1), iteratingLatLong);

					while ((line = br.readLine()) != null) {
						Matcher m2 = null;
						if ( null != (m2=MATCH_EOLATLONG.matcher(line)) && m2.find() ) {
							break;
						}
						else if (null != (m2 = MATCH_DATE.matcher(line)) && m2.find()) {
							HashMap<String, String> iteratingDate = new HashMap<String, String>();
							iteratingLatLong.put(m2.group(1), iteratingDate);

							while ((line = br.readLine()) != null) {
								Matcher m3 = null;
								if ( null != (m3=MATCH_END_OF_DATE.matcher(line)) && m3.find() ) {
									break;
								}
								else if ( null != (m3=MATCH_DATE_PROP.matcher(line)) && m3.find() ) {
									iteratingDate.put(m3.group(1), m3.group(2));
								}
							}
						}
					}
				}
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			return true;
		}
		finally {
			closeFile();
			this.theData=data;
		}
		return false;
	}

	private void openFileForWrite() throws UnableToOpenFileForIOException {
		File dbFile = new File(theFileName);

		// This will output the full path where the file will be written to...
		try {
			System.out.println("Writing to "+ dbFile.getCanonicalPath()+"... ");
			writer = new BufferedWriter(new FileWriter(dbFile,false));
		} catch (IOException e) {
			closeFile();
			e.printStackTrace();
			throw new UnableToOpenFileForIOException();
		}


	}


	private void closeFile() {
		try {
			writer.close();
		}
		catch (Throwable t) { /*Ignore*/ }
	}



}
