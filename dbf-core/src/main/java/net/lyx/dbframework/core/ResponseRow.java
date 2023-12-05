package net.lyx.dbframework.core;

public interface ResponseRow {

    int length();

    Field field(int index);

    Field field(String label);

    Field[] fields();

    boolean isNull(int index);

    boolean isNull(String label);
}
