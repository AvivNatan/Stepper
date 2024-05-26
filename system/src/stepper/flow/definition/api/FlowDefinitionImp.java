package stepper.flow.definition.api;

import stepper.dd.api.DataDefinition;
import stepper.step.api.DataNecessity;
import stepper.xmlexceptions.DuplicateOutputNameException;
import stepper.xmlexceptions.FormalOutputException;
import stepper.xmlexceptions.MandatoryUserFriendlyException;
import stepper.xmlexceptions.TypeOfDuplicatesInputsException;

import java.io.Serializable;
import java.util.*;

public class FlowDefinitionImp implements FlowDefinition , Serializable
{
    private final String name;
    private final String description;
    private final List<String> formalOutputsFlowNames;
    private final List<StepUsageDescription> steps;
    private final Map<DataUsageDescription,DataUsageDescription> inputsToOutputs;
    private final List<DataUsageDescription> currentOutputs;
    private final List<DataUsageDescription> freeInputsFlow;
    private final List<DataUsageDescription> doubleFreeInputsFlow;
    private final List<DataUsageDescription> finalFreeInputs;
    private boolean isFlowReadOnly;
    private List<CustomMapping> customMappings;
    private List<Continuation> continuations;
    private List<InitialInputValue> initialList;


    public FlowDefinitionImp(String name, String description) {
        this.name = name;
        this.description = description;
        formalOutputsFlowNames = new ArrayList<>();
        freeInputsFlow=new ArrayList<>();
        steps = new ArrayList<>();
        customMappings=new ArrayList<>();
        currentOutputs=new ArrayList<>();
        inputsToOutputs=new LinkedHashMap<>();
        finalFreeInputs=new ArrayList<>();
        this.doubleFreeInputsFlow=new ArrayList<>();
        this.continuations=new ArrayList<>();
        this.initialList=new ArrayList<>();
    }

    @Override
    public List<InitialInputValue> getInitialList() {
        return initialList;
    }

    @Override
    public List<Continuation> getContinuations() {
        return continuations;
    }

    @Override
    public DataUsageDescription getInputToOutputValue(String finalInputName) {
        for (Map.Entry<DataUsageDescription, DataUsageDescription> entry : inputsToOutputs.entrySet()) {
            {
                if (Objects.equals(entry.getKey().getFinalName(), finalInputName))
                {
                        return entry.getValue();
                }
            }

        }
        return null;
    }
    @Override
    public void addInitialInputValues(InitialInputValue initialInputValues)
    {
        this.initialList.add(initialInputValues);
    }
    @Override
    public void addContinuation(Continuation continuation)
    {
        this.continuations.add(continuation);
    }


    @Override
    public DataUsageDescription getOutputToInputValue(String finalOutputName) {
        for (Map.Entry<DataUsageDescription, DataUsageDescription> entry : inputsToOutputs.entrySet()) {
            {
                if (entry.getValue()!=null&&Objects.equals(entry.getValue().getFinalName(), finalOutputName)) {
                    return entry.getKey();
                }
            }

        }
        return null;
    }

