package it;

import org.apache.http.HttpResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simple.server.model.ServerRole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RESTApiSpec extends TestContext {

    private static TestContext context = new TestContext();

    // Encoding Base64 for user admintest and password admintest
    private String adminBasicAuthorizationEncoded = "YWRtaW50ZXN0OmFkbWludGVzdA==";

    @BeforeClass
    public static void setup() throws IOException { context.start(); }

    @AfterClass
    public static void tearDown() { context.stop(0); }

    @Test
    public void shouldFailAccessingAPI() {
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("api/user/user1?role=PAGE_1", "POST", null, headers);
        assertEquals(400,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldCreateUser() {
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("api/user/user1?password=user1&role=PAGE_1", "POST", null , headers);
        assertEquals(200,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldDeleteUser() {
        repository.createUser("user1","user1", ServerRole.PAGE1);
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("api/user/user1", "DELETE", null, headers);
        assertEquals(200,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldUpdateUser() {
        repository.createUser("user1","user1", ServerRole.PAGE1);
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("api/user/user1?role=PAGE_2,PAGE_3", "PUT", null, headers);
        assertEquals(200,response.getStatusLine().getStatusCode());
    }

}
