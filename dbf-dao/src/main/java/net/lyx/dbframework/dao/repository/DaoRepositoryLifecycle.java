package net.lyx.dbframework.dao.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.lyx.dbframework.core.DatabaseConnection;
import net.lyx.dbframework.core.compose.Composer;
import net.lyx.dbframework.dao.DataAccessContext;
import net.lyx.dbframework.dao.Dao;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Getter
@Setter
@RequiredArgsConstructor
public final class DaoRepositoryLifecycle<T> {

    private final Dao<T> dao;

    private final Composer composer;
    private final DatabaseConnection activeConnection;

    private final Supplier<T> defaultObject;

    // Modifiable parameters.
    private String cachedContainerName;
    private boolean isContainerSet;

    // Auto-filled parameters.
    private final List<T> elementsStore = new LinkedList<>();
    private final DataAccessContext<T> accessContext;

    public synchronized void insertElement(T element) {
        elementsStore.add(element);
    }

    public synchronized void deleteElement(T element) {
        elementsStore.remove(element);
    }

    public synchronized boolean hasElement(T element) {
        return elementsStore.contains(element);
    }

    public synchronized List<T> getElements() {
        return Collections.unmodifiableList(elementsStore);
    }
}
