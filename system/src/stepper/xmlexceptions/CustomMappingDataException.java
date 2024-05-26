package stepper.xmlexceptions;

import java.io.Serializable;

public class CustomMappingDataException extends RuntimeException implements Serializable
{
    private String stepName;
    private String dataName;
    private String flowName;
    private CustomMappingType type;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: The %s-data: %s is not exist in the %s-step: %s";

    public CustomMappingDataException(String flowName,String stepName,String dataName, CustomMappingType type) {
        this.stepName=stepName;
        this.dataName=dataName;
        this.flowName=flowName;
        this.type=type;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName, type.toString(), dataName,type.toString(), stepName);
    }
}
