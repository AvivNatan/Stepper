package dto;

import java.util.ArrayList;
import java.util.List;

public class DtoFlowsDescriptions
{
    private List<DtoFlowDescription> Flows;

    public DtoFlowsDescriptions() {
        this.Flows = new ArrayList<>();
    }

    public List<DtoFlowDescription> getFlows() {
        return Flows;
    }

    public void setFlows(List<DtoFlowDescription> flows) {
        Flows = flows;
    }
}
