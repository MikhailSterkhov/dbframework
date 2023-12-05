package net.lyx.dbframework.core.compose;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParameterAddon {

    NOTNULL("NOT NULL"),
    INCREMENTING("AUTO_INCREMENT"),
    UNIQUE("UNIQUE"),
    PRIMARY("PRIMARY KEY"),
    // todo - надо потом вынести отдельно в сигнатуру, потому что надо по другому их форматировать если > 1
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
