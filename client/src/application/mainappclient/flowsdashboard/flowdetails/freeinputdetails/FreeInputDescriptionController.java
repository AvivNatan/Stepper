package application.mainappclient.flowsdashboard.flowdetails.freeinputdetails;

import application.mainappclient.flowsdashboard.flowdetails.freeinputsdetails.FreeInputsDescriptionController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class FreeInputDescriptionController {

    private FreeInputsDescriptionController freeInputsController;

    @FXML
    private Label Name;

    @FXML
    private Label Type;

    @FXML
    private Label Necessity;

    @FXML
    private Text StepsOfInput;

    public void setFreeInputsController(FreeInputsDescriptionController freeInputsController) {
        this.freeInputsController = freeInputsController;
    }

    public FreeInputsDescriptionController getFreeInputsController() {
        return freeInputsController;
    }

    public void setName(String name) {
        Name.setText(name);
    }

    public void setType(String type) {
        Type.setText(type);
    }

    public void setNecessity(String necessity) {
        Necessity.setText(necessity);
    }

    public void setStepsOfInput(String stepsOfInput) {
        StepsOfInput.setText(stepsOfInput);
    }
}
