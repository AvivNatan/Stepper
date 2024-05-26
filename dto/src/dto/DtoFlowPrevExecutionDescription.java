package dto;

import stepper.flow.excution.FlowExecutionResult;
import stepper.step.api.DataNecessity;

import java.time.Duration;
import java.util.*;

public class DtoFlowPrevExecutionDescription {
    private final UUID uniqueId;
    private final String userName;
    private final String role;
    private final String name;
    private final String startTime;
    private final FlowExecutionResult resultExecution;
    private final Duration totalTime;
    private final List<DtoFreeInputOutputExecution> freeInputs;
    private final List<DtoFreeInputOutputExecution> outputs;
    private final List<DtoStepExecution> Steps;
    private final List<String> continuationDetails;

    public DtoFlowPrevExecutionDescription(UUID uniqueId,String userName,String role, String name,String startTime, FlowExecutionResult resultExecution, Duration totalTime,List<String> continuationDetails) {
        this.name = name;
        this.userName=userName;
        this.role=role;
        this.uniqueId = uniqueId;
        this.startTime=startTime;
        this.resultExecution = resultExecution;
        this.totalTime = totalTime;
        this.freeInputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.Steps=new ArrayList<>();
        this.continuationDetails=continuationDetails;
    }

    public List<String> getContinuationDetails() {
        return continuationDetails;
    }

    public List<DtoStepExecution> getSteps() {
        return Steps;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    public String getStartTime() {
        return startTime;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public FlowExecutionResult getResultExecution() {
        return resultExecution;
    }

    public List<DtoFreeInputOutputExecution> getFreeInputs() {
        return freeInputs;
    }

    public List<DtoFreeInputOutputExecution> getOutputs() {
        return outputs;
    }

    public void addFreeInputExecution(DtoFreeInputOutputExecution freeInputExecution) {
        if(freeInputExecution.getNecessity()== DataNecessity.MANDATORY)
            this.freeInputs.add(0,freeInputExecution);
        else
            this.freeInputs.add(freeInputExecution);
    }
    public void addOutputExecution(DtoFreeInputOutputExecution outputExecution) {
            this.outputs.add(outputExecution);
    }
    public void addStepExecution(DtoStepExecution step)
    {
        this.Steps.add(step);
    }
}

