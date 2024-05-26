package dto;

public class DtoFlowPrevExecutionDescriptionResponse {
    DtoFlowPrevExecutionDescription dto;
    boolean res;
    boolean reExecute;

    public DtoFlowPrevExecutionDescriptionResponse(DtoFlowPrevExecutionDescription dto,
                                                   boolean res,boolean reExecute) {
        this.dto = dto;
        this.res = res;
        this.reExecute=reExecute;
    }

    public boolean isReExecute() {
        return reExecute;
    }

    public DtoFlowPrevExecutionDescription getDto() {
        return dto;
    }

    public void setDto(DtoFlowPrevExecutionDescription dto) {
        this.dto = dto;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }
}
