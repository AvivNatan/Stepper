package stepper.xmlexceptions;

import java.io.Serializable;

public class InitialInputValueDataException extends RuntimeException implements Serializable
{
    private String flowName;
    private String dataName;
    private final String EXCEPTION_MESSAGE = "Initial failed: The input: %s is not exist in the flow: %s";

    public InitialInputValueDataException(String flowName,String dataName) {
        this.dataName=dataName;
        this.flowName=flowName;

    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,dataName,flowName);
    }
}
