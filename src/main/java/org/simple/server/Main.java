package org.simple.server;


import org.simple.server.application.RunContext;

import java.io.IOException;

/* Main entry point for our server application
 *
 */
public class Main {

    public static void main(String args[]) {
        try {
            new RunContext().start();
            System.out.println("Server started. Listening on port 8001...");
        } catch (IOException e) {
            System.out.println("Can't start the server.");
        }

    }
}
