package dto;

public class DtoStepExecutionDetailsResponse
{
    DtoStepExecutionDetails dto;
    boolean isFlowDetailsSelected;

    public DtoStepExecutionDetailsResponse(DtoStepExecutionDetails dto, boolean isSelectedTheSame) {
        this.dto = dto;
        this.isFlowDetailsSelected = isSelectedTheSame;
    }

    public DtoStepExecutionDetails getDto() {
        return dto;
    }

    public void setDto(DtoStepExecutionDetails dto) {
        this.dto = dto;
    }

    public boolean isFlowDetailsSelected() {
        return isFlowDetailsSelected;
    }

    public void setFlowDetailsSelected(boolean flowDetailsSelected) {
        isFlowDetailsSelected = flowDetailsSelected;
    }
}
