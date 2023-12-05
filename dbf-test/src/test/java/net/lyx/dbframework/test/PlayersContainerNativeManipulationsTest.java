package net.lyx.dbframework.test;

import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.security.BasicCredentials;
import net.lyx.dbframework.test.observer.AnyEventPrintingObserverTest;

public class PlayersContainerNativeManipulationsTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        DatabaseConnection connection = provider.openConnection(
                BasicCredentials.builder()
                        .uri("jdbc:h2:mem:default;DB_CLOSE_ON_EXIT=FALSE")
                        .username("root")
                        .password("123qwe")
                        .build());

        connection.addObserver(new AnyEventPrintingObserverTest());

        connection.call("create table PLAYERS (id int not null unique auto_increment, name varchar(32) not null unique)");

        connection.call("insert into PLAYERS (NAME) values ('lyx')")
                .map(response -> response.find(1).orElse(null))
                .whenCompleted(System.out::println);

        connection.call("select * from PLAYERS")
                .whenCompleted(response ->
                        response.forEach(System.out::println));
    }
}
