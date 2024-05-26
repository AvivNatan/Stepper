package application.mainappclient.flowexecution.stepexecutiondetails;
import application.mainappclient.executionhistory.detailsofselectdexecution.ExecutionHistoryDetailsController;
import application.mainappclient.flowexecution.FlowExecutionController;
import application.mainappclient.flowexecution.stepexecutiondetails.inputoutputstepExecution.InputOutputStepExecutionController;
import application.mainappclient.flowexecution.stepexecutiondetails.logstepexecution.LogStepExecutionController;
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

    private FlowExecutionController flowExecutionController;
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


    public FlowExecutionController getFlowExecutionController() {
        return flowExecutionController;
    }

    public ExecutionHistoryDetailsController getExecutionHistoryDetailsController() {
        return executionHistoryDetailsController;
    }

    public void setExecutionHistoryDetailsController(ExecutionHistoryDetailsController executionHistoryDetailsController) {
        this.executionHistoryDetailsController = executionHistoryDetailsController;
    }

    public void setFlowExecutionController(FlowExecutionController flowExecutionController) {
        this.flowExecutionController = flowExecutionController;
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
    public void setLogDetails(StepsLog log,LogStepExecutionController controller) {
        controller.setLogTime(log.getCreationLogTime());
        controller.setLogContent(log.getDataLog());
    }

    public void addAllInputsToList(DtoStepExecutionDetails dto)
    {
            for(DtoInputOutputForStepDetails input:dto.getInputs())
            {
                try
                {
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/stepexecutiondetails/inputoutputstepExecution/InputOutputStepExecution.fxml"));
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
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/stepexecutiondetails/inputoutputstepExecution/InputOutputStepExecution.fxml"));
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
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/stepexecutiondetails/logstepexecution/LogStepExecution.fxml"));
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
