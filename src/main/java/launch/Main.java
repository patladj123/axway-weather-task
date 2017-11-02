/* Copyright 2015 Oracle and/or its affiliates. All rights reserved. */
package launch;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;

import java.io.File;

public class Main {

    public static String PORT = System.getenv("PORT");
    public static String HOSTNAME = System.getenv("HOSTNAME");

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "../src/main/webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/",
                new File(webappDirLocation).getAbsolutePath());

//declare an alternate location for your "WEB-INF/classes" dir:
        File additionWebInfClasses = new File("classes");
        VirtualDirContext resources = new VirtualDirContext();
        resources.setExtraResourcePaths("/WEB-INF/classes=" + additionWebInfClasses);
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}