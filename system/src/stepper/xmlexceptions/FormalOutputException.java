package stepper.xmlexceptions;

import java.io.Serializable;

public class FormalOutputException extends RuntimeException implements Serializable
{
    private String formalOutputName;
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: The formal Output of flow: %s doesnt exist in flow ";

    public FormalOutputException( String flowName,String formalOutputName) {
        this.flowName=flowName;
        this.formalOutputName=formalOutputName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName,formalOutputName);
    }
}
