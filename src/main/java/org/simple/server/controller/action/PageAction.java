package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/* PageAction is a IServerAction implementation to serving our static pages.
 *
 */
public class PageAction implements IServerAction {

    public void execute(IServerExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        InputStream is = getClass().getResourceAsStream(uri.getPath());
        if (null != is) {
            exchange.setStatus(200,0);
            OutputStream os = exchange.getResponseBody();
            stream(is,os);
            is.close();
            os.close();
        }
    }

    // Helper method to copy our static files to the server output stream.
    private static void stream(InputStream is, OutputStream os) throws IOException{
        final byte[] buffer = new byte[4096];
        int count = 0;
        while ((count = is.read(buffer)) >= 0) {
            os.write(buffer,0,count);
        }
    }
}
