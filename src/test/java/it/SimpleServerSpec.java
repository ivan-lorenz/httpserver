package it;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simple.server.model.IServerSession;
import org.simple.server.model.ServerRole;
import org.simple.server.model.ServerUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleServerSpec extends TestContext {

    private static TestContext context = new TestContext();

    @BeforeClass
    public static void setup() throws IOException { context.start(); }

    @AfterClass
    public static void tearDown() {
        context.stop(0);
    }

    @Test
    public void shouldCheckBootstrap() {
        assertEquals(404,request("").getStatusLine().getStatusCode());
    }

    @Test
    public void shouldRedirectToLogin() throws IOException {
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
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type","application/x-www-form-urlencoded");
        HttpResponse response = request("authorize","POST", "user_name=user1&user_password=user1", headers);

        assertEquals(401,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldCreateSession() {
        repository.createUser("user1","user1", ServerRole.PAGE1);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type","application/x-www-form-urlencoded");
        HttpResponse response = request("authorize","POST", "user_name=user1&user_password=user1", headers);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(response.getFirstHeader("Set-Cookie").getValue().contains("SESSION="));
    }

    @Test
    public void shouldAccessPage1() throws IOException {
        repository.createUser("user1","user1", ServerRole.PAGE1);
        IServerSession session = repository.createSession(new ServerUser("user1", ServerRole.PAGE1));
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=" + session.getSession());
        HttpResponse response = request("page1.html", "GET", null, headers);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("user1"));
    }

}
