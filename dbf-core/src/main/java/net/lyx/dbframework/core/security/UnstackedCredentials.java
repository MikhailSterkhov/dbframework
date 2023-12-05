package net.lyx.dbframework.core.security;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class UnstackedCredentials implements Credentials {

    private final String uri;
    private final String username;
    private final char[] password;
}
