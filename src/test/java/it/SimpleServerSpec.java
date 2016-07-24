package it;

import org.apache.http.HttpResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

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

}
