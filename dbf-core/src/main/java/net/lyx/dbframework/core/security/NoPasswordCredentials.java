package net.lyx.dbframework.core.security;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class NoPasswordCredentials implements Credentials {

    private static final char[] PASSWORD_CONST = new char[0];

    private final String uri;
    private final String username;

    @Override
    public char[] getPassword() {
        return PASSWORD_CONST;
    }
}
