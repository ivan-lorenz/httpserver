package org.simple.server;


import org.simple.server.application.RunContext;

import java.io.IOException;

/* Main entry point for our server application
 *
 */
public class Main {

    public static void main(String args[]) {
        int port = 8001;

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
            new RunContext().start(port);
            System.out.println("Server started. Listening on port " + String.valueOf(port) + "...");
        } catch (IOException e) {
            System.out.println("Can't start the server.");
        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[0] + " must be an integer.");
        }
    }
}