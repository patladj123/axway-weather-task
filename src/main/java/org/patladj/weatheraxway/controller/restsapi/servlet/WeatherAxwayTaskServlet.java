
package org.patladj.weatheraxway.controller.restsapi.servlet;

import org.patladj.weatheraxway.controller.restsapi.RestRequestGet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author PatlaDJ
 *
 */
@WebServlet(
		name = "WeatherAxwayTaskServlet",
		urlPatterns = {"/weatheraxway-rest-service/*"}
)
public class WeatherAxwayTaskServlet extends HttpServlet {

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        //this is a test method, never used
//
//        TestModel tm=new TestModel();
//
//        String json = new Gson().toJson(tm);
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().write(json);
//
//
////    	String nextJSP = "/jsp/weather-axway-task.jsp";
////        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
////        dispatcher.forward(req, resp);
//    }



	@Override
	//Restful API only includes handling GET with different URIs, and responds respectful data for it. I.e. /weatheraxway-rest-service/get-data-for-ip/[theIPAddress] will respond all the weather data for the requested IP address
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter out = resp.getWriter();

		System.out.println("GET request handling");
//		out.println("req.getRequestURI()=|"+req.getRequestURI()+"|");

		RestRequestGet resourceValues = new RestRequestGet(req.getRequestURI(), out, resp);

//		out.println("resourceValues.getIp()=|"+resourceValues.getIp()+"|");


		out.close();
	}

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        YourClass obj = new Gson().fromJson(req.getReader(), YourClass.class);
//
//
//        Type type = new TypeToken<Map<String, String>>(){}.getType();
//        Map<String, String> myMap = new Gson().fromJson("{'k1':'apple','k2':'orange'}", type);
//
//    }

}
