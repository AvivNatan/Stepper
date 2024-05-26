package stepper.xmlexceptions;

import java.io.Serializable;

public class CustomMappingStepException extends RuntimeException implements Serializable
{

    private String StepName;
    private CustomMappingType type;
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: The %s-step: %s is not exist in flow";

    public CustomMappingStepException(String flowName,String StepName, CustomMappingType type) {
        this.flowName=flowName;
       this.StepName=StepName;
       this.type=type;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName, type.toString(), StepName);
    }


}
