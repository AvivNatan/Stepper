package stepper.flow.excution.context;

import stepper.dd.api.DataDefinition;
import stepper.flow.definition.api.DataUsageDescription;
import stepper.flow.excution.FlowExecution;
import stepper.flow.excution.StepExecutionData;
import stepper.step.api.StepDefinition;
import stepper.step.api.StepResult;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class StepExecutionContextImp implements StepExecutionContext , Serializable
{
    private String finalStepName;
    private FlowExecution flowExecution;

    public StepExecutionContextImp(FlowExecution flowExecution) {
        this.finalStepName =null;
        this.flowExecution=flowExecution;
    }


    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType, StepDefinition stepDefinition) {
        // assuming that from the data name we can get to its data definition
        Object aValue=null;
        DataDefinition theExpectedDataDefinition=stepDefinition.getDataDefinitionByNameOfData(dataName);
        String finalDataName=flowExecution.getFlowDefinition().getStepUsageDescription(finalStepName).
                getDataUsageDescriptionByOriginalName(dataName).getFinalName();
        DataUsageDescription optionalData=flowExecution.getFlowDefinition().getInputToOutputValue(finalDataName);

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType()))
        {
            if(flowExecution.getDataValues().get(finalDataName)!=null)
            {
                aValue = flowExecution.getDataValues().get(finalDataName).getContent();
            }
            else
            {
                if (optionalData == null)
                    return null;

                if (flowExecution.getDataValues().get(optionalData.getFinalName()) != null)
                    aValue = flowExecution.getDataValues().get(optionalData.getFinalName()).getContent();


            }
            return expectedDataType.cast(aValue);
        }
        return null;
    }

    @Override
    public boolean storeDataValue(String originalName, DataInFlow value)
    {
        // assuming that from the data name we can get to its data definition
        DataDefinition theData = value.getDataUsageDefinition().getDataDefinition().dataDefinition();

        // we have the DD type so we can make sure that its from the same type
        if (theData.getType().isAssignableFrom(value.getContent().getClass()))
        {
            flowExecution.getDataValues().put(value.getDataUsageDefinition().getFinalName(),value);
            return true;
        }
        return false;
    }

    @Override
    public List<StepsLog> getStepLogs() {
        return getStepDataByNameOfStep().getStepLogs();
    }

    @Override
    public String getStepSummeryLine() {
        return getStepDataByNameOfStep().getStepSummeryLine();
    }

    @Override
    public void addLog(StepsLog log)
    {
        this.getStepDataByNameOfStep().addLogData(log);
    }
    @Override
    public void addSummeryLine(String summeryLine)
    {
        this.getStepDataByNameOfStep().setStepSummeryLine(summeryLine);
    }
    @Override
    public void addResult(StepResult result)
    {
        this.getStepDataByNameOfStep().setResult(result);
    }
    @Override
    public void updateDurationStep(long startTime)
    {
         long endTime=System.currentTimeMillis();
         this.getStepDataByNameOfStep().setStepTotalTime(Duration.ofMillis(endTime-startTime));
    }
    @Override
    public StepExecutionData getStepDataByNameOfStep() {
        for(StepExecutionData stepData: flowExecution.getStepsData())
        {
            if(Objects.equals(finalStepName, stepData.getFinalName()))
                return stepData;
        }
        return null;
    }

    @Override
    public FlowExecution getFlowExecution() {
        return flowExecution;
    }

    @Override
    public String getFinalStepName() {
        return finalStepName;
    }

    @Override
    public void setFinalStepName(String finalStepName) {
        this.finalStepName = finalStepName;
    }


}
