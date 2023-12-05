package net.lyx.dbframework.dao;

import lombok.*;
import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.compose.Composer;
import net.lyx.dbframework.dao.repository.DaoRepository;
import net.lyx.dbframework.dao.repository.DaoRepositoryLifecycle;
import net.lyx.dbframework.dao.repository.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public abstract class AbstractPreparedDao<T> implements Dao<T> {

    @Getter
    @Builder
    protected static class LabelOrder {

        public static LabelOrder create(int order, String label) {
            return LabelOrder.builder().name(label).order(order).build();
        }

        private final int order;
        private final String name;
    }

    @Setter(AccessLevel.PROTECTED)
    private Repository<T> repository;

    @ToString.Include
    private final String name;

    private final Composer composer;
    private final DatabaseConnection activeConnection;

    public abstract List<LabelOrder> references();

    protected abstract T allocateDefaultObject();

    private int getOrder(List<LabelOrder> references, String name) {
        return references.stream()
                .filter(reference -> reference.getName().equals(name))
                .mapToInt(LabelOrder::getOrder)
                .findFirst()
                .orElse(0);
    }

    private void prepareContainerName(DataAccessContext<T> context) {
        context.setContainerName(name);

        var references = this.references();
        var comparator = Comparator.<String>comparingInt(name -> getOrder(references, name));

        context.setLabelAccessorsCache(new TreeMap<>(comparator));
    }

    @Override
    public final void prepare(DataAccessContext<T> context) {
        var preparedRepository = createRepository(context);

        prepareContainerName(context);
        setRepository(preparedRepository);

        onPreparedPost(context);
    }

    @Override
    public final Repository<T> repository() {
        return repository;
    }

    protected void onPreparedPost(DataAccessContext<T> context) {
        // override me.
    }

    protected Repository<T> createRepository(DataAccessContext<T> context) {
        var lifecycle = createRepositoryLifecycle(context);
        return new DaoRepository<>(lifecycle);
    }

    private DaoRepositoryLifecycle<T> createRepositoryLifecycle(DataAccessContext<T> context) {
        return new DaoRepositoryLifecycle<>(this, composer, activeConnection, this::allocateDefaultObject, context);
    }
}
