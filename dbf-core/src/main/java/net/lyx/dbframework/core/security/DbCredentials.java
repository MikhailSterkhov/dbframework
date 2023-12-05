package net.lyx.dbframework.core.security;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class DbCredentials {

    public String uri(@NotNull DatabaseUri.DatabaseUriBuilder builder) {
        return builder.build().toUri();
    }

    public String uri(@NotNull String driver, @NotNull String storage, @NotNull String host, int port) {
        return uri(
                DatabaseUri.builder()
                        .driver(driver)
                        .storage(storage)
                        .host(host)
                        .port(port));
    }

    public String uri(@NotNull String driver, @NotNull String storage, @NotNull String host) {
        return uri(driver, storage, host, 0);
    }

    public Credentials unstackedCredentials(@NotNull String uri, @NotNull String username, @NotNull String password) {
        return UnstackedCredentials.builder()
                .uri(uri)
                .username(username)
                .password(password.toCharArray())
                .build();
    }

    public Credentials unstackedCredentials(@NotNull String uri, @NotNull String username, char[] password) {
        return UnstackedCredentials.builder()
                .uri(uri)
                .username(username)
                .password(password)
                .build();
    }

    public Credentials basicCredentials(@NotNull String uri, @NotNull String username, @NotNull String password) {
        return BasicCredentials.builder()
                .uri(uri)
                .username(username)
                .password(password)
                .build();
    }

    public Credentials noPasswordCredentials(@NotNull String uri, @NotNull String username) {
        return NoPasswordCredentials.builder()
                .uri(uri)
                .username(username)
                .build();
    }
}
