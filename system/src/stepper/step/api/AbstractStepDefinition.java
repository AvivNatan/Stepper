package stepper.step.api;

import stepper.dd.api.DataDefinition;
import stepper.flow.definition.api.DataKind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStepDefinition implements StepDefinition , Serializable
{

    private final String originalStepName;
    private final boolean readonly;
    private final List<stepper.step.api.DataDefinitionDescription> inputs;
    private final List<stepper.step.api.DataDefinitionDescription> outputs;


    public AbstractStepDefinition(String stepName, boolean readonly) {
        this.originalStepName = stepName;
        this.readonly = readonly;
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }
    protected void addInput(stepper.step.api.DataDefinitionDescription data) {
        inputs.add(data);
    }

    protected void addOutput(stepper.step.api.DataDefinitionDescription data) {
        outputs.add(data);
    }
    @Override
    public String originalStepName() {
        return originalStepName;
    }

    @Override
    public boolean isReadonly() {
        return readonly;
    }

    @Override
    public List<stepper.step.api.DataDefinitionDescription> inputs() {
        return inputs;
    }

    @Override
    public List<stepper.step.api.DataDefinitionDescription> outputs() {
        return outputs;
    }

    @Override
    public DataDefinition getDataDefinitionByNameOfData(String NameData) {
        for(stepper.step.api.DataDefinitionDescription input : inputs)
            if(input.getOriginalDataName().equals(NameData))
                return input.dataDefinition();
        return null;
    }
    @Override
    public DataKind isInputOrOutput(String originalName)
    {
        for(stepper.step.api.DataDefinitionDescription data:inputs)
            if(data.getOriginalDataName()==originalName)
                return DataKind.INPUT;
        for(stepper.step.api.DataDefinitionDescription data:outputs)
            if(data.getOriginalDataName()==originalName)
                return DataKind.OUTPUT;

        return null;
    }

}
