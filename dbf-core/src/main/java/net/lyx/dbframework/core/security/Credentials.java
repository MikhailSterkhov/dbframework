package net.lyx.dbframework.core.security;

public interface Credentials {

    String getUri();

    String getUsername();

    char[] getPassword();
}
