package dto;

import stepper.flow.excution.context.StepsLog;
import stepper.step.api.StepResult;

import java.time.Duration;
import java.util.List;

public class DtoStepExecution
{
    String finalName;
    Duration totalTime;
    StepResult stepResult;
    String summeryLine;
    List<StepsLog> logs;

    public DtoStepExecution(String finalName, Duration totalTime, StepResult stepResult, String summeryLine, List<StepsLog> logs) {
        this.finalName = finalName;
        this.totalTime = totalTime;
        this.stepResult = stepResult;
        this.summeryLine = summeryLine;
        this.logs = logs;
    }


    public String getFinalName() {
        return finalName;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public StepResult getStepResult() {
        return stepResult;
    }

    public String getSummeryLine() {
        return summeryLine;
    }

    public List<StepsLog> getLogs() {
        return logs;
    }

}
