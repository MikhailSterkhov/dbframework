package net.lyx.dbframework.core.compose;

public interface Tabled<I extends TemplatedQuery> {

    I container(String table);
}
