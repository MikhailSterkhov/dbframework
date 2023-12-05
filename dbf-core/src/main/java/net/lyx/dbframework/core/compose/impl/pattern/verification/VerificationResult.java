package net.lyx.dbframework.core.compose.impl.pattern.verification;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder(toBuilder = true)
public class VerificationResult {

    private final VerificationResultCode code;
    private final String errorMessage;

    public boolean isSuccess() {
        return code == VerificationResultCode.SUCCESS;
    }
}
