package net.lyx.dbframework.core.compose.impl.pattern;

import lombok.*;
import net.lyx.dbframework.core.compose.impl.collection.PatternCollection;
import net.lyx.dbframework.core.util.ObjectPropertyContainer;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternCollections {

    public static PatternCollections fromPattern(String pattern) {
        return new PatternCollections(pattern);
    }

    private final String pattern;

    private final ObjectPropertyContainer collectionsContainer
            = ObjectPropertyContainer.newWeakContainer()
                .setDefaultKeyModifier(ObjectPropertyContainer.DefaultKeyModifiers.AS_LOWER_CASE);

    public void add(String collectionName, PatternCollection<?> collection) {
        collectionsContainer.setProperty(collectionName, collection);
    }

    public <T> PatternCollection<T> get(String collectionName) {
        return collectionsContainer.getProperty(collectionName);
    }
}
