package launch;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;

import java.io.File;

public class Main {
    
    public static String PORT = System.getenv("PORT");
    public static String HOSTNAME = System.getenv("HOSTNAME");
    
    public static void main(String[] args) throws Exception {
        if (PORT==null || PORT.length()==0) PORT="8080";
        if (HOSTNAME==null || HOSTNAME.length()==0) HOSTNAME="8080";

        Tomcat tomcat = new Tomcat();
        String webappDirLocation = "src/main/webapp/";
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/",
                new File(webappDirLocation).getAbsolutePath());

//declare an alternate location for your "WEB-INF/classes" dir:
        File additionWebInfClasses = new File("/target/classes");
        VirtualDirContext resources = new VirtualDirContext();
        resources.setExtraResourcePaths("" + additionWebInfClasses);
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}