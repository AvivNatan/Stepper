package stepper.flow.definition.api;

import stepper.dd.api.DataDefinition;
import stepper.step.api.StepDefinition;

import java.io.Serializable;
import java.util.List;

public interface StepUsageDescription extends Serializable {
    String getFinalStepName();
    StepDefinition getStepDefinition();
    boolean skipIfFail();
    List<DataUsageDescription> getDataDefinitionFlow();
    DataUsageDescription getDataUsageDescriptionByOriginalName(String originalName);
    DataDefinition getDataDefinitionByFinalName(String finalName);
    void addDataUsage(DataUsageDescription data);
     boolean isDataUsageInStep(String finalName);
    List<DataUsageDescription> getInputs();
    List<DataUsageDescription> getOutputs();
     DataUsageDescription getDataUsageByFinalName(String finalName);

}
