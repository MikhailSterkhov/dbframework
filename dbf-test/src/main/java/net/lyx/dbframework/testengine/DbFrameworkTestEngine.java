package net.lyx.dbframework.testengine;

import lombok.Getter;
import lombok.SneakyThrows;
import net.lyx.dbframework.core.security.BasicCredentials;
import net.lyx.dbframework.core.security.Credentials;
import net.lyx.dbframework.provider.DatabaseProvider;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbFrameworkTestEngine {

    private static Unsafe unsafe;
    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);

            unsafe = (Unsafe) theUnsafe.get(null);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Getter
    private ActiveTestState state = ActiveTestState.builder().build();

    private Set<WrappedTest> collectedTests;

    public synchronized void updateState(@NotNull Function<ActiveTestState, ActiveTestState> updateFunction) {
        this.state = updateFunction.apply(getState());
    }

    public void loadTestTemplate(@NotNull Class<?> cls) {
        loadTestConnectionPropertiesInput(cls.getClassLoader());
        openDatabaseConnection();

        collectedTests = collectTests(cls);
    }

    @SneakyThrows
    private void loadTestConnectionPropertiesInput(ClassLoader classLoader) {
        InputStream testConnectionPropertiesInput = classLoader.getResourceAsStream("test_connection.properties");

        Properties properties = new Properties();
        properties.load(testConnectionPropertiesInput);

        updateState(state -> state.toBuilder()
                .connectionProperties(properties)
                .build());
    }

    private void openDatabaseConnection() {
        DatabaseProvider databaseProvider = new DatabaseProvider();
        Properties connectionProperties = state.getConnectionProperties();

        Credentials credentials = BasicCredentials.builder()
                .uri(connectionProperties.getProperty("uri"))
                .username(connectionProperties.getProperty("username"))
                .password(connectionProperties.getProperty("password"))
                .build();

        updateState(state -> state.toBuilder()
                .provider(databaseProvider)
                .activeConnection(databaseProvider.openConnection(credentials))
                .build());
    }

    private Set<WrappedTest> collectTests(Class<?> cls) {
        final Method[] methods = cls.getDeclaredMethods();
        return Arrays.stream(methods)
                .map(method -> WrappedTest.builder()
                        .name(method.getName())
                        .method(method)
                        .build())
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    public void start(@NotNull Class<?> cls) {
        AbstractTestRunner runner = (AbstractTestRunner) unsafe.allocateInstance(cls);

        for (WrappedTest test : collectedTests)
            test.call(state, runner);
    }
}
