package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;

interface ISendFile {
    int sendFile(IServerExchange exchange, String resource, int status) throws IOException;
}
