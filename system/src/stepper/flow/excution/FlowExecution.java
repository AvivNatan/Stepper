package stepper.flow.excution;

import stepper.dd.imp.EnumeratorDef.EnumeratorData;
import stepper.flow.definition.api.*;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.DataNecessity;
import stepper.xmlexceptions.NumberFormatException;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FlowExecution implements Serializable
{
    private final UUID uniqueId;
    private final String userName;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private String startTime;
    private FlowExecutionResult flowExecutionResult;
    private List<String> allStepsSummeryLines;
    private List<List<StepsLog>> allStepsLogs;
    private Map<String, DataInFlow> freeInputs;
    private List<String> formalOutputs;
    private final Map<String, DataInFlow> dataValues;
    private List<StepExecutionData> stepsData;
    private FlowExecution optionalContinuationSourceExecution;
    private Boolean isFlowExecuteNow=false;



    // lots more data that needed to be stored while flow is being executed...
    public FlowExecution(FlowDefinition flowDefinition,FlowExecution optional,String userName) {

        this.optionalContinuationSourceExecution =optional;
        this.userName=userName;
        this.uniqueId = UUID.randomUUID();
        this.flowDefinition = flowDefinition;
        this.totalTime=Duration.ZERO;
        this.allStepsSummeryLines=new ArrayList<>();
        this.allStepsLogs=new ArrayList<>();
        this.startTime= setStartTimeFlow(LocalTime.now());
        this.freeInputs=new LinkedHashMap<>();
        initFreeInputFromStart();
        this.formalOutputs=new ArrayList<>();
        initFormalOutputs();
        this.dataValues=new LinkedHashMap<>();
        this.stepsData=new ArrayList<>();
        setInitialInputValues();
        setContinuationValues();
    }
    public void copyFreeInputs(FlowExecution execution)
    {
        for(Map.Entry<String, DataInFlow> entry:execution.freeInputs.entrySet())
        {
            freeInputs.get(entry.getKey()).setContent(entry.getValue().getContent());
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setFlowExecuteNow(Boolean flowExecuteNow) {
        isFlowExecuteNow = flowExecuteNow;
    }

    public FlowExecution getOptionalContinuationSourceExecution() {
        return optionalContinuationSourceExecution;
    }


    public void addStepExecutionData(StepExecutionData stepData)
    {
        stepsData.add(stepData);
    }
    public void initFreeInputFromStart() {
        for (DataUsageDescription data : flowDefinition.getFinalFreeInputs())
        {
            if(data.getDataDefinition()
                    .dataDefinition().getType().getSimpleName().equals("EnumeratorData"))
            {
                freeInputs.put(data.getFinalName(),new DataInFlowImp(data,new EnumeratorData((Set<String>)data.getDataDefinition().getMoreInfoOfData())));
            }
            else
              freeInputs.put(data.getFinalName(),new DataInFlowImp(data));
        }
    }
    public void initFormalOutputs()
    {
        formalOutputs.addAll(flowDefinition.getFlowFormalOutputsNames());
    }
    public void updateFreeInput(String finalName,String value)
    {
        String type=freeInputs.get(finalName).getDataUsageDefinition().getDataDefinition().dataDefinition().getType().getSimpleName();
        if(type.equals("EnumeratorData"))
        {
            if(freeInputs.get(finalName).getContent()!=null)
                ((EnumeratorData)freeInputs.get(finalName).getContent()).setChosenValue((String) value);
        }
        else if(type.equals("Integer"))
        {
            if(freeInputs.get(finalName)!=null)
            {
                try {

                    freeInputs.get(finalName).setContent(Integer.parseInt(value));
                }
                catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else
        {
            if(freeInputs.get(finalName)!=null)
                freeInputs.get(finalName).setContent(value);
        }

    }
    public boolean isAllMandatoryInputsReceived()
    {
        for(DataInFlow data:freeInputs.values())
        {
            if(data.getDataUsageDefinition().getDataDefinition().necessity()==DataNecessity.MANDATORY)
            {
                if(!data.getDataUsageDefinition().getDataDefinition().dataDefinition().getType().getSimpleName().equals("EnumeratorData"))
                {
                    if(data.getContent()==null)
                        return false;
                }
                else
                {
                    if(((EnumeratorData)data.getContent()).getChosenValue()==null)
                        return false;
                }

            }
        }
        return true;
    }



    public void setInitialInputValues()
    {
        if(this.flowDefinition.getInitialList()!=null)
        {
            for(InitialInputValue value: this.flowDefinition.getInitialList())
            {
                DataUsageDescription usage=getDataUsageByFinalName(value.getInputName());
                if(usage.getDataDefinition().dataDefinition().getType().getSimpleName().equals("EnumeratorData"))
                {
                    EnumeratorData data=new EnumeratorData((Set<String>)usage.getDataDefinition().getMoreInfoOfData());
                    data.setChosenValue(value.getInitialValue());
                    this.dataValues.put(value.getInputName(),new DataInFlowImp(usage,data));

                }
                else if(usage.getDataDefinition().dataDefinition().getType().getSimpleName().equals("Integer"))
                    this.dataValues.put(value.getInputName(),new DataInFlowImp(usage, Integer.parseInt(value.getInitialValue())));
                else if (usage.getDataDefinition().dataDefinition().getType().getSimpleName().equals("Double"))
                    this.dataValues.put(value.getInputName(),new DataInFlowImp(usage, Double.parseDouble(value.getInitialValue())));
                else
                    this.dataValues.put(value.getInputName(),new DataInFlowImp(usage,value.getInitialValue()));
            }
        }
    }

    public DataUsageDescription getDataUsageByFinalName(String data)
    {
        for(DataUsageDescription usage:this.flowDefinition.getAllInputs())
        {
            if(Objects.equals(data, usage.getFinalName()))
                return usage;
        }
        return null;
    }

    public void setContinuationValues()
    {
        if(optionalContinuationSourceExecution !=null)
        {
            for(Continuation continuation: optionalContinuationSourceExecution.getFlowDefinition().getContinuations())
            {
                if(Objects.equals(continuation.getTargetFlowName(), this.flowDefinition.getName()))
                {
                    setContinuationMapping(continuation);
                    setContinuationAutomatic();
                }
            }
        }
    }

    public void setContinuationMapping(Continuation continuation)
    {
        if(continuation.getMappings().size()!=0)
        {
            for (ContinuationMapping mapping : continuation.getMappings()) {
                DataInFlow data = optionalContinuationSourceExecution.getDataValues().get(mapping.getSourceDataName());
                if (data != null) {
                        freeInputs.get(mapping.getTargetDataName()).setContent(data.getContent());
                }
            }
        }
    }
    public void setContinuationAutomatic()
    {
        for (Map.Entry<String, DataInFlow> entry : freeInputs.entrySet())
        {
                DataInFlow dataInFlowOptional = optionalContinuationSourceExecution.dataValues.get(entry.getKey());
                if (dataInFlowOptional != null)
                {
                    if (dataInFlowOptional.getContent() != null && dataInFlowOptional.getDataUsageDefinition().
                            getDataDefinition().dataDefinition().getType().getSimpleName().
                            equals(entry.getValue().getDataUsageDefinition().getDataDefinition().dataDefinition().getType().getSimpleName())) {
                        entry.getValue().setContent(dataInFlowOptional.getContent());
                    }
                }
            }
        }


    public List<StepExecutionData> getStepsData() {
        return stepsData;
    }

    public Map<String, DataInFlow> getDataValues() {
        return dataValues;
    }
    public String getStartTime() {
        return startTime;
    }

    public String setStartTimeFlow(LocalTime startTime)
    {
        // Format the time in HH:MM:SS format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return startTime.format(formatter);
    }
    public void setStepExecutionEndTime(String finalStepName)
    {
        for(StepExecutionData step:this.stepsData)
        {
            if(Objects.equals(step.getFinalName(), finalStepName))
                step.setStepEndTime(setStartTimeFlow(LocalTime.now()));
        }
    }
    public StepExecutionData findStepExecutionData(String finalStepName)
    {
        for(StepExecutionData step:this.stepsData)
        {
            if(Objects.equals(step.getFinalName(), finalStepName))
                return step;
        }
        return null;
    }

    public void setFlowExecutionResult(FlowExecutionResult result)
    {
        this.flowExecutionResult=result;
    }

    public void setTotalTime(long startTime) {
            long endTime=System.currentTimeMillis();
            this.totalTime=Duration.ofMillis(endTime-startTime);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    public FlowExecutionResult getFlowExecutionResult() {
        return flowExecutionResult;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public void addSummeryLineOfStep(String summeryLine)
    {
        this.allStepsSummeryLines.add(summeryLine);
    }
    public void addStepLogs(List<StepsLog> StepLogs)
    {
            this.allStepsLogs.add(StepLogs);
    }

    public List<String> getFormalOutputs() {
        return formalOutputs;
    }

    public Map<String, DataInFlow> getFreeInputs() {
        return freeInputs;
    }


}
