package stepper.xmlexceptions;

import java.io.Serializable;

public class NameStepException extends RuntimeException implements Serializable
{
    private String nameStep;
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: The Step Name: %s doesnt exist in system";

    public NameStepException(String flowName,String nameStep) {

        this.flowName=flowName;
        this.nameStep=nameStep;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName, nameStep);
    }
}
