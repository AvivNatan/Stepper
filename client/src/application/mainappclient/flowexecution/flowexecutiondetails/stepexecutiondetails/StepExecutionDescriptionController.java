package application.mainappclient.flowexecution.flowexecutiondetails.stepexecutiondetails;

import application.mainappclient.flowexecution.flowexecutiondetails.stepsexecutiondetails.StepsExecutionDescriptionController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import stepper.flow.excution.context.StepsLog;

public class StepExecutionDescriptionController {

    private StepsExecutionDescriptionController stepsExecutionDescriptionController;
    @FXML private Label stepName;

    @FXML private Label result;

    @FXML private Text summeryLine;

    @FXML private Label totalTime;

    @FXML private GridPane logsGrid;


    public void setStepsExecutionDescriptionController(StepsExecutionDescriptionController stepsExecutionDescriptionController) {
        this.stepsExecutionDescriptionController = stepsExecutionDescriptionController;
    }

    public void setStepName(String stepName) {
        this.stepName.setText(stepName);
    }

    public void setResult(String result) {
        this.result.setText(result);
    }

    public void setSummeryLine(String summeryLine) {
        this.summeryLine.setText(summeryLine);
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime.setText(String.format("%,d", totalTime));
    }
    public void addRowNoLogsToGrid()
    {
        logsGrid.addRow(logsGrid.getRowConstraints().size());
        Label log=new Label("no log's");
        TextFlow flow=new TextFlow(log);
        logsGrid.add(flow,0,logsGrid.getRowConstraints().size());
    }
    public void addRowLogToGrid(StepsLog log,int index)
    {
        Label logTime=new Label(log.getCreationLogTime());
        TextFlow timeFlow=new TextFlow(logTime);
        Text logContent=new Text(log.getDataLog());
        TextFlow contentFlow=new TextFlow(logContent);

        logsGrid.addRow(logsGrid.getRowConstraints().size()+index);
        logsGrid.add(timeFlow,0,logsGrid.getRowConstraints().size()+index);
        logsGrid.add(contentFlow,1,logsGrid.getRowConstraints().size()+index);

    }
    public void RemoveRowsOfLogs()
    {
        logsGrid.getRowConstraints().remove(1,logsGrid.getRowConstraints().size());
    }
}
