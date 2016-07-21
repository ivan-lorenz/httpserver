package org.simple.server.application;

import org.simple.server.controller.action.IServerAction;

/* IServerRouter defines the behaviour for routing URls to server actions.
 *
 */
public interface IServerRouter {
    public IServerAction get(String s);
}
