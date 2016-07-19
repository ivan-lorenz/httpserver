package org.simple.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class Context {

    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.setExecutor(null);
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
