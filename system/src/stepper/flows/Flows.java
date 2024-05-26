package stepper.flows;

import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.excution.FlowExecution;
import stepper.roles.Role;

import java.io.Serializable;
import java.util.List;

public interface Flows extends Serializable {
    List<FlowDefinition> getAllFlows();
    void addFlow(FlowDefinition flowDefinition);
    List<FlowDefinition> getFlowsDefinitionsReadOnly();
    List<FlowDefinition> getFlowsDefinitionsByRole(Role role);
    List<String> GetFlowsNames();
    FlowDefinition getFlow(int flowNum);
    void addActivatedFlow(FlowExecution activatedFlow);
    List<FlowExecution> getActivatedFlows();
    List<String> getFlowsNamesReadOnly();
    FlowDefinition getFlowByName(String name);
}
