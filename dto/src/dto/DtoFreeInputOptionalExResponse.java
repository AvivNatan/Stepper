package dto;

import stepper.step.api.DataNecessity;

import java.util.UUID;

public class DtoFreeInputOptionalExResponse
{
    private String finalName;
    private DataNecessity necessity;

    public DtoFreeInputOptionalExResponse(String finalName, DataNecessity necessity) {
        this.finalName = finalName;
        this.necessity = necessity;
    }

    public String getFinalName() {
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public DataNecessity getNecessity() {
        return necessity;
    }

    public void setNecessity(DataNecessity necessity) {
        this.necessity = necessity;
    }
}
