package stepper.xmlexceptions;

import java.io.Serializable;

public class TypeOfDuplicatesInputsException extends RuntimeException implements Serializable
{
    private String duplicateInput;
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: The Input: %s has several types";

    public TypeOfDuplicatesInputsException(String flowName,String duplicateInput) {
        this.flowName=flowName;
        this.duplicateInput=duplicateInput;

    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName, duplicateInput);
    }
}
