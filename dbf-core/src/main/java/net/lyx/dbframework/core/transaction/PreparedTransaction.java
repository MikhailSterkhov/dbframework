package net.lyx.dbframework.core.transaction;

import net.lyx.dbframework.core.DatabaseConnection;
import org.jetbrains.annotations.NotNull;

public interface PreparedTransaction {

    TransactionResult call(@NotNull DatabaseConnection connection);
}
