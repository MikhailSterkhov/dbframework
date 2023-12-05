package net.lyx.dbframework.core.compose.transform.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(builderClassName = "Builder")
public class TransformStatements {

    private String condition;

    private String full;

    private String corrected;
    private String uncorrected;
}