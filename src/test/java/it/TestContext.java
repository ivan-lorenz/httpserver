package it;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.simple.server.application.Context;
import org.simple.server.application.IClock;
import org.simple.server.model.ServerRole;
import org.simple.server.model.repository.IServerRepository;
import org.simple.server.model.repository.ServerRepository;

import java.io.IOException;
import java.util.Map;

class TestContext extends Context {

    static TestClock testClock = new TestClock();
    static IServerRepository repository = new ServerRepository("admintest", "admintest", testClock);
    static private String realm = "test-realm";

    @Override
    public ISupplyContext supplyContext() {
        return new ISupplyContext() {

            @Override
            public IServerRepository getRepository() {
                return repository;
            }

            @Override
            public String getRealm() {
                return realm;
            }

            @Override
            public IClock getClock() {
                return testClock;
            }
        };
    }

    static HttpResponse request(String path) {
        return request(path, "GET", null, null);
    }

    static HttpResponse request(String path, String method, String body, Map<String, String> headers)  {
        try {
            HttpClient client =  HttpClientBuilder.create().disableRedirectHandling().build();
            String uri = "http://localhost:8001/" + path;
            HttpUriRequest request;
            switch (method) {
                case "GET":
                    request = new HttpGet(uri);
                    if (null != headers && !headers.isEmpty())
                        for (Map.Entry<String, String> h : headers.entrySet())
                            request.addHeader(h.getKey(), h.getValue());
                    break;
                case "POST":
                    request = new HttpPost(uri);
                    if (null != body)
                        ((HttpPost) request).setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
                    if (null != headers && !headers.isEmpty())
                        for (Map.Entry<String, String> h : headers.entrySet())
                            request.addHeader(h.getKey(), h.getValue());
                    break;
                case "DELETE":
                    request = new HttpDelete(uri);
                    if (null != headers && !headers.isEmpty())
                        for (Map.Entry<String, String> h : headers.entrySet())
                            request.addHeader(h.getKey(), h.getValue());
                    break;
                case "PUT":
                    request = new HttpPut(uri);
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

    @Before
    public void setUp() { repository.createUser("admintest","admintest",ServerRole.ADMIN); }

    @After
    public void afterTest() {
        repository.deleteAll();
    }

}
