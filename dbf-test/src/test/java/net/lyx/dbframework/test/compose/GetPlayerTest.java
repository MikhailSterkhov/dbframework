package net.lyx.dbframework.test.compose;

import net.lyx.dbframework.provider.DatabaseProvider;
import net.lyx.dbframework.core.compose.CombinedStructs;
import net.lyx.dbframework.core.compose.OrderDirection;
import net.lyx.dbframework.core.compose.Composer;

public class GetPlayerTest {

    private static final String PLAYERS_TABLE = "Players";
    private static final String PLAYERS_AGES_TABLE = "PlayersAges";

    private static final String PLAYER_ID_COLUMN = "ID";
    private static final String PLAYER_NAME_COLUMN = "NAME";
    private static final String PLAYER_AGE_COLUMN = "AGE";

    public static void main(String[] args) {
        DatabaseProvider provider = new DatabaseProvider();
        Composer composer = provider.getComposer();

        String query = composer.useSearchPattern()
                .container(PLAYERS_TABLE)
                .sort(CombinedStructs.orderedLabel(OrderDirection.DESCENDING, PLAYER_AGE_COLUMN))
                .subjects(composer.subjects()
                        .select(CombinedStructs.label(PLAYER_ID_COLUMN))
                        .average(CombinedStructs.label(PLAYER_AGE_COLUMN, "AVG_AGES"))
                        .min(CombinedStructs.label(PLAYER_ID_COLUMN))
                        .combine())
                .predicates(composer.predicates()
                        .ifMatches(CombinedStructs.field(PLAYER_NAME_COLUMN, "lyx_"))
                        .or()
                        .ifEqual(CombinedStructs.field(PLAYER_AGE_COLUMN, 5))
                        .bind()
                        .combine())
                .merges(composer.merges()
                        .unscoped(CombinedStructs.merged(PLAYERS_AGES_TABLE, PLAYER_ID_COLUMN))
                        .combine())
                .groups(composer.groups()
                        .with(CombinedStructs.fieldNullable(PLAYER_ID_COLUMN))
                        .combine())
                .limit(2)
                .combine()
                .toNativeQuery()
                .orElse(null);

        // SELECT AVG(AGE) AS AVG_AGES, MIN(ID), ID FROM Players  OUTER JOIN PlayersAges ON ID = PlayersAges.ID WHERE NAME LIKE 'lyx_' OR AGE = 5 GROUP BY ID ORDER BY AGE DESC LIMIT 2 ;
        System.out.println(query);
    }
}
