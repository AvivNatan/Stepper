package dto;

import stepper.dd.api.DataDefinition;
import stepper.step.api.DataNecessity;

public class DtoFreeInputOutputExecution
{
    String finalName;
    String type;
    String stepRelated;
    Object content;
    DataNecessity necessity;
    public DtoFreeInputOutputExecution(String finalName,String stepRelated, String type, Object content, DataNecessity necessity)
    {
        this.finalName=finalName;
        this.stepRelated=stepRelated;
        this.type=type;
        this.content=content;
        this.necessity=necessity;
    }

    public String getStepRelated() {
        return stepRelated;
    }

    public DataNecessity getNecessity() {
        return necessity;
    }

    public String getFinalName() {
        return finalName;
    }

    public String getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }
}
