package net.lyx.dbframework.testengine;

public abstract class AbstractTestRunner {

    private static final DbFrameworkTestEngine ENGINE = new DbFrameworkTestEngine();

    private static Class<?> getCurrentTestClass() {
        return new Object(){}.getClass().getEnclosingClass();
    }

    public static void main(String[] args) {
        Class<?> currentTestClass = getCurrentTestClass();

        ENGINE.loadTestTemplate(currentTestClass);
        ENGINE.start(currentTestClass);
    }
}
