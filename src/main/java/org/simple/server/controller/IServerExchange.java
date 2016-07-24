package org.simple.server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/* IServerExchange is the interface for the Adapter pattern implemented in ServerExchange.
 *
 */
public interface IServerExchange {
    public String getRequestMethod();
    public URI getRequestURI();
    public Map<String, List<String>> getRequestHeaders();
    public Map<String, List<String>> getResponseHeaders();
    public InputStream getRequestBody() throws IOException;
    public OutputStream getResponseBody() throws IOException;
    public void setStatus(int rCode, long responseLength)throws IOException;
    public void close();
    public String getUser();
}
