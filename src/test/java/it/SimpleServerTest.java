package it;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simple.server.model.IServerSession;
import org.simple.server.model.IServerUser;
import org.simple.server.model.ServerRole;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleServerTest extends TestContext {

    private static TestContext context = new TestContext();

    @BeforeClass
    public static void setup() throws IOException { context.start(8001); }

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

        assertEquals(307,response.getStatusLine().getStatusCode());
        assertEquals("login.html?from=/page1.html",response.getFirstHeader("Location").getValue());
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
    public void shouldRedirectAfterAuthentication() {
        repository.createUser("user1","user1", ServerRole.PAGE1);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type","application/x-www-form-urlencoded");
        HttpResponse response = request("authorize?from=/page1.html","POST", "user_name=user1&user_password=user1", headers);

        assertEquals(303,response.getStatusLine().getStatusCode());
        assertEquals("/page1.html",response.getFirstHeader("Location").getValue());
    }

    @Test
    public void shouldAccessPage1() throws IOException {
        IServerUser user = repository.createUser("user1","user1", ServerRole.PAGE1);
        IServerSession session = repository.createSession(user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=" + session.getSession());
        HttpResponse response = request("page1.html", "GET", null, headers);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("user1"));
    }

    @Test
    public void shouldFailOnSessionExpiration() throws IOException {
        IServerUser user = repository.createUser("user1","user1", ServerRole.PAGE1);
        IServerSession session = repository.createSession(user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=" + session.getSession());

        // 5 minutes is the expiration time for a session
        // So after 5 minutes...
        testClock.setTime(testClock.getTimestamp() + Duration.ofMinutes(5).toMillis());
        HttpResponse response = request("page1.html", "GET", null, headers);

        assertEquals(403,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldKeepSessionOnActivity() throws IOException {
        IServerUser user = repository.createUser("user1","user1", ServerRole.PAGE1);
        IServerSession session = repository.createSession(user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=" + session.getSession());

        // After 3 minutes we have activity for the same user...
        testClock.setTime(testClock.getTimestamp() + Duration.ofMinutes(3).toMillis());
        HttpResponse response1 = request("page1.html", "GET", null, headers);

        assertEquals(200,response1.getStatusLine().getStatusCode());

        // And the after 4 minutes more, the session should not be expired...
        testClock.setTime(testClock.getTimestamp() + Duration.ofMinutes(4).toMillis());
        HttpResponse response2 = request("page1.html", "GET", null, headers);

        assertEquals(200,response2.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldHonorRoles() throws IOException {
        IServerUser user = repository.createUser("user1","user1", ServerRole.PAGE1);
        IServerSession session = repository.createSession(user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=" + session.getSession());
        HttpResponse response = request("page2.html", "GET", null, headers);

        assertEquals(403,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldCloseSession() throws IOException {
        IServerUser user = repository.createUser("user1","user1", ServerRole.PAGE1);
        IServerSession session = repository.createSession(user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=" + session.getSession());
        HttpResponse response = request("logout.html", "GET", null, headers);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("log out"));
    }

}
