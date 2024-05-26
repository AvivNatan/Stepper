package stepper.xmlexceptions;

import java.io.Serializable;

public class DuplicateOutputNameException extends RuntimeException implements Serializable
{
    private String dataName;
    private String step1;
    private String step2;
    private String flowName;

    private final String EXCEPTION_MESSAGE = "Flow %s failed: The flow failed because output final name: %s is in the steps: %s , %s ";

    public DuplicateOutputNameException(String flowName,String dataName, String step1, String step2) {
        this.flowName=flowName;
        this.dataName=dataName;
        this.step1=step1;
        this.step2=step2;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,flowName, dataName,step1,step2);
    }
}
