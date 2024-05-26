package application.mainappclient.flowsdashboard.flowdetails.stepdetails.outputstepdetails;

import application.mainappclient.flowsdashboard.flowdetails.stepdetails.StepMoreDescriptionController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class OutputStepDescriptionController {

    private StepMoreDescriptionController stepMoreDescriptionController;
    @FXML
    private Label outputConnected;

    @FXML
    private GridPane gridForConnected;

    @FXML
    private Label inputName;

    @FXML
    private Text inputStepName;

    public StepMoreDescriptionController getStepMoreDescriptionController() {
        return stepMoreDescriptionController;
    }

    public void setStepMoreDescriptionController(StepMoreDescriptionController stepMoreDescriptionController) {
        this.stepMoreDescriptionController = stepMoreDescriptionController;
    }

    public void setOutputConnected(Boolean outputConnected,String inputName,String inputStepName) {
        if(outputConnected)
            this.outputConnected.setText("yes");
        else
            this.outputConnected.setText("No");
        if(outputConnected)
        {
            setInputName(inputName);
            setInputStepName(inputStepName);
            gridForConnected.setDisable(false);
        }
        else
        {
            gridForConnected.setDisable(true);
        }
    }

    public void setInputName(String inputName) {
        this.inputName.setText(inputName);
    }

    public void setInputStepName(String inputStepName) {
        this.inputStepName.setText(inputStepName);
    }
}
