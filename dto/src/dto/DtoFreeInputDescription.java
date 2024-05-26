package dto;

import stepper.dd.api.DataDefinition;
import stepper.step.api.DataDefinitionDescription;
import stepper.step.api.DataNecessity;

public class DtoFreeInputDescription
{
    private String userStringAndName;
    private String finalName;
    private DataNecessity necessity;
    private DataDefinitionDescription dataDefinition;

    public DtoFreeInputDescription(String userString, String finalName, DataNecessity necessity, DataDefinitionDescription dataDefinition)
    {
        this.userStringAndName=String.format("%s (%s)",userString,finalName);
        this.finalName=finalName;
        this.necessity=necessity;
        this.dataDefinition=dataDefinition;
    }

    public String getFinalName() {
        return finalName;
    }

    public String getUserStringAndName() {
        return userStringAndName;
    }

    public DataDefinitionDescription getDataDefinition() {
        return dataDefinition;
    }

    public DataNecessity getNecessity() {
        return necessity;
    }


}
