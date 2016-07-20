package org.simple.server;


import java.io.IOException;

/* Main entry point for our server application
 *
 */
public class Main {

    public static void main(String args[]) {
        try {
            new RunContext().start();
        } catch (IOException e) {
            System.out.println("Can't start the server.");
        }

    }
}
