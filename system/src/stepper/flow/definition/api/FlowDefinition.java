package stepper.flow.definition.api;

import java.io.Serializable;
import java.util.List;

public interface FlowDefinition extends Serializable {
    String getName();

    String getDescription();

    List<StepUsageDescription> getFlowSteps();

    List<String> getFlowFormalOutputsNames();

    void addStepToFlow(StepUsageDescription stepToAdd);


    boolean isFlowReadOnly();

    void setReadOnly();
     void checkDuplicatesOutputsNames();
    public List<DataUsageDescription> getAllInputs();
    DataUsageDescription getFormalOutput(String formalOutputName);
    StepUsageDescription getStepUsageDescription(String finalName);
     List<CustomMapping> getCustomMappings();
     boolean isStepExist(String finalStepName);
     void addCustomMapping(CustomMapping mapping);
     void addInitialInputValues(InitialInputValue initialInputValues);
    public List<InitialInputValue> getInitialList();
     List<Continuation> getContinuations();
     Boolean isDataInFlow(String finalName);
     void addContinuation(Continuation continuation);
    boolean isPrevStepBeforeNextStep(String prev,String next);
     boolean isOutputStepMatchToInputStep(CustomMapping mapping);
     void addFormalOutputs(String formalOutput);
     List<DataUsageDescription> getListFinalNamesOfOutputs();
     void buildInputToOutputList();
     Boolean isInitialValue(String inputName);
     void getCustomMappingInputToOutput();
     void automaticMapping();
     void mergeFreeInputs();
     void removeInitialFreeInputs();
     List<DataUsageDescription> getFinalFreeInputs();
     void checkMandatoryInputsUserFriendly();

     boolean checkFormalOutputExist(String formalOutputName);
     void checkFormalOutputsExist();
     void checkTypeOfDuplicatesInputs();
     DataUsageDescription getInputToOutputValue(String finalInputName);
     DataUsageDescription getOutputToInputValue(String finalOutputName);
     DataUsageDescription isDoubleFreeInput(String finalName);
     Boolean isContinuations(String inputName,String flowName);
     List<String> getContinuationFlowsNames();



}
