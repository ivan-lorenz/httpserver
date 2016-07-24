package org.simple.server.controller.action;

import com.sun.net.httpserver.Headers;
import org.simple.server.controller.IServerExchange;
import org.simple.server.model.ServerRole;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* ServerActionHelper is an utility class for Actions following DRY principle
 *
 */
class ServerActionHelper {

    // Helper method to replace computed server side tokens in the views
    // TODO: Converting an InputStream into a String can cause performance issues. Implement TokenReplacingReader.
    // For large web pages this should be a problem. For a real web server implement a TokenReplacingReader
    // (see: http://tutorials.jenkov.com/java-howto/replace-strings-in-streams-arrays-files.html)
    static void replaceTokenAndSend(IServerExchange exchange, String resource, String token ,String replace, int status) throws IOException {
        String s = getStringFromInputStream(ServerActionHelper.class.getResourceAsStream(resource));

        String response = s.replace(token, replace);

        if (null != response) {
            Headers h = (Headers) exchange.getResponseHeaders();
            h.set("Content-Type", "text/html");
            exchange.setStatus(status, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            exchange.close();
        }
    }

    // Helper method to copy our static files to the server output stream.
    static void sendFile(IServerExchange exchange, String resource, int status) throws IOException {

        InputStream is = ServerActionHelper.class.getResourceAsStream(resource);
        if (null != is) {
            Headers h = (Headers) exchange.getResponseHeaders();
            h.set("Content-Type", "text/html");
            exchange.setStatus(status, 0);
            stream(is, exchange.getResponseBody());
            exchange.close();
        }
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


    static void sendResponse(int status, String message, IServerExchange exchange) throws IOException {
        exchange.setStatus(status, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes("UTF-8"));
    }

    private static final Pattern userPattern = Pattern.compile("/api/user/(.+)$");

    static String getUserFromRequest(IServerExchange exchange) {
        Matcher matcher = userPattern.matcher(exchange.getRequestURI().getPath());

        if (matcher.find())
            return matcher.group(1);

        return null;
    }

    static Map<String, String> getQueryParams(IServerExchange exchange) {
        HashMap<String, String> map = new HashMap<>();

        String query = exchange.getRequestURI().getQuery();

        if (null == query)
            return null;

        for (String parameter : query.split("&")) {
            String[] keyValue = parameter.split("=");

            if (keyValue.length != 2)
                return null;

            map.put(keyValue[0], keyValue[1]);
        }

        return map;
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
