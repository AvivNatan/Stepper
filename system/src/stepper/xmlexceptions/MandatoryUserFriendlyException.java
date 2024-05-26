package stepper.xmlexceptions;

import java.io.Serializable;

public class MandatoryUserFriendlyException extends RuntimeException implements Serializable
{
    private String nameStep;
    private String dataName;
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: The input: %s in step: %s is not user friendly ";

    public MandatoryUserFriendlyException(String flowName,String nameStep,String dataName) {
        this.flowName=flowName;
        this.nameStep=nameStep;
        this.dataName=dataName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName,dataName, nameStep);
    }
}
