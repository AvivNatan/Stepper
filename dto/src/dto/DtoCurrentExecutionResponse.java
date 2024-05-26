package dto;

import stepper.flow.excution.FlowExecutionResult;

public class DtoCurrentExecutionResponse
{
    private FlowExecutionResult result;
    private boolean isResult;

    public DtoCurrentExecutionResponse(FlowExecutionResult result, boolean isResult) {
        this.result = result;
        this.isResult = isResult;
    }
    public FlowExecutionResult getResult() {
        return result;
    }
    public void setResult(FlowExecutionResult result)
    {
        this.result = result;
    }
    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }
}
