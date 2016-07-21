package org.simple.server.model;

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
}
