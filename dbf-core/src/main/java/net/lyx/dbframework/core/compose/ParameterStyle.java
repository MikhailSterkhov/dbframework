package net.lyx.dbframework.core.compose;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.lyx.dbframework.core.compose.impl.collection.element.Encoding;

import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
public class ParameterStyle {

    private final ParameterType type;
    private final Integer length;

    private final Encoding encoding;

    private final List<ParameterAddon> addons;

    private final Object defaultValue;
}
