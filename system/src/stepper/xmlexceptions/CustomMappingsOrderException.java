package stepper.xmlexceptions;

import java.io.Serializable;

public class CustomMappingsOrderException extends RuntimeException implements Serializable
{
    private String prevStep;
    private String nextStep;
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: You cant custom mapping because the step: %s is previous to the step: %s";

    public CustomMappingsOrderException(String flowName,String prevStep, String nextStep) {
        this.flowName=flowName;
        this.prevStep=prevStep;
        this.nextStep=nextStep;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName, prevStep, nextStep);
    }
}
