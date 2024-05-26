package application.mainappclient.flowsdashboard.flowdescription;

import application.mainappclient.flowsdashboard.flowsdescription.FlowsDescriptionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class FlowDescriptionController {

    private FlowsDescriptionController flowsDescriptionController;
    @FXML
    private Button chooseFlowButton;

    @FXML
    private Label Name;

    @FXML
    private Text Description;

    @FXML
    private Label StepsNum;

    @FXML
    private Label FreeInputsNum;

    @FXML
    private Label ContinuationsNum;

    @FXML
    public void ChooseFlowButtonAction(ActionEvent event)
    {
        flowsDescriptionController.setChosenFlowName(Name.getText());
        flowsDescriptionController.ShowDetailsOfFlow();
        flowsDescriptionController.getMainController().updateVisibleDetails();

    }
    public void setName(String name) {
        Name.setText(name);
    }

    public void setDescription(String description) {
        Description.setText(description);
    }

    public void setStepsNum(Integer stepsNum) {
        StepsNum.setText(stepsNum.toString());
    }

    public void setFreeInputsNum(Integer freeInputsNum) {
        FreeInputsNum.setText(freeInputsNum.toString());
    }

    public void setContinuationsNum(Integer continuationsNum) {
        ContinuationsNum.setText(continuationsNum.toString());
    }

    public FlowsDescriptionController getFlowsDescriptionController() {
        return flowsDescriptionController;
    }

    public void setFlowsDescriptionController(FlowsDescriptionController flowsDescriptionController) {
        this.flowsDescriptionController = flowsDescriptionController;
    }
}
