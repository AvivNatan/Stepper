package dto;

import java.util.UUID;

public class DtoFlowExecutionHistory
{
    private String flowName;
    private String startTime;
    private String result;
    private UUID id;

    public DtoFlowExecutionHistory(String flowName, String startTime, String result,UUID id) {
        this.flowName = flowName;
        this.startTime = startTime;
        this.result = result;
        this.id=id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
