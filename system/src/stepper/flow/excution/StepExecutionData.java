package stepper.flow.excution;

import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.StepDefinition;
import stepper.step.api.StepResult;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StepExecutionData implements Serializable
{
    private String finalName;
    private StepDefinition stepDefinition;
    private Duration StepTotalTime;
    private String StepStartTime;
    private String StepEndTime;
    private StepResult Result;
    private List<StepsLog> StepLogs;
    private String StepSummeryLine;
    private StepUsageDescription usage;

    public StepExecutionData(String name,StepDefinition stepDefinition, StepUsageDescription usage) {
        finalName = name;
        this.stepDefinition=stepDefinition;
        StepLogs = new ArrayList<>();
        this.StepStartTime= setStartTimeFlow(LocalTime.now());
        this.usage=usage;
    }

    public StepUsageDescription getUsage() {
        return usage;
    }

    public void setUsage(StepUsageDescription usage) {
        this.usage = usage;
    }

    public String setStartTimeFlow(LocalTime startTime)
    {
        // Format the time in HH:MM:SS format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return startTime.format(formatter);
    }

    public String getStepStartTime() {
        return StepStartTime;
    }

    public String getStepEndTime() {
        return StepEndTime;
    }

    public void setStepEndTime(String stepEndTime) {
        StepEndTime = stepEndTime;
    }

    public String getFinalName() {
        return finalName;
    }

    public StepResult getResult() {
        return Result;
    }

    public Duration getStepTotalTime() {
        return StepTotalTime;
    }

    public List<StepsLog> getStepLogs() {
        return StepLogs;
    }

    public String getStepSummeryLine() {
        return StepSummeryLine;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public void setStepTotalTime(Duration stepTotalTime) {
        StepTotalTime = stepTotalTime;
    }

    public void setStepSummeryLine(String stepSummeryLine) {
        StepSummeryLine = stepSummeryLine;
    }

    public void setResult(StepResult result) {
        Result = result;
    }

    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }

    public void addLogData(StepsLog log)
    {
        this.StepLogs.add(log);
    }
}
