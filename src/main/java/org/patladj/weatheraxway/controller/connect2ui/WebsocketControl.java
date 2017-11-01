package org.patladj.weatheraxway.controller.connect2ui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;


/**
 * This is the websocket which is used for receiving and sending messages from and to the front-end part
 * (I will write the FE using React and JSON messaging between this socket and the FE using a constantly open websocket for quickest IU/Backend responsiveness possible)
 * @author PatlaDJ
 *
 */
@SuppressWarnings("deprecation")
@ServerEndpoint("/weather-axway-task-ws-opps")
public class WebsocketControl extends WebSocketServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 110283178240290856L;

	/**
	 * List with all sessions (clients) which are connected using from the webpage interface
	 */
    private static ArrayList<Session> sessionList = new ArrayList<Session>();
    
    /**
     * Parsing incoming message string by using this pattern
     */
    private static Pattern SEP_INCOMING_MESSAGE=Pattern.compile("^([^\\s]+)\\s+(.*)", Pattern.DOTALL);

    
    /**
     * Things that are done when a new client connects on the webpage
     * @param session Received new session
     */
    @OnOpen
    public void onOpen(Session session) {
    	synchronized (getClass()) {
            sessionList.add(session);
    	}
    }
    
    /**
     * Things that are done where a client is disconnected from the webpage
     * @param session Given session which this client had
     */
    @OnClose
    public void onClose(Session session){
        synchronized (getClass()) {
        	sessionList.remove(session);
        }
    }
    
    /**
     * Registers a new message received on the websocket for this particular client
     * @param msg The string message
     */
    @OnMessage
    public void onMessage(String msg) {

    }


	public void sendData2SpecificSession(String jsonString, Session session) {
		synchronized (getClass()) {
			try {
				session.getBasicRemote().sendText(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }



	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}