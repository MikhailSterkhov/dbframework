package net.lyx.dbframework.test.dao;

import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.security.BasicCredentials;
import net.lyx.dbframework.dao.EntityAccessCondition;
import net.lyx.dbframework.dao.EntityDao;
import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.testengine.dao.entity.Status;
import net.lyx.dbframework.testengine.dao.entity.User;

import java.util.List;
import java.util.Optional;

public class UserDaoTest {

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        DatabaseConnection connection = provider.openConnection(
                BasicCredentials.builder()
                        .uri("jdbc:h2:mem:default;DB_CLOSE_ON_EXIT=FALSE")
                        .username("root")
                        .password("123qwe")
                        .build());

        EntityDao<Status> statusDao = provider.createDao(Status.class, connection);
        EntityDao<User> userDao = provider.createDao(User.class, connection);

        List<Long> insertedUsersIdList = userDao.insertMany(
                User.builder()
                        .firstName("Oleg1")
                        .lastName("Tinkoff1")
                        .status(new Status("CEO1"))
                        .build(),
                User.builder()
                        .firstName("Oleg2")
                        .lastName("Tinkoff2")
                        .status(new Status("CEO2"))
                        .build(),
                User.builder()
                        .firstName("Oleg3")
                        .lastName("Tinkoff3")
                        .status(new Status("CEO3"))
                        .build());

        System.out.println("Last Inserted User ID: " + insertedUsersIdList);

        for (Status status : statusDao.findAll()) {
            System.out.println(status);
        }
        for (User user : userDao.findAll()) {
            System.out.println(user);
        }

        Optional<User> userByIdOptional = userDao.findMonoById(insertedUsersIdList.get(1));
        Optional<User> userByFirstNameOptional = userDao.findMono(
                EntityAccessCondition.createMono("first_name", "Oleg2"));

        User userById = userByIdOptional.get();
        User userByFirstName = userByFirstNameOptional.get();

        System.out.println(userById.equals(userByFirstName));
        System.out.println(userById);
        System.out.println(userByFirstName);
    }
}
