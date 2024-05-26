package stepper.flow.definition.api;

import stepper.step.api.DataDefinitionDescription;

import java.io.Serializable;

public class DataUsageDescriptionImp implements DataUsageDescription, Serializable
{
    private String finalName;
    private DataKind kind;
    private DataDefinitionDescription dataDefinition;
    private String finalStepName;

    public DataUsageDescriptionImp(DataKind kind, DataDefinitionDescription dataDefinition,String finalStepName) {
        this(dataDefinition.getOriginalDataName(),kind,dataDefinition,finalStepName);
    }

    public DataUsageDescriptionImp(String finalName, DataKind kind, DataDefinitionDescription dataDefinition,String finalStepName) {
        this.finalName = finalName;
        this.kind = kind;
        this.dataDefinition = dataDefinition;
        this.finalStepName=finalStepName;
    }




    @Override
    public String getFinalName() {
        return finalName;
    }

    @Override
    public DataDefinitionDescription getDataDefinition() {
        return dataDefinition;
    }

    @Override
    public boolean isInput() {
        return kind == DataKind.INPUT;
    }

    @Override
    public boolean isOutput() {
        return kind == DataKind.OUTPUT;
    }

    @Override
    public String getOriginalName() {
        return dataDefinition.getOriginalDataName();
    }

    @Override
    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    @Override
    public String getFinalStepName() {
        return finalStepName;
    }
}
