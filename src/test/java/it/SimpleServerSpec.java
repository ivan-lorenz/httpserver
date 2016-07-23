package it;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simple.server.model.ServerRole;
import org.simple.server.model.repository.IServerRepository;
import org.simple.server.model.repository.ServerRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SimpleServerSpec {


    private static TestContext context;
    private static IServerRepository repository;

    private static HttpResponse request(String path) {
        return request(path, "GET", null, null);
    }

    private static HttpResponse request(String path, String method, String body, Map<String, String> headers)  {
        try {
            HttpClient client =  HttpClientBuilder.create().disableRedirectHandling().build();
            String uri = "http://localhost:8001/" + path;
            HttpUriRequest request = null;
            switch (method) {
                case "GET":
                    request = new HttpGet(uri);
                    break;
                case "POST":
                    request = new HttpPost(uri);
                    if (null != body)
                        ((HttpPost) request).setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
                    if (null != headers && !headers.isEmpty())
                        for (Map.Entry<String, String> h : headers.entrySet())
                            request.addHeader(h.getKey(), h.getValue());
                    break;
                default:
                    throw new RuntimeException("Http verb not known.");
            }
            return client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @BeforeClass
    public static void setup() throws IOException {
        repository = new ServerRepository("admin","admin");
        context = new TestContext(repository);
        context.start();
    }

    @AfterClass
    public static void tearDown() {
        context.stop(0);
    }

    @Test
    public void shouldCheckBootstrap() {
        assertEquals(404,request("").getStatusLine().getStatusCode());
    }

    @Test
    public void shouldAccessPage1() throws IOException {
        HttpResponse response = request("page1.html");
        assertEquals(301,response.getStatusLine().getStatusCode());
        assertEquals("login.html",response.getFirstHeader("Location").getValue());
    }

    @Test
    public void shouldNotFindPage4() {
        HttpResponse response = request("page4.html");
        assertEquals(404,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldNotFindFakePage1() {
        HttpResponse response = request("fakepage1.html");
        assertEquals(404,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldAccessLogin() {
        HttpResponse response = request("login.html");
        assertEquals(200,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldNotAuthorizeAccess() {
        HttpResponse response = request("authorize","POST", "user_name=user1&user_password=user1", null);
        assertEquals(401,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldFailAccessingAPI() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization","Basic YWRtaW46YWRtaW4=");
        HttpResponse response = request("api/user", "POST", "user_name=user1&user_password=user1&role=PAGE_1", headers);
        assertEquals(400,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldCreateUser() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization","Basic YWRtaW46YWRtaW4=");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        HttpResponse response = request("api/user", "POST", "user_name=user1&user_password=user1&role=PAGE_1", headers);
        assertEquals(200,response.getStatusLine().getStatusCode());
    }

}
