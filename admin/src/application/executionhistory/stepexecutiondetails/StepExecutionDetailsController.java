package application.executionhistory.stepexecutiondetails;
import application.executionhistory.detailsofselectdexecution.ExecutionHistoryDetailsController;
//import application.flowexecution.FlowExecutionController;
//import application.flowexecution.stepexecutiondetails.inputoutputstepExecution.InputOutputStepExecutionController;
//import application.flowexecution.stepexecutiondetails.logstepexecution.LogStepExecutionController;
import application.executionhistory.stepexecutiondetails.inputoutputstepExecution.InputOutputStepExecutionController;
import application.executionhistory.stepexecutiondetails.logstepexecution.LogStepExecutionController;
import dto.DtoInputOutputForStepDetails;
import dto.DtoStepExecutionDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import stepper.flow.excution.context.StepsLog;

import java.io.IOException;

public class StepExecutionDetailsController {

    private ExecutionHistoryDetailsController executionHistoryDetailsController;
    @FXML
    private Label stepName;

    @FXML
    private Label result;

    @FXML
    private Label startTime;

    @FXML
    private Label EndTime;

    @FXML
    private Label totalTime;

    @FXML
    private ListView<GridPane> inputsList;

    @FXML
    private ListView<GridPane> outputsList;

    @FXML
    private ListView<GridPane> logsList;


    public ExecutionHistoryDetailsController getExecutionHistoryDetailsController() {
        return executionHistoryDetailsController;
    }

    public void setExecutionHistoryDetailsController(ExecutionHistoryDetailsController executionHistoryDetailsController) {
        this.executionHistoryDetailsController = executionHistoryDetailsController;
    }

    public void setStepName(String stepName) {
        this.stepName.setText(stepName);
    }

    public void setResult(String result) {
        this.result.setText(result);
    }

    public void setStartTime(String startTime) {
        this.startTime.setText(startTime);
    }

    public void setEndTime(String endTime) {
        EndTime.setText(endTime);
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime.setText(String.format("%,d", totalTime));
    }
    public void setInputOrOutputDetails(DtoInputOutputForStepDetails dto, GridPane pane, InputOutputStepExecutionController controller) {
        controller.setInputName(dto.getName());
        controller.setType(dto.getType());
        controller.setContent(dto.getContent(),pane);
    }
    public void setLogDetails(StepsLog log, LogStepExecutionController controller) {
        controller.setLogTime(log.getCreationLogTime());
        controller.setLogContent(log.getDataLog());
    }

    public void addAllInputsToList(DtoStepExecutionDetails dto)
    {
            for(DtoInputOutputForStepDetails input:dto.getInputs())
            {
                try
                {
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/executionhistory/stepexecutiondetails/inputoutputstepExecution/InputOutputStepExecution.fxml"));
                    GridPane res=loader.load();
                    InputOutputStepExecutionController controller=loader.getController();
                    controller.setStepExecutionDetailsController(this);
                    setInputOrOutputDetails(input,res,controller);
                    inputsList.getItems().add(res);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    }
    public void addAllOutputsToList(DtoStepExecutionDetails dto)
    {
        for(DtoInputOutputForStepDetails output:dto.getOutputs())
        {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/executionhistory/stepexecutiondetails/inputoutputstepExecution/InputOutputStepExecution.fxml"));
                GridPane res=loader.load();
                InputOutputStepExecutionController controller=loader.getController();
                controller.setStepExecutionDetailsController(this);
                setInputOrOutputDetails(output,res,controller);
                outputsList.getItems().add(res);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addAllLogsToList(DtoStepExecutionDetails dto)
    {
        for(StepsLog log:dto.getLogs())
        {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/executionhistory/stepexecutiondetails/logstepexecution/LogStepExecution.fxml"));
                GridPane res=loader.load();
                LogStepExecutionController controller=loader.getController();
                controller.setStepExecutionDetailsController(this);
                setLogDetails(log,controller);
                logsList.getItems().add(res);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
