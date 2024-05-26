package stepper.step.api;

import stepper.dd.api.DataDefinition;
import stepper.flow.definition.api.DataKind;
import stepper.flow.excution.context.StepExecutionContext;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface StepDefinition extends Serializable {

    String originalStepName();
    boolean isReadonly();
    List<stepper.step.api.DataDefinitionDescription> inputs();
    List<stepper.step.api.DataDefinitionDescription> outputs();
    DataDefinition getDataDefinitionByNameOfData(String NameData);
    DataKind isInputOrOutput(String originalName);

    StepResult invoke(StepExecutionContext context) throws IOException;
}
