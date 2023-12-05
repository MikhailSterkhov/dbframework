package net.lyx.dbframework.dao.make;

import lombok.experimental.UtilityClass;
import lombok.var;
import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.ParameterStyle;
import net.lyx.dbframework.core.compose.ParameterType;
import net.lyx.dbframework.core.compose.StorageType;
import net.lyx.dbframework.core.compose.template.collection.PredicatesTemplate;
import net.lyx.dbframework.dao.repository.DaoRepository;
import net.lyx.dbframework.dao.repository.DaoRepositoryLifecycle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class DaoRequestMaker {

    public <T> void makeDelete(DaoRepository<T> repository, T object) {
        var lifecycle = repository.getLifecycle();

        var context = DaoPreparationMaker.makeContext(repository);
        var composer = lifecycle.getComposer();

        var predicatesTemplate = composer.predicates();

        var queryTemplate = composer.useDeletionPattern()
                .container(context.getContainerName());

        PredicatesTemplate.PredicationAgent lastAgent = null;

        for (var label : context.labels()) {
            var read = context.read(object, label);

            if (lastAgent != null) {
                predicatesTemplate = lastAgent.and();
            }

            lastAgent = predicatesTemplate.ifMatches(
                    CombinedStructs.field(label, read));
        }

        if (lastAgent != null) {
            lastAgent.bind();
        }

        queryTemplate.predicates(predicatesTemplate.combine())
                .combine()
                .call(lifecycle.getActiveConnection());
    }

    public <T> void makeInsert(DaoRepository<T> repository, T object) {
        var lifecycle = repository.getLifecycle();

        var context = DaoPreparationMaker.makeContext(repository);
        var composer = lifecycle.getComposer();

        var template = composer.useInsertionPattern()
                .container(context.getContainerName());

        var autofilled = Arrays.asList(context.autofilled());
        //var keys = Arrays.asList(context.keys());

        //if (!keys.isEmpty())
        //    template = template.useDuplicationReduce();

        for (var label : context.labels()) {
            var read = context.read(object, label);
            var field = CombinedStructs.field(label, read);

            if (!autofilled.contains(label)) {
                template = template.withValue(field);
            }

            //if (!keys.isEmpty() && !keys.contains(label)) {
            //    template = template.updateOnConflict(field);
            //}
        }

        template.combine().call(lifecycle.getActiveConnection());
    }

    public <T> List<T> makeSearch(int limit, DaoRepository<T> repository) {
        var lifecycle = repository.getLifecycle();

        var context = DaoPreparationMaker.makeContext(repository);
        var composer = lifecycle.getComposer();

        var future = new CompletableFuture<List<T>>();

        var template = composer.useSearchPattern()
                .container(context.getContainerName())
                .subjects(composer.subjects().selectAll().combine());

        if (limit > 0) {
            template = template.limit(limit);
        }

        template.combine()
                .call(lifecycle.getActiveConnection())
                .whenCompleted(response ->
                        DaoPreparationMaker.makeResponse(lifecycle, response, future));

        return future.join();
    }

    public <T> void makeContainerAdjust(DaoRepositoryLifecycle<T> lifecycle) {
        var defaultObject = lifecycle.getDefaultObject().get();
        var composer = lifecycle.getComposer();

        var accessContext = lifecycle.getAccessContext();

        var template = composer.useCreationPattern()
                .entity(StorageType.CONTAINER)
                .name(accessContext.getContainerName());

        var signature = composer.signature();

        for (var label : accessContext.labels()) {
            var read = accessContext.read(defaultObject, label);

            signature.with(CombinedStructs.styledParameter(label,
                    ParameterStyle.builder()
                            .type(ParameterType.fromJavaType(read.getClass()))
                            .build()));
        }

        template.signature(signature.combine())
                .combine()
                .call(lifecycle.getActiveConnection());
    }
}
