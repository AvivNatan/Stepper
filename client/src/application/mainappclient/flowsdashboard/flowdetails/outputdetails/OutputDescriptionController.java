package application.mainappclient.flowsdashboard.flowdetails.outputdetails;

import application.mainappclient.flowsdashboard.flowdetails.outputsdetails.OutputsDescriptionController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OutputDescriptionController {

    private OutputsDescriptionController outputsController;
    @FXML
    private Label Name;

    @FXML
    private Label Type;

    @FXML
    private Label StepOfOutput;

    public void setOutputsController(OutputsDescriptionController outputsController) {
        this.outputsController = outputsController;
    }

    public OutputsDescriptionController getOutputsController() {
        return outputsController;
    }

    public void setName(String name) {
        Name.setText(name);
    }

    public void setType(String type) {
        Type.setText(type);
    }
    public void setStepOfOutput(String step) {
        StepOfOutput.setText(step);
    }

}
