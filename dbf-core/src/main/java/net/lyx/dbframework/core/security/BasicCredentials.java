package net.lyx.dbframework.core.security;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class BasicCredentials implements Credentials {

    private final String uri;

    private final String username;
    private final String password;

    @Override
    public char[] getPassword() {
        return password.toCharArray();
    }
}
