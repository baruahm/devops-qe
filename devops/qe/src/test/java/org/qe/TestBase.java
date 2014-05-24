package org.qe;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class TestBase {

    String appIP = getPropValues("appIP");
    String appPort = getPropValues("port");
    String appPath = getPropValues("appPath");

    InetAddress inet;

    public TestBase() {
        try {
            inet = InetAddress.getByName(appIP);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getPropValues(String key) {

        Properties prop = new Properties();
        String propFileName = "app.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        if (inputStream == null) {
            System.out.println("property file '" + propFileName + "' not found in the classpath");
        }
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            System.out.println("Failed to fetch the value from properties file for key: "+key);
            e.printStackTrace();
        }

        // get the property value and print it out
        return prop.getProperty(key);
    }
}