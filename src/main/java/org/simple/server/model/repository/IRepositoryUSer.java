package org.simple.server.model.repository;

import org.simple.server.model.ServerRole;

import java.util.List;

/* IRepositoryUser interface defines how a server user is stored into
 * the repository. So it is the equivalent to a user table scheme for a data base.
 * It's only possible to use this interface within the boundaries of the "model"
 * and must not be leaked to other layers such as controllers or views in order to
 * maintain the password hidden.
 */
interface IRepositoryUSer {
    String getUser();
    List<ServerRole> getRoles();
    String getPassword();
}
