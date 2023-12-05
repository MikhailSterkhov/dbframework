package net.lyx.dbframework.test.dao;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Employee {

    private long id;

    private int age;

    private String firstName;
    private String lastName;
}
