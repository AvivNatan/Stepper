package dto;

import java.util.ArrayList;
import java.util.List;

public class DtoFreeInputsOptionalExResponse
{
    List<DtoFreeInputOptionalExResponse> list;

    public DtoFreeInputsOptionalExResponse() {
        this.list = new ArrayList<>();
    }

    public List<DtoFreeInputOptionalExResponse> getList() {
        return list;
    }

    public void setList(List<DtoFreeInputOptionalExResponse> list) {
        this.list = list;
    }
}
