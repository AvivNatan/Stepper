package stepper.flow.definition.api;

import java.io.Serializable;

public class CustomMapping implements Serializable
{
    private String sourceStep;
    private String sourceData;
    private String targetStep;
    private String targetData;

    public CustomMapping(String sourceStep, String sourceData, String targetStep, String targetData) {
        this.sourceStep = sourceStep;
        this.sourceData = sourceData;
        this.targetStep = targetStep;
        this.targetData = targetData;
    }

    public String getSourceStep() {
        return sourceStep;
    }

    public String getSourceData() {
        return sourceData;
    }

    public String getTargetStep() {
        return targetStep;
    }

    public String getTargetData() {
        return targetData;
    }
}
