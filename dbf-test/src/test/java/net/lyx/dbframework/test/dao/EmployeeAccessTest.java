package net.lyx.dbframework.test.dao;

import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.observer.event.DbRequestCompletedEvent;
import net.lyx.dbframework.core.security.BasicCredentials;
import net.lyx.dbframework.dao.repository.Repository;
import net.lyx.dbframework.provider.DatabaseProvider;

public class EmployeeAccessTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        DatabaseConnection connection = provider.openConnection(
                BasicCredentials.builder()
                        .uri("jdbc:h2:mem:default;DB_CLOSE_ON_EXIT=FALSE")
                        .username("root")
                        .password("123qwe")
                        .build());

        connection.addObserver(event -> {
            if (event instanceof DbRequestCompletedEvent) {
                System.out.println(((DbRequestCompletedEvent) event).getSql());
            }
        });

        Repository<Employee> repository = provider.registerDao(
                new EmployeeDao(provider.getComposer(), connection));

        repository.insertMono(
                Employee.builder()
                        .firstName("Oleg")
                        .lastName("Tinkoff")
                        .age(55)
                        .build());
        repository.insertMono(
                Employee.builder()
                        .firstName("Viktor")
                        .lastName("Busov")
                        .age(35)
                        .build());

        repository.findAll()
                .forEach(System.out::println);
    }
}
