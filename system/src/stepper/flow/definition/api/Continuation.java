package stepper.flow.definition.api;

import stepper.flow.excution.FlowExecution;

import java.util.ArrayList;
import java.util.List;

public class Continuation
{
    private String targetFlowName;
    private List<ContinuationMapping> mappings;

    public Continuation(String targetFlowName)
    {
        this.targetFlowName = targetFlowName;
        this.mappings=new ArrayList<>();
    }

    public String getTargetFlowName() {
        return targetFlowName;
    }

    public void setTargetFlowName(String targetFlowName) {
        this.targetFlowName = targetFlowName;
    }

    public List<ContinuationMapping> getMappings() {
        return mappings;
    }

    public void addMapping(ContinuationMapping mapping) {
        this.mappings.add(mapping);
    }
}
