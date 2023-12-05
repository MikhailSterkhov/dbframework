package net.lyx.dbframework.test.dao;

import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.observer.event.DbRequestPreprocessEvent;
import net.lyx.dbframework.core.security.BasicCredentials;
import net.lyx.dbframework.dao.Dao;
import net.lyx.dbframework.dao.repository.Repository;

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
            if (event instanceof DbRequestPreprocessEvent) {
                System.out.println(((DbRequestPreprocessEvent) event).getSql());
            }
        });

        Dao<Employee> employeeDao = provider.registerDao(new EmployeeDao(provider.getComposer(), connection));
        Repository<Employee> repository = employeeDao.repository();

        repository.insert(
                Employee.builder()
                        .firstName("Oleg")
                        .lastName("Tinkoff")
                        .age(55)
                        .build());

        repository.findAll().forEach(System.out::println);
    }
}
