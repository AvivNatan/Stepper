package stepper.xmlexceptions;

import java.io.Serializable;

public class NameFlowException extends RuntimeException implements Serializable
{
    String nameFlow;
    private final String EXCEPTION_MESSAGE = "The Flow Name: %s is already exist in system";

    public NameFlowException(String nameFlow) {
        this.nameFlow=nameFlow;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, nameFlow);
    }
}
