package stepper.xmlexceptions;

import java.io.Serializable;

public class ContinuationFlowExistException extends RuntimeException implements Serializable
{
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Continuation failed: The flow: %s is not exist in the system";

    public ContinuationFlowExistException(String flowName) {
        this.flowName=flowName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName);
    }
}
