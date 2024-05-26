package application.mainappclient.flowsdashboard.flowdetails.stepdetails.inputstepdetails;

import application.mainappclient.flowsdashboard.flowdetails.stepdetails.StepMoreDescriptionController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class InputStepDescriptionController {

    private StepMoreDescriptionController stepMoreDescriptionController;
    @FXML
    private Label necessity;

    @FXML
    private Label inputConnected;

    @FXML
    private Label outputName;

    @FXML
    private Text outputStepName;
    @FXML
    private GridPane gridForConnected;

    public StepMoreDescriptionController getStepMoreDescriptionController() {
        return stepMoreDescriptionController;
    }

    public void setStepMoreDescriptionController(StepMoreDescriptionController stepMoreDescriptionController) {
        this.stepMoreDescriptionController = stepMoreDescriptionController;
    }


    public void setNecessity(String necessity) {
        this.necessity.setText(necessity);
    }
    public void setInputConnected(Boolean inputConnected,String outputName,String outputStepName) {
          if(inputConnected)
             this.inputConnected.setText("yes");
          else
              this.inputConnected.setText("No");
        if(inputConnected)
        {
            setOutputName(outputName);
            setOutputStepName(outputStepName);
            gridForConnected.setDisable(false);
        }
        else
        {
            gridForConnected.setDisable(true);
        }
    }

    public void setOutputName(String outputName) {
        this.outputName.setText(outputName);
    }

    public void setOutputStepName(String outputStepName) {
        this.outputStepName.setText(outputStepName);
    }


}
