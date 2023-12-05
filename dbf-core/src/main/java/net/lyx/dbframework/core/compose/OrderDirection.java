package net.lyx.dbframework.core.compose;

public enum OrderDirection {

    ASCENDING,
    DESCENDING,
    ;

    private static final String SUFFIX = "ENDING";

    private String name;

    @Override
    public String toString() {
        if (name == null) {
            name = name();
        }
        return name.replace(SUFFIX, "");
    }
}
