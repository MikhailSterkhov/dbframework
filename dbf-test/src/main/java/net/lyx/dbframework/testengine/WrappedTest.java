package net.lyx.dbframework.testengine;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Getter
@Builder
public class WrappedTest {

    private static final String INTERNAL_PROCESSING_ERROR_FORMAT = "Internal test-running error on called `%s()`";

    private static final Object[] EMPTY_RESOLVED_PARAMETERS = new Object[0];

    private final String name;
    private final Method method;

    public synchronized void call(@NotNull ActiveTestState state, @NotNull AbstractTestRunner runner) {
        try {
            method.setAccessible(true);
            method.invoke(runner, resolveParameters(state));
        }
        catch (Exception ex) {
            throw new RuntimeException(String.format(INTERNAL_PROCESSING_ERROR_FORMAT, name), ex);
        }
    }

    private Object[] resolveParameters(ActiveTestState state) {
        if (method.getParameterCount() == 0) {
            return EMPTY_RESOLVED_PARAMETERS;
        }

        Parameter[] parameters = method.getParameters();
        Object[] resolved = new Object[parameters.length];

        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            resolved[index] = resolveObjectType(state, parameter);
        }

        return resolved;
    }

    private Object resolveObjectType(ActiveTestState state, Parameter parameter) {
        Class<?> type = parameter.getType();
        if (type == ActiveTestState.class) {
            return state;
        }

        return null;
    }
}
