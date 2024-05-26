package dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoStatistics
{
    private List<DtoFlowStepStatistics> allFlowsStatistics;
    private List<DtoFlowStepStatistics> allStepsStatistics;

    public DtoStatistics() {
        this.allFlowsStatistics = new ArrayList<>();
        this.allStepsStatistics=new ArrayList<>();
    }
    public void addFlowStatistics(DtoFlowStepStatistics dto)
    {
        allFlowsStatistics.add(dto);
    }
    public void addStepStatistics(DtoFlowStepStatistics dto)
    {
        allStepsStatistics.add(dto);
    }

    public List<DtoFlowStepStatistics> getAllFlowsStatistics() {
        return allFlowsStatistics;
    }
    public boolean isEmpty()
    {
        if(allFlowsStatistics.size()==0)
            return true;
        else
            return false;
    }



    public List<DtoFlowStepStatistics> getAllStepsStatistics() {
        return allStepsStatistics;
    }
    public Map<String,Integer> getMapOfStepsStatistics()
    {
        Map<String,Integer> res=new HashMap<>();
        for(DtoFlowStepStatistics item:allStepsStatistics)
        {
            res.put(item.getName(), item.getExecutionNum());
        }
        return res;
    }
    public Map<String,Integer> getMapOfFlowsStatistics()
    {
        Map<String,Integer> res=new HashMap<>();
        for(DtoFlowStepStatistics item:allFlowsStatistics)
        {
            res.put(item.getName(), item.getExecutionNum());
        }
        return res;
    }

}
