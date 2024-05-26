package stepper.flows;

import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.excution.FlowExecution;
import stepper.roles.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlowsImp implements Flows , Serializable
{
    List<FlowDefinition> allFlows;
    List<FlowExecution> activatedFlows;

    public FlowsImp()
    {
        this.allFlows=new ArrayList<>();
        activatedFlows=new ArrayList<>();
    }
    public List<FlowDefinition> getAllFlows() {
        return allFlows;
    }
    public void addFlow(FlowDefinition flowDefinition)
    {
        this.allFlows.add(flowDefinition);
    }
    public void addActivatedFlow(FlowExecution activatedFlow)
    {
        activatedFlows.add(0,activatedFlow);
    }
    @Override
    public List<String> GetFlowsNames()
    {
        List<String> names=new ArrayList<>();
        for(FlowDefinition flowDefinition:allFlows)
            names.add(flowDefinition.getName());
        return names;
    }
    @Override
    public FlowDefinition getFlowByName(String name)
    {
        for(FlowDefinition flow:allFlows)
        {
            if(Objects.equals(flow.getName(), name))
                return flow;
        }
        return null;
    }
    @Override
    public List<String> getFlowsNamesReadOnly()
    {
        List<String> names=new ArrayList<>();
        for(FlowDefinition flowDefinition:allFlows)
        {
            if(flowDefinition.isFlowReadOnly())
                names.add(flowDefinition.getName());
        }
        return names;
    }
    @Override
    public List<FlowDefinition> getFlowsDefinitionsReadOnly()
    {
        List<FlowDefinition> flows=new ArrayList<>();
        for(FlowDefinition flowDefinition:allFlows)
        {
            if(flowDefinition.isFlowReadOnly())
                flows.add(flowDefinition);
        }
        return flows;
    }
    @Override
    public List<FlowDefinition> getFlowsDefinitionsByRole(Role role)
    {
        List<FlowDefinition> flows=new ArrayList<>();
        for(String flow:role.getFlowsNames())
        {
            flows.add(getFlowByName(flow));
        }
        return flows;
    }
    public FlowDefinition getFlow(int flowNum)
    {
        return this.allFlows.get(flowNum);
    }
    public List<FlowExecution> getActivatedFlows() {
        return activatedFlows;
    }
}
