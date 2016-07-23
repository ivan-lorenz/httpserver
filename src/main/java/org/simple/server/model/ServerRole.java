package org.simple.server.model;

import java.util.Optional;

/*
 *
 */
public enum ServerRole {

    PAGE1("PAGE_1"),
    PAGE2("PAGE_2"),
    PAGE3("PAGE_3"),
    ADMIN("ADMIN");

    private final String value;

    ServerRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Optional<ServerRole> fromString(String text) {
        if (text != null) {
            for (ServerRole role : ServerRole.values()) {
                if (text.equalsIgnoreCase(role.getValue())) {
                    return Optional.of(role);
                }
            }
        }
        return Optional.empty();
    }
}
