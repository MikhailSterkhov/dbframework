package net.lyx.dbframework.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.ToString;
import net.lyx.dbframework.core.compose.ParameterType;

@ToString
@Builder(toBuilder = true)
public class WrappedSignatureParameter implements WrappedElement {

    private final String label;
    private final ParameterType type;
    private final Number length;

    private final String encoding;
    private final String encodingCollate;

    private final String addonsLabel;

    private final String defaultValue;
}
