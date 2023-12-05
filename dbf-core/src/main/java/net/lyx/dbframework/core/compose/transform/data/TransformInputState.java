package net.lyx.dbframework.core.compose.transform.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@Builder(builderClassName = "Builder")
public class TransformInputState {

    private String name;
    private TransformStatements mainStatement;

    private Map<String, TransformStatements> followedStatements;
}