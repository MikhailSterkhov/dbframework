package net.lyx.dbframework.core.compose;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StorageType {

    VIEW("VIEW"),
    STORAGE("SCHEMA"),
    CONTAINER("TABLE"),
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
