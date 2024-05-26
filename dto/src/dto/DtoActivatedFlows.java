package dto;

import java.util.ArrayList;
import java.util.List;

public class DtoActivatedFlows
{
    private List<DtoActivatedFlowDescription> activatedFlows;
    public DtoActivatedFlows()
    {
        this.activatedFlows=new ArrayList<>();
    }

    public List<DtoActivatedFlowDescription> getActivatedFlows() {
        return activatedFlows;
    }

    public void setActivatedFlows(List<DtoActivatedFlowDescription> activatedFlows) {
        this.activatedFlows = activatedFlows;
    }
    public void addActivatedFlow(DtoActivatedFlowDescription flow)
    {
        this.activatedFlows.add(flow);
    }
}
