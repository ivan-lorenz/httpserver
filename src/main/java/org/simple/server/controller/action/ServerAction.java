package org.simple.server.controller.action;

import com.sun.net.httpserver.Headers;
import org.simple.server.controller.IServerExchange;
import org.simple.server.model.ServerRole;

import java.io.*;
import java.util.*;

/* ServerAction is an utility class for Actions following DRY principle
 *
 */
class ServerAction {

    // Helper method to copy our static files to the server output stream.
    static int sendFile(IServerExchange exchange, String resource, int status) throws IOException {
        int count = 0;

        InputStream is = ServerAction.class.getResourceAsStream(resource);
        if (null != is) {
            Headers h = (Headers) exchange.getResponseHeaders();
            h.set("Content-Type", "text/html");
            exchange.setStatus(status, 0);
            count = stream(is, exchange.getResponseBody());
            exchange.close();
        }

        return count;
    }

    static boolean isWwwFormUrlencoded(IServerExchange exchange) {
        List<String> contentType = exchange.getRequestHeaders().get("Content-type");

        return null != contentType && contentType.contains("application/x-www-form-urlencoded");

    }

    static Map<String, String> getWwwFormUrlencodedBody(IServerExchange exchange) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        String body = getStringFromInputStream(exchange.getRequestBody());

        for (String parameter : body.split("&")) {
            String[] keyValue = parameter.split("=");

            if (keyValue.length != 2)
                return null;

            map.put(keyValue[0], keyValue[1]);

        }

        return map;

    }

    static List<ServerRole> getRoles(String roles) {
        ArrayList<ServerRole> serverRoles = new ArrayList<>();

        for (String role: roles.split(",")) {
            ServerRole.fromString(role).ifPresent(serverRoles::add);
        }

        return serverRoles;
    }


    static void sendBadRequestResponse(String message, IServerExchange exchange) throws IOException {
        exchange.setStatus(400, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes("UTF-8"));
    }

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

    private static String getStringFromInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null ) {
            sb.append(line);
        }
        return sb.toString();
    }

}
