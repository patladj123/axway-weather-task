## Weather Axway Task
Is still in alpha version. Please stay tuned for immediate updates.


Online demos:

REST API:
http://www.websitedevguide.com:8080/weatheraxway-rest-service/get-data-for-ip/[IP_address_you_desire_a_weather_forcast_for]

Front-End (Still under development):
http://www.websitedevguide.com:8080/


Version 0.1 released.

Requirements for stand alone build & run (uses embedded Tomcat 7):
 - Java 1.7+
 - Maven 3+
 - Available network port 8080
 
Build & run HOWTO. Stand alone. (No need of app server. Uses embedded Tomcat 7):
 - mvn clean compile
 - mvn package
 - cd target
 - java -jar weather-axwaytask-app-1.0-SNAPSHOT-jar-with-dependencies.jar
 - Navigate your browser to http://localhost:8080


If you want to run it IDE, yet using Maven:
Basically if you want to run it on non-embedded Tomcat you need to open the pom.xml and uncomment each <scope>provided</scope> in the "Begin of Apache 7 embedded" section.
Tomcat 7 is a requirement for the IDE app server in this case.

