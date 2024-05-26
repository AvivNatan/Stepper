package stepper.xmlexceptions;

import java.io.Serializable;

public class NameDataException extends RuntimeException implements Serializable
{
    private String nameStep;
    private String dataName;
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: The data: %s doesnt exist in step: %s ";

    public NameDataException(String flowName,String nameStep,String dataName) {
        this.flowName=flowName;
        this.nameStep=nameStep;
        this.dataName=dataName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName,dataName, nameStep);
    }
}
