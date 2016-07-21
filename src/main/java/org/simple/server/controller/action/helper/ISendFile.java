package org.simple.server.controller.action.helper;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;

public interface ISendFile {
    int sendFile(IServerExchange exchange, String resource, int status) throws IOException;
}
