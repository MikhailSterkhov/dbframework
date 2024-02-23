package net.lyx.dbframework.dao.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class ExternalElement {

    private final Entity foreignEntity;
    private final Element element;
}
