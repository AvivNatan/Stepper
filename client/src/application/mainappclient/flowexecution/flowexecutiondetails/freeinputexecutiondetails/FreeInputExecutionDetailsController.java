package application.mainappclient.flowexecution.flowexecutiondetails.freeinputexecutiondetails;

import application.mainappclient.flowexecution.flowexecutiondetails.freeinputsexecutiondetails.FreeInputsExecutionDetailsController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import stepper.dd.imp.EnumeratorDef.EnumeratorData;

import java.util.LinkedHashMap;
import java.util.Objects;

public class FreeInputExecutionDetailsController {

    private FreeInputsExecutionDetailsController freeInputsExecutionDetailsController;
    @FXML
    private Label finalInputName;

    @FXML
    private Label type;

    @FXML
    private Text content;

    @FXML
    private Label necessity;

    public void setFreeInputsExecutionDetailsController(FreeInputsExecutionDetailsController freeInputsExecutionDetailsController) {
        this.freeInputsExecutionDetailsController = freeInputsExecutionDetailsController;
    }

    public void setFinalInputName(String finalInputName) {
        this.finalInputName.setText(finalInputName);
    }

    public void setType(String type) {
        this.type.setText(type);
    }

    public void setContent(Object content)
    {
        if(Objects.equals(this.type.getText(), "EnumeratorData"))
        {
            String chosen=((String)((LinkedHashMap)content).get("chosenValue"));
            this.content.setText(chosen);
        }
        else if(Objects.equals(this.type.getText(), "Integer"))
        {
            Integer num=((Double)content).intValue();
            this.content.setText(num.toString());
        }
        else if(Objects.equals(this.type.getText(), "Double"))
            this.content.setText(((Double)content).toString());
        else
            this.content.setText((String) content);
    }

    public void setNecessity(String necessity) {
        this.necessity.setText(necessity);
    }
}
