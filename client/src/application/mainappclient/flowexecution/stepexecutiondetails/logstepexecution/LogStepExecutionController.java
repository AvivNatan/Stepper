package application.mainappclient.flowexecution.stepexecutiondetails.logstepexecution;

import application.mainappclient.flowexecution.stepexecutiondetails.StepExecutionDetailsController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class LogStepExecutionController {

    private StepExecutionDetailsController stepExecutionDetailsController;
    @FXML
    private Label logTime;

    @FXML
    private Text logContent;


    public StepExecutionDetailsController getStepExecutionDetailsController() {
        return stepExecutionDetailsController;
    }

    public void setLogTime(String logTime) {
        this.logTime.setText(logTime);
    }

    public void setLogContent(String logContent) {
        this.logContent.setText(logContent);
    }

    public void setStepExecutionDetailsController(StepExecutionDetailsController stepExecutionDetailsController) {
        this.stepExecutionDetailsController = stepExecutionDetailsController;
    }
}
