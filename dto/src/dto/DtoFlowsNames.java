package dto;

import java.util.ArrayList;
import java.util.List;

public class DtoFlowsNames
{
    List<String> flowsNames;
    public DtoFlowsNames()
    {
        this.flowsNames=new ArrayList<>();
    }

    public void setFlowsNames(List<String> flowsNames) {
        this.flowsNames = flowsNames;
    }

    public List<String> getFlowsNames() {
        return flowsNames;
    }
}
