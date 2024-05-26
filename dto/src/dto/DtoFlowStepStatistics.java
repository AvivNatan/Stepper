package dto;

import java.util.HashMap;
import java.util.Map;

public class DtoFlowStepStatistics
{
    private String name;
    private int executionNum;
    private long avg;

    public DtoFlowStepStatistics(String name, int executionNum, long avg) {
        this.name = name;
        this.executionNum = executionNum;
        this.avg = avg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExecutionNum() {
        return executionNum;
    }

    public void setExecutionNum(int executionNum) {
        this.executionNum = executionNum;
    }

    public long getAvg() {
        return avg;
    }

    public void setAvg(long avg) {
        this.avg = avg;
    }

}
