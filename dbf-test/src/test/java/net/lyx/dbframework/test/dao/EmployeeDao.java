package net.lyx.dbframework.test.dao;

import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.compose.Composer;
import net.lyx.dbframework.dao.AbstractPreparedDao;
import net.lyx.dbframework.dao.DataAccessContext;

import java.util.Arrays;
import java.util.List;

public class EmployeeDao extends AbstractPreparedDao<Employee> {

    private static final List<LabelOrder> ORDERED_REFERENCES = Arrays.asList(
            LabelOrder.create(1, "id"),
            LabelOrder.create(2, "first_name"),
            LabelOrder.create(3, "last_name"),
            LabelOrder.create(4, "age"));

    public EmployeeDao(Composer composer, DatabaseConnection connection) {
        super("employees", composer, connection);
    }

    @Override
    public void writeAccess(DataAccessContext<Employee> context) {
        context.writeAutofill("id", Employee::getId, Employee::setId);
        context.write("age", Employee::getAge, Employee::setAge);
        context.write("first_name", Employee::getFirstName, Employee::setFirstName);
        context.write("last_name", Employee::getLastName, Employee::setLastName);
    }

    @Override
    public List<LabelOrder> references() {
        return ORDERED_REFERENCES;
    }

    @Override
    protected Employee allocateDefaultObject() {
        return Employee.builder()
                .firstName("")
                .lastName("")
                .age(0)
                .id(-1)
                .build();
    }
}
