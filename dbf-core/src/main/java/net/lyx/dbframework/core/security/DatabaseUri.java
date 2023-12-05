package net.lyx.dbframework.core.security;

import lombok.*;

@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseUri {

    private static final String URI_FORMAT = "%s://%s:%s/%s";

    private String driver;
    private String storage;
    private String host;
    private int port;

    public String toUri() {
        checkPort();
        setEmptiesStrings();

        return String.format(URI_FORMAT, driver, host, port, storage);
    }

    private void setEmptiesStrings() {
        this.driver = (driver == null ? "" : driver);
        this.storage = (storage == null ? "" : storage);
        this.host = (host == null ? "" : host);
    }

    private void checkPort() {
        if (port < 0) {
            throw new IllegalArgumentException("port can not be < 0");
        }
    }
}
