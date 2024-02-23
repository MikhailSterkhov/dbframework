package net.lyx.dbframework.testengine.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.lyx.dbframework.dao.entity.EntityAccessible;
import net.lyx.dbframework.dao.entity.EntityAutoPrepare;

@EntityAutoPrepare
@EntityAccessible(name = "Groups")
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Status {

    private int id;

    private final String name;
}
