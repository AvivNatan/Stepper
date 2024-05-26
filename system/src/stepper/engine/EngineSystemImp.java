package stepper.engine;

import dto.*;
import stepper.flow.definition.api.*;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.FlowExecution;
import stepper.flow.excution.StepExecutionData;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepExecutionContextImp;
import stepper.flow.excution.runner.FlowExecutor;
import stepper.flows.Flows;
import stepper.flows.FlowsImp;
import stepper.roles.Role;
import stepper.roles.RoleManager;
import stepper.step.StepDefinitionTypes;
import stepper.step.api.DataDefinitionDescription;
import stepper.step.api.StepDefinition;
import stepper.users.User;
import stepper.users.UserManager;
import stepper.xmlexceptions.*;
import xmlreader.schema.SchemaBasedJAXB;
import xmlreader.schema.generated.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EngineSystemImp implements EngineSystem,Serializable
{
    private final Flows allFlowsAllFiles;
    private Flows allFlows=null;
    private ExecutorService threadPoolExecutor;
    private int threadPoolNum=0;
    private final Map<String,FlowExecution> userToOptionalExecution;
    private final Map<String,Set<FlowExecution>> userToExecutions;
    private final Map<String,Set<FlowDefinition>> userToDefinitions;

    public EngineSystemImp()
    {
        userToOptionalExecution=new HashMap<>();
        allFlowsAllFiles=new FlowsImp();
        userToExecutions=new HashMap<>();
        userToDefinitions = new HashMap<>();
    }



    @Override
    public void updateRolesAndUsers(RoleManager roleManager)
    {
        if(roleManager.isRoleExists("Read Only Flows")&&roleManager.isRoleExists("All Flows"))
        {
            Role readOnly=roleManager.getRole("Read Only Flows");
            Role allFlows=roleManager.getRole("All Flows");
            List<String> readOnlyNames=allFlowsAllFiles.getFlowsNamesReadOnly();
            List<String> allFlowsNames=allFlowsAllFiles.GetFlowsNames();

                readOnly.getFlowsNames().addAll(readOnlyNames);
                for(String user:readOnly.getUsersNames())
                {
                    this.userToDefinitions.get(user).addAll(allFlowsAllFiles.getFlowsDefinitionsReadOnly());
                }
                allFlows.getFlowsNames().addAll(allFlowsNames);
                for(String user:allFlows.getUsersNames())
                {
                    userToDefinitions.get(user).addAll(allFlowsAllFiles.getAllFlows());
                }

        }

    }
    @Override
    public void addRoleToUserUpdateDefinition(Role role, User user)
    {
        if(Objects.equals(role.getNameRole(), "All Flows"))
        {
            userToDefinitions.get(user.getUserName()).addAll(allFlowsAllFiles.getAllFlows());
            user.setManager(true);
        }
        else if(Objects.equals(role.getNameRole(), "Read Only Flows"))
            userToDefinitions.get(user.getUserName()).addAll(allFlowsAllFiles.getFlowsDefinitionsReadOnly());
        else
            userToDefinitions.get(user.getUserName()).addAll(allFlowsAllFiles.getFlowsDefinitionsByRole(role));

        role.addUserToRole(user.getUserName());
        user.addRole(role.getNameRole());
    }
    @Override
    public void removeRoleFromUserUpdateDefinition(Role role,RoleManager roleManager, User user)
    {
        if(Objects.equals(role.getNameRole(), "All Flows"))
        {
            user.setManager(false);
        }
        role.RemoveUserFromRole(user.getUserName());
        user.RemoveRole(role.getNameRole());
        userToDefinitions.get(user.getUserName()).clear();
        for(String roleName:user.getRolesNames())
        {
            Role res=roleManager.getRole(roleName);
            addRoleToUserUpdateDefinition(res,user);
        }
    }

    @Override
    public void addOptionalExecution(String userName,FlowExecution optional)
    {
        this.userToOptionalExecution.put(userName,optional);
    }
    @Override
    public void initUserDefinitionAndExecutions(String name)
    {
        userToExecutions.put(name,new HashSet<>());
        userToDefinitions.put(name,new HashSet<>());
    }
    private void addDefinitionToUser(String name,FlowDefinition flowDefinition)
    {
        userToDefinitions.get(name).add(flowDefinition);
    }
    private void addExecutionToUser(String name,FlowExecution flowExecution)
    {
        userToExecutions.get(name).add(flowExecution);
    }
    @Override
    public  Set<FlowDefinition> getFlowDefinitionsOfUser(String name)
     {
         return userToDefinitions.get(name);
     }

    @Override
    public synchronized Set<FlowExecution> getFlowExecutionsOfUser(String name)
    {
        return userToExecutions.get(name);
    }

    @Override
    public synchronized FlowExecution getOptionalExecution(String userName)
    {
        return userToOptionalExecution.get(userName);
    }

    @Override
    public Set<String> getFlowsNotAssignedOfRole(Role role)
    {
        Set<String> res=new HashSet<>();
        for(FlowDefinition flowDefinition:allFlowsAllFiles.getAllFlows())
        {
            if(!role.getFlowsNames().contains(flowDefinition.getName()))
                res.add(flowDefinition.getName());
        }
        return res;
    }

    @Override
    public void addNewFlowOfRoleInUser(String flowName,Role role)
    {
        FlowDefinition flow=allFlowsAllFiles.getFlowByName(flowName);
        for(String userName:role.getUsersNames())
           this.userToDefinitions.get(userName).add(flow);
    }
    @Override
    public void removeFlowOfRoleInUser(UserManager userManager,RoleManager roleManager,Role role)
    {
        for(String userName:role.getUsersNames())
        {
            User user = userManager.getUserByName(userName);
            userToDefinitions.get(userName).clear();
            for(String roleName:user.getRolesNames())
            {
                addRoleToUserUpdateDefinition(roleManager.getRole(roleName), user);
            }
        }
    }



    @Override
    public String LoadXMLFile(InputStream file)
    {
        SchemaBasedJAXB jb = new SchemaBasedJAXB();
        STStepper stepper=jb.getStepperFromXmlFile(file);
        this.allFlows=getAllFlowsFromXml(stepper.getSTFlows());
        checkAllFlowsStructure(stepper.getSTFlows());
        if(this.threadPoolNum==0)
        {
            this.threadPoolNum=stepper.getSTThreadPool();
            this.threadPoolExecutor= Executors.newFixedThreadPool(this.threadPoolNum);
        }
        addFlowsToCurrentFlows(this.allFlows);
        return "The XML file load successfully";
    }

    public void checkAllFlowsStructure(STFlows flows)
    {
        for (int i = 0; i < allFlows.getAllFlows().size(); i++) {
            checkStructureOfFlow(flows.getSTFlow().get(i), allFlows.getAllFlows().get(i));
        }
        for(STFlow stFlow: flows.getSTFlow())
        {
            FlowDefinition flow=getFlowDefinitionByName(stFlow.getName(),this.allFlows);
            if(stFlow.getSTContinuations()!=null)
            {
                for(STContinuation continuation:stFlow.getSTContinuations().getSTContinuation())
                    getAndCheckContinuationMappingsFromXml(continuation,flow);
            }

        }
    }
    public void addFlowsToCurrentFlows(Flows flows)
    {
        if(flows.getAllFlows().size()!=0)
        {
            for(FlowDefinition flow:flows.getAllFlows())
            {
                if(!isFlowNameExistInSystem(flow.getName()))
                    this.allFlowsAllFiles.getAllFlows().add(flow);
            }
        }
    }
    private Boolean isFlowNameExistInSystem(String name)
    {
        List<String> names=this.allFlowsAllFiles.GetFlowsNames();
        for(String flow:names)
            if(flow.equals(name))
                return true;
        return false;
    }

    public Flows getAllFlowsFromXml(STFlows flows)
    {
        Flows res=new FlowsImp();
        for(STFlow flow: flows.getSTFlow())
            res.addFlow(getAndCheckFlowDefinitionFromXml(flow,res.getAllFlows()));
        return res;
    }
    public FlowDefinition getAndCheckFlowDefinitionFromXml(STFlow stFlow, List<FlowDefinition> list)
    {
        checkIfFlowNameExist(stFlow.getName(),list);//check no duplicates flows
        return new FlowDefinitionImp(stFlow.getName(),stFlow.getSTFlowDescription());
    }

    public void checkStructureOfFlow(STFlow stFlow,FlowDefinition flow) {
        if (!Objects.equals(stFlow.getSTFlowOutput(), "")) {
            String[] substrings = stFlow.getSTFlowOutput().split(",");
            for (String substring : substrings) {
                flow.addFormalOutputs(substring);
            }
        }
        getStepsDefinitionFromXml(stFlow.getSTStepsInFlow(), flow);
        if (stFlow.getSTFlowLevelAliasing() != null)
            getDataUsagesFromXml(stFlow.getSTFlowLevelAliasing(), flow);
        flow.checkDuplicatesOutputsNames();
        flow.checkTypeOfDuplicatesInputs();
        flow.checkFormalOutputsExist();
        flow.buildInputToOutputList();
        if (stFlow.getSTCustomMappings() != null) {
            getCustomMappingsFromXml(stFlow.getSTCustomMappings(), flow);
            checkCustomMappings(flow.getCustomMappings(), flow);
        }
        flow.getCustomMappingInputToOutput();
        flow.automaticMapping();
        if (stFlow.getSTInitialInputValues() != null)
        {
            getAndCheckInitialInputValuesFromXml(stFlow.getSTInitialInputValues(), flow);
        }
        flow.mergeFreeInputs();
        flow.checkMandatoryInputsUserFriendly();
        flow.setReadOnly();
    }
    public void checkIfFlowNameExist(String flowName,List<FlowDefinition> list)
    {
        for(FlowDefinition flow:list) {
            if (Objects.equals(flow.getName(), flowName))
                throw new NameFlowException(flowName);
        }//now I want to add the flow because the name doesnt exit
    }
    public Boolean checkIfFlowNameExistContinuation(String flowName)
    {
        boolean res1=false;
        for(FlowDefinition flow:this.allFlowsAllFiles.getAllFlows())
        {
            if (Objects.equals(flow.getName(), flowName))
                res1= true;
        }
        boolean res2=false;
        for(FlowDefinition flow:this.allFlows.getAllFlows())
        {
            if (Objects.equals(flow.getName(), flowName))
                res2= true;
        }
         return (res1||res2);
    }

    public void getStepsDefinitionFromXml(STStepsInFlow steps,FlowDefinition flow)
    {
        for(STStepInFlow step: steps.getSTStepInFlow())
            flow.addStepToFlow(getAndCheckStepDefinitionFromXml(step,flow.getName()));
    }
    public StepUsageDescription getAndCheckStepDefinitionFromXml(STStepInFlow step,String flowName)
    {
        StepDefinition stepDefinition=isStepDefinitionTypeExist(step.getName(),flowName);
        String alias=step.getAlias();
        Boolean skip=step.isContinueIfFailing();
        StepUsageDescription res;
        if(alias!=null && skip!=null)
            res=new StepUsageDescriptionImp(stepDefinition, skip,alias);
        else if (alias!=null)
            res=new StepUsageDescriptionImp(stepDefinition,alias);
        else
            res=new StepUsageDescriptionImp(stepDefinition);

        return res;
    }
    public StepDefinition isStepDefinitionTypeExist(String name,String nameFlow) {

        StepDefinition res=null;


        for (StepDefinitionTypes type : StepDefinitionTypes.values()) {
            if (Objects.equals(type.originalStepName(), name)) {
                res = type.getStepDefinition();
                break;
            }
        }
        if(res==null)
            throw new NameStepException(nameFlow,name);
        else
            return res;
    }
    public void getDataUsagesFromXml(STFlowLevelAliasing flowLevelAliasing,FlowDefinition flow)
    {
        for(STFlowLevelAlias alias: flowLevelAliasing.getSTFlowLevelAlias())
            getDataUsageFromXml(alias,flow);
    }
    public void getDataUsageFromXml(STFlowLevelAlias flowLevelAlias,FlowDefinition flow)
    {
        String finalNameStep= flowLevelAlias.getStep();
        if(!flow.isStepExist(finalNameStep))
            throw new NameStepException(flow.getName(),"alias:"+finalNameStep);
        StepUsageDescription step=flow.getStepUsageDescription(finalNameStep);
        String OriginalDataName=flowLevelAlias.getSourceDataName();
        String finalDataName=flowLevelAlias.getAlias();
        DataUsageDescription data=step.getDataUsageDescriptionByOriginalName(OriginalDataName);
        if(data==null)
            throw new NameDataException(flow.getName(),step.getFinalStepName(),OriginalDataName);

        data.setFinalName(finalDataName);

    }
    public void getCustomMappingsFromXml(STCustomMappings mappings,FlowDefinition flow)
    {
        for(STCustomMapping mapping: mappings.getSTCustomMapping())
            flow.addCustomMapping(getCustomMappingFromXml(mapping));
    }
    public void getAndCheckContinuationMappingsFromXml(STContinuation continuation, FlowDefinition flow)
    {
        if(!checkIfFlowNameExistContinuation(continuation.getTargetFlow()))
        {
            throw new ContinuationFlowExistException(continuation.getTargetFlow());
        }
        Continuation res=new Continuation(continuation.getTargetFlow());
        if(continuation.getSTContinuationMapping()!=null)
        {
            for(STContinuationMapping mapping: continuation.getSTContinuationMapping())
            {
                res.addMapping(new ContinuationMapping(mapping.getSourceData(),mapping.getTargetData()));
            }
            checkContinuationMapping(res,flow);
        }
        flow.addContinuation(res);
    }
    public void getAndCheckInitialInputValuesFromXml(STInitialInputValues values,FlowDefinition flow)
    {

        for(STInitialInputValue value:values.getSTInitialInputValue())
        {
            if(!flow.isDataInFlow(value.getInputName()))
                throw new InitialInputValueDataException(flow.getName(), value.getInputName());
            flow.addInitialInputValues(new InitialInputValue(value.getInputName(),value.getInitialValue()));
        }
    }
    public void checkContinuationMapping(Continuation continuation,FlowDefinition flow)
    {
        FlowDefinition definition;
        definition=getFlowDefinitionByName(continuation.getTargetFlowName(),this.allFlowsAllFiles);
        if(definition==null)
             definition=getFlowDefinitionByName(continuation.getTargetFlowName(),this.allFlows);
        for(ContinuationMapping mapping:continuation.getMappings())
        {
            if(!flow.isDataInFlow(mapping.getSourceDataName()))
                throw new ContinuationMappingDataException(flow.getName(), mapping.getSourceDataName(), CustomMappingType.SOURCE);
            if(!definition.isDataInFlow(mapping.getTargetDataName()))
                throw new ContinuationMappingDataException(definition.getName(), mapping.getTargetDataName(), CustomMappingType.TARGET);
        }
    }
    public CustomMapping getCustomMappingFromXml(STCustomMapping mapping)
    {
        CustomMapping resMapping=new CustomMapping(mapping.getSourceStep(), mapping.getSourceData(),
                mapping.getTargetStep(), mapping.getTargetData());
        return resMapping;
    }
    public void checkCustomMapping(CustomMapping mapping,FlowDefinition flow)
    {
        if(!flow.isStepExist(mapping.getSourceStep()))
        {
            throw new CustomMappingStepException(flow.getName(),mapping.getSourceStep(), CustomMappingType.SOURCE);
        }
        if(!flow.isStepExist(mapping.getTargetStep()))
        {
            throw new CustomMappingStepException(flow.getName(),mapping.getTargetStep(), CustomMappingType.TARGET);
        }
        StepUsageDescription sourceStep=flow.getStepUsageDescription(mapping.getSourceStep());
        if(!sourceStep.isDataUsageInStep(mapping.getSourceData()))
        {
            throw new CustomMappingDataException(flow.getName(),sourceStep.getFinalStepName(),mapping.getSourceData(),CustomMappingType.SOURCE);
        }
        StepUsageDescription targetStep=flow.getStepUsageDescription(mapping.getTargetStep());
        if(!targetStep.isDataUsageInStep(mapping.getTargetData()))
        {
            throw new CustomMappingDataException(flow.getName(),targetStep.getFinalStepName(),mapping.getTargetData(),CustomMappingType.TARGET);
        }
    }
    public void checkCustomMappings(List<CustomMapping> list,FlowDefinition flow)
    {
        for(CustomMapping mapping:list)
        {
            checkCustomMapping(mapping,flow);
            if(!flow.isPrevStepBeforeNextStep(mapping.getSourceStep(),mapping.getTargetStep()))
                throw new CustomMappingsOrderException(flow.getName(),mapping.getSourceStep(),mapping.getTargetStep());
            if(!flow.isOutputStepMatchToInputStep(mapping))
                throw new CustomMappingsKindException(flow.getName(),mapping);
        }

    }
    @Override
    public DtoFlowDescription ShowFlowDefinition(FlowDefinition flowDefinition)
    {
        DtoFlowDescription dtoFlowDescription=new DtoFlowDescription(flowDefinition.getName(),
                flowDefinition.getDescription(),flowDefinition.isFlowReadOnly());
        for(StepUsageDescription step: flowDefinition.getFlowSteps())
        {
            dtoFlowDescription.addStep(new DtoStepDescription(step.getFinalStepName(),
                    step.getStepDefinition().originalStepName(),
                    step.getStepDefinition().isReadonly()));
        }
        for(StepUsageDescription step: flowDefinition.getFlowSteps()) {
            dtoFlowDescription.addStepMoreInfo(getStepMoreInfo(step,flowDefinition));
        }
        dtoFlowDescription.setFormalOutputs(flowDefinition.getFlowFormalOutputsNames());
        for(DataUsageDescription data: flowDefinition.getFinalFreeInputs()) {
            dtoFlowDescription.addFreeInput(new DtoInputDescription(data.getFinalName()
                    ,data.getDataDefinition().dataDefinition().getType().getSimpleName(), data.getDataDefinition().necessity(),
                    getNamesStepsOfInput(data.getFinalName(),flowDefinition)));
        }
        for(StepUsageDescription step: flowDefinition.getFlowSteps())
        {
            for(DataDefinitionDescription data:step.getStepDefinition().outputs())
            {
                dtoFlowDescription.addOutput(new DtoOutputDescription(data.getOriginalDataName(),
                        data.dataDefinition().getType().getSimpleName(), step.getFinalStepName()));
            }
        }
        dtoFlowDescription.setContinuationsNum(flowDefinition.getContinuations().size());
        dtoFlowDescription.setFreeInputsNum();
        dtoFlowDescription.setStepsNum();
        return dtoFlowDescription;
    }
    public DtoStepMoreDescription getStepMoreInfo(StepUsageDescription usage,FlowDefinition definition)
    {
        DtoDataMoreDescription dtoData;
        DtoStepMoreDescription res=new DtoStepMoreDescription(usage.getFinalStepName());
        for(DataUsageDescription data: usage.getInputs())
        {
            dtoData=new DtoDataMoreDescription(data.getFinalName(),data.getDataDefinition().necessity());
            DataUsageDescription outputOptional=definition.getInputToOutputValue(data.getFinalName());
            if(outputOptional!=null)
            {
                dtoData.setConnected(true);
                dtoData.setDataConnectedName(outputOptional.getFinalName());
                dtoData.setStepConnectedName(outputOptional.getFinalStepName());
            }
            res.addInput(dtoData);
        }
        for(DataUsageDescription data: usage.getOutputs())
        {
            dtoData=new DtoDataMoreDescription(data.getFinalName(),data.getDataDefinition().necessity());
            DataUsageDescription inputOptional=definition.getOutputToInputValue(data.getFinalName());
            if(inputOptional!=null)
            {
                dtoData.setConnected(true);
                dtoData.setDataConnectedName(inputOptional.getFinalName());
                dtoData.setStepConnectedName(inputOptional.getFinalStepName());
            }
            res.addOutput(dtoData);
        }
        return res;
    }

    @Override
    public void ExecutionFlow(FlowExecution OptionalFlowExecution,String userName)
    {
        allFlowsAllFiles.addActivatedFlow(OptionalFlowExecution);//add the execution flow to activated list
        addExecutionToUser(userName,OptionalFlowExecution);
        StepExecutionContext context=new StepExecutionContextImp(findActivatedExecutionByID(OptionalFlowExecution.getUniqueId()));
        threadPoolExecutor.execute(new FlowExecutor(context));
    }
   @Override
    public DtoFlowExecutionDescription getExecutionDescription(FlowExecution flowExecution)
    {
        DtoFlowExecutionDescription dto=new DtoFlowExecutionDescription(flowExecution.getUniqueId(),
                flowExecution.getFlowDefinition().getName(),flowExecution.getFlowExecutionResult());
        for (String outputName : flowExecution.getFormalOutputs())
        {
            DataInFlow out=flowExecution.getDataValues().get(outputName);
            if(out==null)
            {
                DataUsageDescription output=flowExecution.getFlowDefinition().getFormalOutput(outputName);
                dto.addUserStringAndName(output.getFinalName(),output.getDataDefinition().userString(),null);
            }
            else
                dto.addUserStringAndName(out.getDataUsageDefinition().getFinalName(),
                        out.getDataUsageDefinition().getDataDefinition().userString(),out.getContent());

        }
        return dto;
    }

    public synchronized Flows getAllFlowsAllFiles() {
        return allFlowsAllFiles;
    }

    @Override
    public FlowExecution findActivatedExecutionByID(UUID id)
    {
        if(id!=null)
        {
            for(FlowExecution flow:allFlowsAllFiles.getActivatedFlows())
                if(flow.getUniqueId().equals(id))
                    return flow;
        }
        return null;
    }
    @Override
    public DtoFlowPrevExecutionDescription getExecutionByID(UUID id,UserManager userManager)
    {
        return getPrevExecutionDescription(findActivatedExecutionByID(id),userManager);
    }
    @Override
    public DtoStepExecutionDetails getStepExecutionDetails(UUID idFlow,String finalStepName)
    {
        FlowExecution execution=findActivatedExecutionByID(idFlow);
        StepExecutionData stepData=execution.findStepExecutionData(finalStepName);
        DtoStepExecutionDetails details=new DtoStepExecutionDetails(stepData.getFinalName(),
                stepData.getResult(),stepData.getStepStartTime(),stepData.getStepEndTime(),stepData.getStepTotalTime(),stepData.getStepLogs());
        for(DataUsageDescription data:stepData.getUsage().getInputs())
        {
            DataUsageDescription optionalData=execution.getFlowDefinition().getInputToOutputValue(data.getFinalName());
            if(execution.getDataValues().get(data.getFinalName())!=null)
            {
                Object content=execution.getDataValues().get(data.getFinalName()).getContent();
                if(content!=null)
                {
                    DtoInputOutputForStepDetails input=new DtoInputOutputForStepDetails(
                            data.getFinalName(),content,data.getDataDefinition().dataDefinition().getType().getSimpleName());
                    details.addInput(input);
                }
            }
            else
            {
                if(optionalData!=null)
                {
                    if (execution.getDataValues().get(optionalData.getFinalName()) != null)
                    {
                        Object content=execution.getDataValues().get(optionalData.getFinalName()).getContent();
                        if(content!=null)
                        {
                            DtoInputOutputForStepDetails input=new DtoInputOutputForStepDetails(
                                    data.getFinalName(),content,data.getDataDefinition().dataDefinition().getType().getSimpleName());
                            details.addInput(input);
                        }
                    }
                }

            }
        }
        for(DataUsageDescription data:stepData.getUsage().getOutputs())
        {
            if(execution.getDataValues().get(data.getFinalName())!=null)
            {
                Object content=execution.getDataValues().get(data.getFinalName()).getContent();
                DtoInputOutputForStepDetails output=new DtoInputOutputForStepDetails(
                        data.getFinalName(),content,data.getDataDefinition().dataDefinition().getType().getSimpleName());
                details.addOutput(output);
            }
        }
        return details;
    }

    private List<String> getContinuationFlowsByRole(List<String> continuations,String userName)
    {
        List<String> res=new ArrayList<>();
        boolean stop;
        for(String continuation:continuations)
        {
            stop=false;
            for(FlowDefinition flowDefinition:this.userToDefinitions.get(userName))
            {
                if(flowDefinition.getName().equals(continuation))
                    stop=true;
            }
            if(stop)
                res.add(continuation);
        }
        return res;
    }
    @Override
    public DtoFlowPrevExecutionDescription getPrevExecutionDescription(FlowExecution flowExecution,UserManager userManager) {
        List<String> con;
        con=getContinuationFlowsByRole(flowExecution.getFlowDefinition().getContinuationFlowsNames(),flowExecution.getUserName());
        User user=userManager.getUserByName(flowExecution.getUserName());

        DtoFlowPrevExecutionDescription dto=new
                DtoFlowPrevExecutionDescription(flowExecution.getUniqueId(),flowExecution.getUserName(),user.getRolesNames().contains("All Flows")?"Manager":"Normal",
                flowExecution.getFlowDefinition().getName(),flowExecution.getStartTime(),flowExecution.getFlowExecutionResult(),
                flowExecution.getTotalTime(), con);

            for (Map.Entry<String, DataInFlow> entry : flowExecution.getFreeInputs().entrySet())
            {
                DataUsageDescription optional=flowExecution.getFlowDefinition().isDoubleFreeInput(entry.getKey());
                if(optional!=null)
                {
                    dto.addFreeInputExecution(new DtoFreeInputOutputExecution
                            (entry.getKey()+"("+optional.getOriginalName()+")",optional.getFinalStepName(), optional.getDataDefinition().dataDefinition().getType().getSimpleName(),
                                    entry.getValue().getContent(),
                                    optional.getDataDefinition().necessity()));
                }
                dto.addFreeInputExecution(new DtoFreeInputOutputExecution
                        (entry.getKey(),entry.getValue().getDataUsageDefinition().getFinalStepName(), entry.getValue().getDataUsageDefinition().getDataDefinition().dataDefinition().getType().getSimpleName(),
                                entry.getValue().getContent(),
                                entry.getValue().getDataUsageDefinition().getDataDefinition().necessity()));
            }
            DataInFlow output;
            for(DataUsageDescription data:flowExecution.getFlowDefinition().getListFinalNamesOfOutputs())
            {
                output=flowExecution.getDataValues().get(data.getFinalName());
                if(output!=null)
                {
                    dto.addOutputExecution(new DtoFreeInputOutputExecution
                            (data.getFinalName(),output.getDataUsageDefinition().getFinalStepName(), data.getDataDefinition().dataDefinition().getType().getSimpleName(),
                                    output.getContent(), data.getDataDefinition().necessity()));
                }
                else
                {
                    dto.addOutputExecution(new DtoFreeInputOutputExecution
                            (data.getFinalName(), data.getFinalStepName(),data.getDataDefinition().dataDefinition().getType().getSimpleName(),
                                   null, data.getDataDefinition().necessity()));
                }
            }
            for(StepExecutionData step:flowExecution.getStepsData())
            {
                dto.addStepExecution(new DtoStepExecution(step.getFinalName(),
                        step.getStepTotalTime(),step.getResult(),step.getStepSummeryLine(),step.getStepLogs()));

            }
            return dto;
    }
    public List<String> getNamesStepsOfInput(String name,FlowDefinition flowDefinition)
    {
        List<String> NamesInput=new ArrayList<>();
        for(StepUsageDescription step: flowDefinition.getFlowSteps()) {
            if(step.isDataUsageInStep(name))
                NamesInput.add(step.getFinalStepName());
        }
        return NamesInput;
    }


    @Override
    public DtoStatistics getStatistics()
    {
        DtoStatistics dto=new DtoStatistics();
        updateFlowsStatistics(dto);
        updateStepsStatistics(dto);
        return dto;
    }
    @Override
    public List<DtoFlowExecutionHistory> getFlowExecutionHistory(User user)
    {
        List<DtoFlowExecutionHistory> res=new ArrayList<>();
        DtoFlowExecutionHistory executionHistory;

        if(user!=null&&!user.getRolesNames().contains("All Flows"))
        {
            for(FlowExecution flow:this.userToExecutions.get(user.getUserName()))
            {
                if(flow.getFlowExecutionResult()!=null)
                {
                    executionHistory=new DtoFlowExecutionHistory(flow.getFlowDefinition().getName(),
                            flow.getStartTime(), flow.getFlowExecutionResult().toString(),flow.getUniqueId());
                    res.add(executionHistory);
                }
            }

        }
        else
        {
            for(FlowExecution flow:allFlowsAllFiles.getActivatedFlows())
            {
                if(flow.getFlowExecutionResult()!=null)
                {
                    executionHistory=new DtoFlowExecutionHistory(flow.getFlowDefinition().getName(),
                            flow.getStartTime(), flow.getFlowExecutionResult().toString(),flow.getUniqueId());
                    res.add(executionHistory);
                }
            }
        }
        return res;
    }
    private void updateFlowsStatistics(DtoStatistics dto)
    {
        long numFlow;
        long time=0;
        for(FlowDefinition definition: allFlowsAllFiles.getAllFlows())
        {
            numFlow=0;
            for(FlowExecution execution:allFlowsAllFiles.getActivatedFlows())
            {
                    if(execution.getFlowDefinition()==definition)
                    {
                        if(execution.getFlowExecutionResult()!=null) {
                            numFlow++;
                            time=time+execution.getTotalTime().toMillis();
                        }
                    }
            }
            if(numFlow!=0)
                dto.addFlowStatistics(new DtoFlowStepStatistics(definition.getName(),(int)numFlow,(time/numFlow)));
        }
    }
    private void updateStepsStatistics(DtoStatistics dto)
    {
        long numStep;
        long time=0;
        for(StepDefinition definition: StepDefinitionTypes.values())
        {
            numStep=0;
            for(FlowExecution execution:allFlowsAllFiles.getActivatedFlows())
            {
                for (StepExecutionData step: execution.getStepsData())
                {
                    if(Objects.equals(step.getStepDefinition().originalStepName(), definition.originalStepName()))
                    {
                        if(step.getResult()!=null)
                        {
                            numStep++;
                            time=time+step.getStepTotalTime().toMillis();
                        }
                    }
                }

            }
            if(numStep!=0)
                dto.addStepStatistics(new DtoFlowStepStatistics(definition.originalStepName(),(int)numStep,(time/numStep)));
        }
    }
    @Override
    public DtoFlowsNames getFlowsNames()
    {
        DtoFlowsNames dtoFlowsNames=new DtoFlowsNames();
        dtoFlowsNames.setFlowsNames(allFlowsAllFiles.GetFlowsNames());
        return dtoFlowsNames;
    }
    @Override
    public DtoActivatedFlows getActivatedFlowsDescription()
    {
        DtoActivatedFlows dtoActivatedFlows=new DtoActivatedFlows();
        for(FlowExecution flowExecution :allFlowsAllFiles.getActivatedFlows()) {
            dtoActivatedFlows.addActivatedFlow(new DtoActivatedFlowDescription
                    (flowExecution.getUniqueId(),flowExecution.getFlowDefinition().getName(),flowExecution.getStartTime()));

        }
        return dtoActivatedFlows;
    }
    @Override
    public DtoFreeInputs getFreeInputs(int selection) {
        DtoFreeInputs freeInputs=new DtoFreeInputs();
        for(DataUsageDescription data: allFlowsAllFiles.getFlow(selection-1).getFinalFreeInputs())
        {
            freeInputs.addFreeInput(new DtoFreeInputDescription(data.getDataDefinition().userString(),
                    data.getFinalName(),
                    data.getDataDefinition().necessity(), data.getDataDefinition()));
        }
        return freeInputs;
    }
    @Override
    public DtoFreeInputs getFreeInputsByFlowName(String name) {
        DtoFreeInputs freeInputs=new DtoFreeInputs();

        for(DataUsageDescription data: getFlowDefinitionByName(name,this.allFlowsAllFiles).getFinalFreeInputs()) {
            freeInputs.addFreeInput(new DtoFreeInputDescription(data.getDataDefinition().userString(),
                    data.getFinalName(),
                    data.getDataDefinition().necessity(), data.getDataDefinition()));
        }
        return freeInputs;
    }

    @Override
    public FlowDefinition getFlowDefinitionByName(String name,Flows flows)
    {
        for(FlowDefinition flow:flows.getAllFlows())
        {
            if(Objects.equals(name, flow.getName()))
                return flow;
        }
        return null;
    }

    @Override
    public List<DtoFlowDescription> getAllFlowsDescriptions(String userName) {
        List<DtoFlowDescription> list=new ArrayList<>();
        for(FlowDefinition flowDefinition:this.userToDefinitions.get(userName))
        {
            list.add(ShowFlowDefinition(flowDefinition));
        }
        return list;
    }

}