    @Override
    public List<DataUsageDescription> getFinalFreeInputs() {
        return finalFreeInputs;
    }
    @Override
    public void checkMandatoryInputsUserFriendly() {
        for(DataUsageDescription input:finalFreeInputs)
        {
            if(!input.getDataDefinition().dataDefinition().isUserFriendly()&&
                    input.getDataDefinition().necessity()== DataNecessity.MANDATORY)
                throw new MandatoryUserFriendlyException(name,input.getFinalName(),input.getFinalStepName());
        }

    }
    @Override
    public DataUsageDescription isDoubleFreeInput(String finalName)
    {
        for(DataUsageDescription data:doubleFreeInputsFlow)
            if(Objects.equals(data.getFinalName(), finalName))
                return data;

        return null;
    }
    @Override
    public DataUsageDescription getFormalOutput(String formalOutputName)
    {
        List<DataUsageDescription> outputs;
        for(StepUsageDescription step:steps)
        {
            outputs=step.getOutputs();
            for(DataUsageDescription output:outputs)
            {
                if(Objects.equals(formalOutputName, output.getFinalName()))
                    return output;
            }
        }
        return null;
    }
    @Override
    public boolean checkFormalOutputExist(String formalOutputName)
    {
        List<DataUsageDescription> outputs;
        for(StepUsageDescription step:steps)
        {
            outputs=step.getOutputs();
            for(DataUsageDescription output:outputs)
            {
                if(Objects.equals(formalOutputName, output.getFinalName()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void checkFormalOutputsExist() {
        for(String formalOutput: formalOutputsFlowNames)
        {
            if(!checkFormalOutputExist(formalOutput))
                throw new FormalOutputException(name,formalOutput);
        }
    }
    @Override
    public List<DataUsageDescription> getAllInputs()
    {
        List<DataUsageDescription> res=new ArrayList<>();
        for(StepUsageDescription step:steps)
        {
            res.addAll(step.getInputs());
        }
        return res;
    }

    @Override
    public void checkTypeOfDuplicatesInputs()
    {
        List<DataUsageDescription> inputs=getAllInputs();
        for (int i = 0; i < inputs.size(); i++)
        {
            for(int j=i+1;j<inputs.size();j++)
            {
                if (Objects.equals(inputs.get(i).getFinalName(), inputs.get(j).getFinalName()) &&
                        inputs.get(i).getDataDefinition().dataDefinition().getType() != inputs.get(j).getDataDefinition().dataDefinition().getType())
                {
                    throw new TypeOfDuplicatesInputsException(name,inputs.get(i).getFinalName());
                }
            }
        }
    }

    @Override
    public boolean isFlowReadOnly() {
        return isFlowReadOnly;
    }

    @Override
    public void setReadOnly()
    {
        this.isFlowReadOnly=true;
        for(StepUsageDescription stepDes : steps)
        {
            if(!stepDes.getStepDefinition().isReadonly()) {
                this.isFlowReadOnly = false;
                break;
            }
        }
    }
    @Override
    public void checkDuplicatesOutputsNames()
    {
        List<DataUsageDescription> outputs=this.getListFinalNamesOfOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            for(int j=i+1;j<outputs.size();j++)
            {
                if(outputs.get(i).getFinalName().equals(outputs.get(j).getFinalName()))
                {
                    throw new DuplicateOutputNameException(name,outputs.get(i).getFinalName(),
                            outputs.get(i).getFinalStepName(),outputs.get(j).getFinalStepName());

                }
            }
        }
    }
    @Override
    public List<DataUsageDescription> getListFinalNamesOfOutputs()
    {
        List<DataUsageDescription> res=new ArrayList<>();
        for (StepUsageDescription step:steps)
        {
            for(DataUsageDescription data:step.getDataDefinitionFlow())
            {
                if(data.isOutput())
                    res.add(data);
            }
        }
        return res;
    }

    @Override
    public StepUsageDescription getStepUsageDescription(String finalName) {
        for(StepUsageDescription step:steps)
        {
            if(Objects.equals(step.getFinalStepName(), finalName))
                return step;
        }
        return null;
    }

    @Override
    public List<CustomMapping> getCustomMappings() {
        return customMappings;
    }

    @Override
    public boolean isStepExist(String finalStepName) {
        for(StepUsageDescription step:steps)
        {
            if(Objects.equals(step.getFinalStepName(), finalStepName))
                return true;
        }
        return false;
    }

    @Override
    public void addCustomMapping(CustomMapping mapping) {
        customMappings.add(mapping);
    }

    @Override
    public boolean isPrevStepBeforeNextStep(String prev, String next)
    {
        return findIndexOfStep(prev) < findIndexOfStep(next);
    }

    @Override
    public boolean isOutputStepMatchToInputStep(CustomMapping mapping) {
        StepUsageDescription step1=getStepUsageDescription(mapping.getSourceStep());
        StepUsageDescription step2=getStepUsageDescription(mapping.getTargetStep());
        DataDefinition def1=step1.getDataDefinitionByFinalName(mapping.getSourceData());
        DataDefinition def2= step2.getDataDefinitionByFinalName(mapping.getTargetData());
        return def1==def2;
    }

    @Override
    public void addFormalOutputs(String formalOutput) {
        this.formalOutputsFlowNames.add(formalOutput);
    }

    @Override
    public void buildInputToOutputList() {
            for(StepUsageDescription step:steps) {
                for (DataUsageDescription input : step.getInputs())
                {
                        inputsToOutputs.put(input, null);
                }
            }
    }
    @Override
    public Boolean isDataInFlow(String finalName)
    {
        for(StepUsageDescription step:steps)
        {
             if(step.isDataUsageInStep(finalName))
                 return true;
        }
        return false;
    }
    @Override
    public void getCustomMappingInputToOutput()
    {
        DataUsageDescription input;
        DataUsageDescription output;

        for(CustomMapping mapping:customMappings)
        {
            input=getDataUsageInputMapping(mapping);
            output=getDataUsageOutputMapping(mapping);
            for (Map.Entry<DataUsageDescription, DataUsageDescription> entry : inputsToOutputs.entrySet()) {
                if(entry.getKey()==input)
                    entry.setValue(output);
            }
        }

    }

    @Override
    public void automaticMapping() {
        String prevFinalStepName=steps.get(0).getFinalStepName();
        String currFinalStepName=null;
        StepUsageDescription step;
        for (Map.Entry<DataUsageDescription, DataUsageDescription> entry : inputsToOutputs.entrySet())
        {
            currFinalStepName=entry.getKey().getFinalStepName();
            if(!Objects.equals(currFinalStepName, prevFinalStepName))
            {
                step=this.getStepUsageDescription(prevFinalStepName);
                currentOutputs.addAll(step.getOutputs());
            }
            if(entry.getValue()==null) {
                for (DataUsageDescription output : currentOutputs) {
                    if (Objects.equals(entry.getKey().getFinalName(), output.getFinalName()) &&
                            entry.getKey().getDataDefinition().dataDefinition().getType() == output.getDataDefinition().dataDefinition().getType()) {
                        entry.setValue(output);
                    }
                }
                if (entry.getValue() == null )
                    freeInputsFlow.add(entry.getKey());

            }
            prevFinalStepName=currFinalStepName;
        }
        step=this.getStepUsageDescription(currFinalStepName);
        currentOutputs.addAll(step.getOutputs());
    }
    @Override
    public Boolean isContinuations(String inputName,String flowName)
    {
        for(Continuation continuation:continuations)
        {
            if(Objects.equals(continuation.getTargetFlowName(), flowName))
            {
                for(ContinuationMapping mapping:continuation.getMappings())
                {
                    if(Objects.equals(mapping.getSourceDataName(), inputName))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> getContinuationFlowsNames() {
        List<String> res=new ArrayList<>();
        for(Continuation continuation:this.continuations)
        {
            res.add(continuation.getTargetFlowName());
        }
        return res;
    }

    @Override
    public Boolean isInitialValue(String inputName)
    {
        for(InitialInputValue value:initialList)
        {
            if(Objects.equals(value.getInputName(), inputName))
                return true;
        }
        return false;
    }
    @Override
    public void removeInitialFreeInputs()
    {
        for (int i = 0; i < finalFreeInputs.size(); i++)
        {
            if(isInitialValue(finalFreeInputs.get(i).getFinalName()))
                finalFreeInputs.remove(i);
        }
    }
    @Override
    public void mergeFreeInputs()
    {
        finalFreeInputs.addAll(freeInputsFlow);
        for (int i = 0; i < finalFreeInputs.size(); i++) //delete the free inputs with the same name,kind
        {
            for(int j=i+1;j<finalFreeInputs.size();j++)
            {
                if (Objects.equals(finalFreeInputs.get(i).getFinalName(), finalFreeInputs.get(j).getFinalName()) &&
                        finalFreeInputs.get(i).getDataDefinition().dataDefinition().getType() == finalFreeInputs.get(j).getDataDefinition().dataDefinition().getType())
                {

                    if(finalFreeInputs.get(i).getDataDefinition().necessity()==DataNecessity.MANDATORY)//takes the one that is mandatory
                    {
                        doubleFreeInputsFlow.add(finalFreeInputs.get(j));
                        finalFreeInputs.remove(j);
                    }
                    else
                    {
                        doubleFreeInputsFlow.add(finalFreeInputs.get(i));
                        finalFreeInputs.remove(i);
                    }
                }
            }
        }
        //delete optional free inputs that not user-friendly
        finalFreeInputs.removeIf(data -> data.getDataDefinition().necessity() == DataNecessity.OPTIONAL &&
                !data.getDataDefinition().dataDefinition().isUserFriendly());
        removeInitialFreeInputs();
    }

    public DataUsageDescription getDataUsageOutputMapping(CustomMapping mapping)
    {
        StepUsageDescription step=getStepUsageDescription(mapping.getSourceStep());
        return step.getDataUsageByFinalName(mapping.getSourceData());
    }
    public DataUsageDescription getDataUsageInputMapping(CustomMapping mapping)
    {
        StepUsageDescription step=getStepUsageDescription(mapping.getTargetStep());
        return step.getDataUsageByFinalName(mapping.getTargetData());
    }

    public int findIndexOfStep(String name)
    {
        for (int i = 0; i < steps.size(); i++) {
            if(Objects.equals(steps.get(i).getFinalStepName(), name))
                return i;
        }
        return -1;//
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<StepUsageDescription> getFlowSteps() {
        return steps;
    }

    @Override
    public List<String> getFlowFormalOutputsNames() {
        return formalOutputsFlowNames;
    }

    @Override
    public void addStepToFlow(StepUsageDescription stepToAdd) {
        steps.add(stepToAdd);
    }

}
