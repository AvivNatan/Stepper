package stepper.engine;

import dto.*;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.excution.FlowExecution;
import stepper.flows.Flows;
import stepper.roles.Role;
import stepper.roles.RoleManager;
import stepper.users.User;
import stepper.users.UserManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EngineSystem extends Serializable {
    String LoadXMLFile(InputStream file);//read from file the details of the flows
    void ExecutionFlow(FlowExecution OptionalFlowExecution,String userName);
     DtoStepExecutionDetails getStepExecutionDetails(UUID idFlow,String finalStepName);

    DtoFlowDescription ShowFlowDefinition(FlowDefinition flowDefinition);//show to user all data of flow
    DtoFlowExecutionDescription getExecutionDescription(FlowExecution flowExecution);
    DtoFlowPrevExecutionDescription getPrevExecutionDescription(FlowExecution flowExecution,UserManager userManager);
    DtoStatistics getStatistics();//get all Statistics on flows
    List<DtoFlowExecutionHistory> getFlowExecutionHistory(User user);
    DtoFlowsNames getFlowsNames();
    DtoActivatedFlows getActivatedFlowsDescription();
    DtoFreeInputs getFreeInputs(int selection);
    DtoFreeInputs getFreeInputsByFlowName(String name);
     Set<FlowDefinition> getFlowDefinitionsOfUser(String name);
     Set<FlowExecution> getFlowExecutionsOfUser(String name);
     void initUserDefinitionAndExecutions(String name);
     List<DtoFlowDescription> getAllFlowsDescriptions(String userName);
     FlowDefinition getFlowDefinitionByName(String name,Flows flows);
     DtoFlowPrevExecutionDescription getExecutionByID(UUID id,UserManager userManager);
     FlowExecution findActivatedExecutionByID(UUID id);
     Flows getAllFlowsAllFiles();
     void addOptionalExecution(String userName,FlowExecution optional);
     FlowExecution getOptionalExecution(String userName);
    void updateRolesAndUsers(RoleManager roleManager);

    void addRoleToUserUpdateDefinition(Role role, User user);
    void removeRoleFromUserUpdateDefinition(Role role,RoleManager roleManager, User user);
     Set<String> getFlowsNotAssignedOfRole(Role role);
     void addNewFlowOfRoleInUser(String flowName,Role role);
     void removeFlowOfRoleInUser(UserManager userManager,RoleManager roleManager,Role role);



}
