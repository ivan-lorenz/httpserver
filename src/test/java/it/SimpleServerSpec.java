package it;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simple.server.Context;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleServerSpec {

    public static Context context;

    public static HttpResponse request(String path) throws IOException {
        HttpClient client =  HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8001/" + path);
        return client.execute(request);
    }

    @BeforeClass
    public static void setup() throws IOException {
        context = new TestContext();
        context.start();
    }

    @AfterClass
    public static void tearDown() {
        context.stop();
    }

    @Test
    public void shouldCheckBootstrap() throws IOException {
        assertEquals(404,request("").getStatusLine().getStatusCode());
    }

    @Test
    public void shouldAccessPage1() throws IOException {
        HttpResponse response = request("page1.html");
        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("Hello"));
    }
}
