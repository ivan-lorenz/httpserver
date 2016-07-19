package it;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simple.server.Context;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SimpleServerSpec {

    @BeforeClass
    public static void setup() {

    }

    @Test
    public void shouldCheckBootstrap() throws IOException
    {
        Context context = new TestContext();
        context.start();

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8000/");
        HttpResponse response = client.execute(request);

        assertEquals(404,response.getStatusLine().getStatusCode());

        context.stop();
    }
}
