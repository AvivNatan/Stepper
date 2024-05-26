package dto;

import java.util.ArrayList;
import java.util.List;

public class DtoFlowsExecutionHistoryResponse
{
    List<DtoFlowExecutionHistory> list;

    public DtoFlowsExecutionHistoryResponse() {
        this.list = new ArrayList<>();

    }

    public List<DtoFlowExecutionHistory> getList() {
        return list;
    }

    public void setList(List<DtoFlowExecutionHistory> list) {
        this.list = list;
    }
}
