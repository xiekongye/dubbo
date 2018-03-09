import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务启动
 * Create by kyxie on 2018/3/8 16:34
 */
public class ServiceStarter {
    private static final Logger log = LoggerFactory.getLogger(ServiceStarter.class);

    private static final String KEY_SERVER_PORT = "server.port";
    private static final String DEFAULT_PORT = "8080";
    private static final String APP_PROPERTIES = "META-INF/app.properties";
    private static final String APPLICATION_PROPERTIES = "application.properties";
    private static final String TARGET_DIR = "target/classes";

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");

        String workingDir = getWorkingDir();
        int port = getPort();

        log.info("Starting tomcat with port {}(workingDir={})", port, workingDir);

        Tomcat tomcat = startTomcat(workingDir, port);
        Desktop.getDesktop().browse(new URI(String.format("http://127.0.0.1:%s/api", port)));

        tomcat.getServer().await();
    }

    private static Tomcat startTomcat(String workingDir, int port)
            throws ServletException, LifecycleException, IOException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        Path tmpDir = Files.createTempDirectory(String.format("tomcat-%s-", port));
        tmpDir.toFile().deleteOnExit();
        tomcat.setBaseDir(tmpDir.toString());

        StandardContext ctx =
                (StandardContext) tomcat.addWebapp("", new File(workingDir, "src/main/webapp").getAbsolutePath());

        File additionWebInfClasses = new File(workingDir, TARGET_DIR);
        VirtualDirContext resources = new VirtualDirContext();
        resources.setExtraResourcePaths("/WEB-INF/classes=" + additionWebInfClasses);
        ctx.setResources(resources);

        tomcat.start();
        return tomcat;
    }

    private static String getWorkingDir() throws Exception {
        URL url = ServiceStarter.class.getClassLoader().getResource(APP_PROPERTIES);
        if (url != null) {
            Path path = Paths.get(url.toURI());
            String absPath = path.toAbsolutePath().toString();
            return absPath.substring(0, absPath.length() - (TARGET_DIR + "/" + APP_PROPERTIES).length());
        } else {
            throw new NullPointerException(String.format("%s not found", APP_PROPERTIES));
        }
    }

    private static int getPort() throws Exception {
        Properties prop = new Properties();
        try {
            prop.load(ServiceStarter.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES));
            return Integer.parseInt(prop.getProperty(KEY_SERVER_PORT, DEFAULT_PORT));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Exception occurred while loading %s from %s", KEY_SERVER_PORT, APPLICATION_PROPERTIES), e);
        }
    }

}
