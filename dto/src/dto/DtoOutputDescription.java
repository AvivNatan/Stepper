package dto;

import stepper.dd.api.DataDefinition;

public class DtoOutputDescription {
    private String finalName;
    private String type;
    private String nameStep;

    public DtoOutputDescription(String finalName, String type, String nameStep)
    {
        this.finalName=finalName;
        this.nameStep=nameStep;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public String getFinalName() {
        return finalName;
    }

    public String getNameStep() {
        return nameStep;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNameStep(String nameStep) {
        this.nameStep = nameStep;
    }
}
