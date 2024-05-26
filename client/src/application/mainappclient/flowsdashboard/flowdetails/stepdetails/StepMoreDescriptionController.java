package application.mainappclient.flowsdashboard.flowdetails.stepdetails;

import application.mainappclient.flowsdashboard.flowdetails.FlowDetailsController;
import application.mainappclient.flowsdashboard.flowdetails.stepdetails.inputstepdetails.InputStepDescriptionController;
import application.mainappclient.flowsdashboard.flowdetails.stepdetails.outputstepdetails.OutputStepDescriptionController;
import dto.DtoDataMoreDescription;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class StepMoreDescriptionController {
    private FlowDetailsController flowDetailsController;

    @FXML
    private Accordion allStepsInputs;

    @FXML
    private Accordion allStepsOutputs;

    @FXML
    private Text stepName;

    public FlowDetailsController getFlowDetailsController() {
        return flowDetailsController;
    }

    public void setFlowDetailsController(FlowDetailsController flowDetailsController) {
        this.flowDetailsController = flowDetailsController;
    }

    public void setStepName(String stepName) {
        this.stepName.setText(stepName);
    }

    public void setInputStepDescription(DtoDataMoreDescription dto, InputStepDescriptionController controller)
    {
        controller.setNecessity(dto.getNecessity().toString());
        controller.setInputConnected(dto.isConnected(),dto.getDataConnectedName(),dto.getStepConnectedName());

    }
    void RemoveAllTitlesFromAccordionInputsStep()
    {
        allStepsInputs.getPanes().remove(0,allStepsInputs.getPanes().size());
    }
    public void addAllInputsStep(List<DtoDataMoreDescription> list)
    {
        RemoveAllTitlesFromAccordionInputsStep();
        for(DtoDataMoreDescription dto:list)
        {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowsdashboard/flowdetails/stepdetails/inputstepdetails/InputStepDescription.fxml"));
                AnchorPane res=loader.load();
                InputStepDescriptionController controller=loader.getController();
                controller.setStepMoreDescriptionController(this);
                setInputStepDescription(dto,controller);
                TitledPane titledPane=new TitledPane(dto.getDataFinalName(),res);
                allStepsInputs.getPanes().add(titledPane);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setOutputStepDescription(DtoDataMoreDescription dto, OutputStepDescriptionController controller)
    {
        controller.setOutputConnected(dto.isConnected(),dto.getDataConnectedName(),dto.getStepConnectedName());
    }
    void RemoveAllTitlesFromAccordionOutputsStep()
    {
        allStepsOutputs.getPanes().remove(0,allStepsOutputs.getPanes().size());
    }
    public void addAllOutputsStep(List<DtoDataMoreDescription> list)
    {
        RemoveAllTitlesFromAccordionOutputsStep();
        for(DtoDataMoreDescription dto:list)
        {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowsdashboard/flowdetails/stepdetails/outputstepdetails/OutputStepDescription.fxml"));
                AnchorPane res=loader.load();
                OutputStepDescriptionController controller=loader.getController();
                controller.setStepMoreDescriptionController(this);
                setOutputStepDescription(dto,controller);
                TitledPane titledPane=new TitledPane(dto.getDataFinalName(),res);
                allStepsOutputs.getPanes().add(titledPane);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
