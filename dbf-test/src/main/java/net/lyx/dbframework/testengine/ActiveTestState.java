package net.lyx.dbframework.testengine;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.provider.DatabaseProvider;

import java.util.Properties;

@Getter
@ToString
@Builder(toBuilder = true)
public class ActiveTestState {

    private DatabaseProvider provider;
    private Properties connectionProperties;
    private DatabaseConnection activeConnection;
}
