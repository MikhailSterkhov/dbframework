package net.lyx.dbframework.dao;

import lombok.RequiredArgsConstructor;
import lombok.var;
import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.compose.Composer;
import net.lyx.dbframework.core.compose.template.completed.CompletedQuery;
import net.lyx.dbframework.dao.entity.Element;
import net.lyx.dbframework.dao.entity.Entity;
import net.lyx.dbframework.dao.entity.EntityTransmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TypedEntityDao<E> implements EntityDao<E> {

    private static final int UNLIMITED_LIMIT = -1;

    private static final DaoRequestFactory REQUEST_FACTORY = new DaoRequestFactory();
    private static final DaoRequestCaller REQUEST_CALLER = new DaoRequestCaller();

    private final Composer composer;
    private final DatabaseConnection connection;

    private final Class<E> type;

    private Entity entityTemplate;
    private boolean marksContainerExists;

    @Override
    public void deleteMonoById(Long id) {
        var generatedKeyLabel = getGeneratedKeyLabel();
        delete(EntityAccessCondition.createMono(generatedKeyLabel, id));
    }

    @Override
    public void deleteMono(E entityObj) {
        var entityAccessCondition = EntityAccessCondition.create();
        writeAccessCondition(entityAccessCondition,
                EntityTransmitter.transmitEntity(entityObj));

        delete(entityAccessCondition);
    }

    @Override
    public void deleteManyById(Long... ids) {
        var generatedKeyLabel = getGeneratedKeyLabel();
        delete(EntityAccessCondition.createMany(generatedKeyLabel, ids));
    }

    @Override
    public void deleteMany(E... entities) {
        connection.openTransaction();

        for (E entity : entities) {
            deleteMono(entity);
        }

        connection.closeTransaction();
    }

    @Override
    public void delete(EntityAccessCondition condition) {
        var entityTemplate = getEntityTemplate();
        markContainerExisted(entityTemplate);

        var preparedQuery = REQUEST_FACTORY.prepareDelete(composer, entityTemplate.getName(), condition);

        preparedQuery.call(connection);
    }

    @Override
    public Optional<Long> insertMono(E entityObj) {
        var transactional = connection.ofTransactionalGet(() -> {

            var entity = EntityTransmitter.transmitEntity(entityObj);
            markContainerExisted(entity);

            var preparedQueries = REQUEST_FACTORY.prepareInserts(this, composer, entity.getName(), entity);
            return insertMono(preparedQueries);
        });

        return Optional.ofNullable(transactional);
    }

    public Long insertMono(List<CompletedQuery> preparedQueries) {
        for (int i = 0; i < preparedQueries.size(); i++) {
            Long entityId = REQUEST_CALLER.insertEntityWithId(connection, preparedQueries.get(i));

            if (i >= preparedQueries.size() - 1) {
                return entityId;
            }
        }

        return null;
    }

    @Override
    public List<Long> insertMany(E... entities) {
        return connection.ofTransactionalGet(() -> {
            List<Long> entityIds = new ArrayList<>();

            for (E entityObj : entities) {
                entityIds.add(insertMono(entityObj).orElse(-1L));
            }

            return entityIds;
        });
    }

    private List<E> doFind(EntityAccessCondition condition, int limit) {
        var transactional = connection.ofTransactionalGet(() -> {
            var entityTemplate = getEntityTemplate();
            markContainerExisted(entityTemplate);

            var preparedQuery = REQUEST_FACTORY.prepareSearch(composer, entityTemplate.getName(), condition, limit);

            return preparedQuery.call(connection);
        });

        return transactional.map(responseStream -> EntityTransmitter.transmitObjectsList(responseStream, type, composer, connection))
                .get();
    }

    @Override
    public Optional<E> findMonoById(Long id) {
        return findMono(EntityAccessCondition.createMono("id", id));
    }

    @Override
    public Optional<E> findMono(EntityAccessCondition condition) {
        List<E> list = findManyWithLimit(1, condition);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(0));
    }

    @Override
    public List<E> findManyById(Long... ids) {
        return findMany(EntityAccessCondition.createMany("id", ids));
    }

    @Override
    public List<E> findMany(EntityAccessCondition condition) {
        return doFind(condition, UNLIMITED_LIMIT);
    }

    @Override
    public List<E> findManyWithLimit(int limit, EntityAccessCondition condition) {
        return doFind(condition, limit);
    }

    @Override
    public List<E> findWithLimit(int limit) {
        return doFind(null, UNLIMITED_LIMIT);
    }

    @Override
    public List<E> findAll() {
        return findWithLimit(UNLIMITED_LIMIT);
    }

    private void markContainerExisted(Entity entityTemplate) {
        if (marksContainerExists)
            return;

        var preparedQuery = REQUEST_FACTORY.prepareContainerCreate(composer, entityTemplate);
        preparedQuery.call(connection);

        marksContainerExists = true;
    }

    private void writeAccessCondition(EntityAccessCondition condition, Entity entity) {
        var elements = entity.getElements();
        var write = 0;

        for (Element element : elements) {
            if (element.isAutoGenerated() || element.isPrimary()) {
                write++;
                condition = condition.withMono(element.getShortName(), element.getValue());
            }
        }

        if (write == 0) {
            for (Element element : elements) {
                condition = condition.withMono(element.getShortName(), element.getValue());
            }
        }
    }

    private Entity getEntityTemplate() {
        if (entityTemplate == null) {
            entityTemplate = EntityTransmitter.transmitEntityWithoutValues(type);
        }
        return entityTemplate;
    }

    private String getGeneratedKeyLabel() {
        var key = getEntityTemplate().getKey();

        var hasGeneratedKey = key != null && key.isAutoGenerated();
        return hasGeneratedKey ? key.getShortName() : EntityTransmitter.AUTO_GENERATED_LABEL_NAME;
    }
}
