package stepper.xmlexceptions;

import java.io.Serializable;

public class ContinuationMappingDataException  extends RuntimeException implements Serializable
{
    private String flowName;
    private String dataName;
    private CustomMappingType type;
    private final String EXCEPTION_MESSAGE = "Continuation failed: The %s-data: %s is not exist in the %s-flow %s";

    public ContinuationMappingDataException(String flowName,String dataName, CustomMappingType type) {
        this.dataName=dataName;
        this.flowName=flowName;
        this.type=type;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,type.toString(),dataName,type.toString(),flowName);
    }
}
