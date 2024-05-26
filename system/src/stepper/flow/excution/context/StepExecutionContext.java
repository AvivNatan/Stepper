package stepper.flow.excution.context;

import stepper.flow.excution.FlowExecution;
import stepper.flow.excution.StepExecutionData;
import stepper.step.api.StepDefinition;
import stepper.step.api.StepResult;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

public interface StepExecutionContext extends Serializable {
    <T> T getDataValue(String dataName, Class<T> expectedDataType, StepDefinition stepDefinition);
    boolean storeDataValue(String originalName, DataInFlow value);
    List<StepsLog> getStepLogs();
    String getStepSummeryLine();
    void addLog(StepsLog log);
    void addSummeryLine(String summeryLine);
    void updateDurationStep(long startTime);
     StepExecutionData getStepDataByNameOfStep();
     FlowExecution getFlowExecution();
     String getFinalStepName();
     void setFinalStepName(String finalStepName);
     void addResult(StepResult result);
}
