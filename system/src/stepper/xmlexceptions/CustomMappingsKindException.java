package stepper.xmlexceptions;

import stepper.flow.definition.api.CustomMapping;

import java.io.Serializable;

public class CustomMappingsKindException extends RuntimeException implements Serializable
{
    private CustomMapping mapping;
    private String flowName;
    private final String EXCEPTION_MESSAGE = "Flow %s failed: The data:%s kind in step:%s not match to The data:%s kind in step:%s";

    public CustomMappingsKindException(String flowName,CustomMapping mapping) {
       this.flowName=flowName;
        this.mapping=mapping;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, flowName,mapping.getSourceData(),mapping.getSourceStep()
        ,mapping.getTargetData(),mapping.getTargetStep());
    }
}
