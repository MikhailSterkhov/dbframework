package net.lyx.dbframework.core;

import java.sql.Timestamp;

public interface Field {

    int index();

    String label();

    Timestamp getAsTimestamp();

    String getAsString();

    Integer getAsInt();

    Double getAsDouble();

    Float getAsFloat();

    Long getAsLong();

    Boolean getAsBoolean();

    Object getAsObject();
}
