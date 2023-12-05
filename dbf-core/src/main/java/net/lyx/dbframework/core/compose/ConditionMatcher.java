package net.lyx.dbframework.core.compose;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConditionMatcher {

    EQUALS("="),
    MORE(">"),
    LESS("<"),
    MORE_OR_EQUAL(">="),
    LESS_OR_EQUAL("<="),
    MATCHES("LIKE"),
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
