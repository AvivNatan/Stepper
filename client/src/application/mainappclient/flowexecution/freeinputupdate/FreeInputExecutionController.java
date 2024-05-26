package application.mainappclient.flowexecution.freeinputupdate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class FreeInputExecutionController {

    private FreeInputsExecutionController freeInputsExecutionController;
    @FXML
    private Label inputName;

    @FXML
    private Label necessity;



    @FXML
    void setChosenFreeInput(MouseEvent event)
    {
        freeInputsExecutionController.setChosenFreeInput(inputName.getText());
        freeInputsExecutionController.setIsFreeInputSelected(true);
        freeInputsExecutionController.addFreeInputFillingScreen();
    }


    public void setInputName(String inputName) {
        this.inputName.setText(inputName);
    }

    public void setNecessity(String necessity) {
        this.necessity.setText(necessity);
    }

    public FreeInputsExecutionController getFreeInputsExecutionController() {
        return freeInputsExecutionController;
    }

    public void setFreeInputsExecutionController(FreeInputsExecutionController freeInputsExecutionController) {
        this.freeInputsExecutionController = freeInputsExecutionController;
    }

}
