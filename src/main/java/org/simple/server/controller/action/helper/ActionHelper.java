package org.simple.server.controller.action.helper;

import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* ActionHelper is an utility class for Actions following DRY principle
 *
 */
public class ActionHelper {

    // Helper method to copy our static files to the server output stream.
    public static ISendFile actionTool = (exchange, resource, status) -> {
            int count = 0;

            InputStream is = ActionHelper.class.getResourceAsStream(resource);
            if (null != is) {
                Headers h = (Headers) exchange.getResponseHeaders();
                h.set("Content-Type", "text/html");
                exchange.setStatus(status, 0);
                count = stream(is, exchange.getResponseBody());
                exchange.close();
            }

            return count;
    };

    private static int stream(InputStream is, OutputStream os) throws IOException {
        final byte[] buffer = new byte[4096];
        int count;
        int total = 0;
        while ((count = is.read(buffer)) >= 0) {
            os.write(buffer,0,count);
            total += count;
        }
        return total;
    }

}
