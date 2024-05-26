package stepper.flow.definition.api;

public class ContinuationMapping
{
    private String sourceDataName;
    private String targetDataName;

    public ContinuationMapping(String sourceDataName, String targetDataName) {
        this.sourceDataName = sourceDataName;
        this.targetDataName = targetDataName;
    }

    public String getSourceDataName() {
        return sourceDataName;
    }

    public void setSourceDataName(String sourceDataName) {
        this.sourceDataName = sourceDataName;
    }

    public String getTargetDataName() {
        return targetDataName;
    }

    public void setTargetDataName(String targetDataName) {
        this.targetDataName = targetDataName;
    }
}
