package stepper.flow.definition.api;

import stepper.step.api.DataDefinitionDescription;

import java.io.Serializable;

public interface DataUsageDescription extends Serializable
{
     String getFinalName();
     DataDefinitionDescription getDataDefinition();
     boolean isInput();
     boolean isOutput();
     String getOriginalName();
     void setFinalName(String finalName);
     String getFinalStepName();


}
