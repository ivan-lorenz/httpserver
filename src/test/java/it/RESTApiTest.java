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
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RESTApiTest extends TestContext {

    private static TestContext context = new TestContext();

    // Encoding Base64 for user admintest and password admintest
    private String adminBasicAuthorizationEncoded = "YWRtaW50ZXN0OmFkbWludGVzdA==";

    @BeforeClass
    public static void setup() throws IOException { context.start(8001); }

    @AfterClass
    public static void tearDown() { context.stop(0); }

    @Test
    public void shouldFailAccess() {
        HttpResponse response = request("api/user/user4", "POST", null, null);
        assertEquals(401,response.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldFailWithWrongParameters() {
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("api/user/user1", "POST", null, headers);
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

    @Test
    public void shouldAccessApiGetForUsers() throws IOException {
        IServerUser user = repository.createUser("user1","user1", ServerRole.PAGE1);
        IServerSession session = repository.createSession(user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=" + session.getSession());
        HttpResponse response = request("api/user/user1", "GET", null, headers);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("User: user1; Roles: PAGE_1,"));
    }

    @Test
    public void shouldFailApiDeleteForUsers() throws IOException {
        IServerUser user = repository.createUser("user1","user1", ServerRole.PAGE1);
        IServerSession session = repository.createSession(user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=" + session.getSession());
        HttpResponse response = request("api/user/user1", "DELETE", null, headers);

        assertEquals(403,response.getStatusLine().getStatusCode());
    }

}
