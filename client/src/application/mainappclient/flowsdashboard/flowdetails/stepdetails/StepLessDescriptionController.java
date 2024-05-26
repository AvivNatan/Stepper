package application.mainappclient.flowsdashboard.flowdetails.stepdetails;

import application.mainappclient.flowsdashboard.flowdetails.stepsdetails.StepsDescriptionController;
import dto.DtoFlowDescription;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class StepLessDescriptionController {

    private StepsDescriptionController stepsDescriptionController;
    @FXML
    private Button StepInformationButton;

    @FXML
    private Text stepName;

    @FXML
    private Label stepReadOnly;

    public StepsDescriptionController getStepsDescriptionController() {
        return stepsDescriptionController;
    }

    public void setStepsDescriptionController(StepsDescriptionController stepsDescriptionController) {
        this.stepsDescriptionController = stepsDescriptionController;
    }

    public void setStepName(String stepName) {
        this.stepName.setText(stepName);
    }

    public void setStepReadOnly(String stepReadOnly) {
        this.stepReadOnly.setText(stepReadOnly);
    }

    @FXML
    void ShowMoreInformationOfStep(MouseEvent event)
    {
        stepsDescriptionController.setChosenStepForMoreInfo(stepName.getText());
        stepsDescriptionController.getFlowDetailsController().getMainController().
                RequestForFlowsDescriptions(new Consumer<List<DtoFlowDescription>>() {
                    @Override
                    public void accept(List<DtoFlowDescription> dtoFlowDescriptions) {
                        stepsDescriptionController.getFlowDetailsController().
                                getMainController().ShowMoreDescriptionStepsOfChosenFlow(dtoFlowDescriptions);
                    }
                });
    }


}
