package org.qe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppTest extends TestBase{
    URL url = null;
    HttpURLConnection connection;
    int code = 0;

    @Test
    public void testDeploymentMachineReachable(){
        System.out.println("Testing if app deployment machine is reachable: "+getPropValues("appIP"));
        try {
            Assert.assertTrue(inet.isReachable(500), "App machine is not reachable");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testAppRunning(){
        try {
            url = new URL("http://"+appIP+":"+appPort+"/"+appPath);
            System.out.println("Testing URL: "+url);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
            System.out.println("Reponse code is: "+code);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertTrue((code == 200), "Application is not running on the URL"+url);
    }

    //This test is meant to fail if app is deployed on non 8080 port. It will pass
    //if deployed on 8081 port
    @Test
    public void testAppRunningOn8081port(){
        URL url1 = null;
        try {
            url1 = new URL("http://"+appIP+":8081/"+appPath);
            System.out.println("Testing URL: "+url1);
            connection = (HttpURLConnection)url1.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
            System.out.println("Reponse code is: "+code);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertTrue((code == 200), "Application is not running on the URL"+url1);
    }


    @Test(dependsOnMethods="testAppRunning")
    public void testApplicationPage(){
        String responseString = null;
        String expectedResponseStr = "DevOps App with Artifact Management";
        HttpClient httpclient = new DefaultHttpClient();

        try {
        HttpResponse response = httpclient.execute(new HttpGet(url.toURI()));
        HttpEntity entity = response.getEntity();
        responseString = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            System.out.println("Network exception: "+e.getMessage());
        }
        Assert.assertTrue(responseString.contains(expectedResponseStr),
                "Response did not contain expected response string Actual: "+ responseString +
                "Expected substring: "+expectedResponseStr);
    }

    @Test(dependsOnMethods="testAppRunning")
    public void testJBossRunning(){
        URL url2 = null;
        try {
            url2 = new URL("http://"+appIP+":"+appPort);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String responseString = null;
        String expectedResponseStr = "Manage this JBoss AS Instance";
        HttpClient httpclient = new DefaultHttpClient();

        try {
        HttpResponse response = httpclient.execute(new HttpGet(url2.toURI()));
        HttpEntity entity = response.getEntity();
        responseString = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            System.out.println("Network exception: "+e.getMessage());
        }
        Assert.assertTrue(responseString.contains(expectedResponseStr),
                "Response did not contain expected response string Actual: "+ responseString +
                "Expected substring: "+expectedResponseStr);
    }
}
