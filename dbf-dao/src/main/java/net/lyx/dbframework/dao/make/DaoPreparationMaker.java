package net.lyx.dbframework.dao.make;

import lombok.experimental.UtilityClass;
import lombok.var;
import net.lyx.dbframework.core.Field;
import net.lyx.dbframework.core.ResponseStream;
import net.lyx.dbframework.core.compose.ParameterType;
import net.lyx.dbframework.dao.Dao;
import net.lyx.dbframework.dao.DataAccessContext;
import net.lyx.dbframework.dao.DataAccessException;
import net.lyx.dbframework.dao.repository.DaoRepository;
import net.lyx.dbframework.dao.repository.DaoRepositoryLifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class DaoPreparationMaker {

    private <T> void makeContainer(DaoRepositoryLifecycle<T> lifecycle) {
        if (lifecycle.getAccessContext().getContainerName() == null) {
            if (lifecycle.getCachedContainerName() == null) {
                lifecycle.setCachedContainerName(lifecycle.getDefaultObject().get().getClass().getSimpleName());
            }

            lifecycle.getAccessContext().setContainerName(lifecycle.getCachedContainerName());
        }

        if (!lifecycle.isContainerSet()) {
            DaoRequestMaker.makeContainerAdjust(lifecycle);

            lifecycle.setContainerSet(true);
        }
    }

    public <T> DataAccessContext<T> makeContext(DaoRepository<T> repository) {
        var lifecycle = repository.getLifecycle();
        var context = lifecycle.getAccessContext();

        if (!context.isEmpty()) {
            return context;
        }

        var dao = lifecycle.getDao();

        dao.writeAccess(context);

        DaoPreparationMaker.makeContainer(lifecycle);

        return context;
    }

    public <T> void makeResponse(DaoRepositoryLifecycle<T> lifecycle, ResponseStream response, CompletableFuture<List<T>> future) {
        var result = new ArrayList<T>();

        response.forEach(row -> {
            var object = lifecycle.getDefaultObject().get();

            for (var field : row.fields()) {
                var value = DaoPreparationMaker.makeResponseValue(lifecycle, object, field);

                lifecycle.getAccessContext().update(object, field.label(), value);
            }

            result.add(object);
        });

        future.complete(result);
    }

    private <T> Object makeResponseValue(DaoRepositoryLifecycle<T> lifecycle, T object, Field field) {
        var current = lifecycle.getAccessContext().read(object, field.label());
        var javaType = current.getClass();

        switch (ParameterType.fromJavaType(javaType)) {

            case SERIALIZATION: {
                byte[] bytes = field.getAsString().getBytes();
                // todo - deserialize
                break;
            }

            case INT: {
                return field.getAsInt();
            }
            case BIGINT: {
                return field.getAsLong();
            }
            case BOOLEAN: {
                return field.getAsBoolean();
            }
            case FLOAT: {
                return field.getAsFloat();
            }
            case DOUBLE: {
                return field.getAsDouble();
            }
            case STRING: {
                return field.getAsString();
            }
            case TIMESTAMP: {
                return field.getAsTimestamp();
            }
        }

        throw new DataAccessException("object value cannot to prepare");
    }
}
