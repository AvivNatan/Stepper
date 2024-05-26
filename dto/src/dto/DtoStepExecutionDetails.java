package dto;

import stepper.flow.excution.context.StepsLog;
import stepper.step.api.StepResult;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DtoStepExecutionDetails
{
    private String finalName;
    private StepResult result;
    private String startTime;
    private String endTime;
    private Duration totalTime;
    private List<DtoInputOutputForStepDetails> inputs;
    private List<DtoInputOutputForStepDetails> outputs;
    private List<StepsLog> logs;

    public DtoStepExecutionDetails(String finalName, StepResult result, String startTime, String endTime, Duration totalTime,List<StepsLog> logs) {
        this.finalName = finalName;
        this.result = result;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.logs.addAll(logs);
    }

    public String getFinalName() {
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public StepResult getResult() {
        return result;
    }

    public void setResult(StepResult result) {
        this.result = result;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Duration totalTime) {
        this.totalTime = totalTime;
    }

    public List<DtoInputOutputForStepDetails> getInputs() {
        return inputs;
    }

    public void addInput(DtoInputOutputForStepDetails input) {
        this.inputs.add(input);
    }

    public List<DtoInputOutputForStepDetails> getOutputs() {
        return outputs;
    }

    public void addOutput(DtoInputOutputForStepDetails output) {
        this.outputs.add(output);
    }

    public List<StepsLog> getLogs() {
        return logs;
    }

    public void addLog(StepsLog log) {
        this.logs.add(log);
    }
}
