package application.mainappclient.flowexecution.continuationdetails;

import application.mainappclient.executionhistory.detailsofselectdexecution.ExecutionHistoryDetailsController;
import application.mainappclient.flowexecution.FlowExecutionController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.util.List;
import java.util.UUID;

public class ChooseContinuationController {

    private FlowExecutionController flowExecutionController;
    private ExecutionHistoryDetailsController executionHistoryDetailsController;
    @FXML
    private ListView<String> ContinuationFlowsNames;
    private String chosenContinuation=null;

    @FXML
    private Button continuationButton;



    @FXML
    public void initialize() {
        ContinuationFlowsNames.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                chosenContinuation=newValue;
            }
        });
    }
    @FXML
    void continuationButtonAction(ActionEvent event)
    {
        if(chosenContinuation!=null)
        {
            if(this.executionHistoryDetailsController!=null)
                this.executionHistoryDetailsController.getFlowExecutionHistoryController().getMainController()
                        .getFlowDetailsController().RequestOptionalExecutionAndFreeInputs("false",chosenContinuation,executionHistoryDetailsController.getChosenFlowId(),null);
            else
                this.flowExecutionController.getMainController().getFlowDetailsController().RequestOptionalExecutionAndFreeInputs("true",chosenContinuation,null,null);

        }
    }
    public void addAllContinuationFlowsNames(List<String> names)
    {
        ContinuationFlowsNames.getItems().addAll(names);
    }

    public ExecutionHistoryDetailsController getExecutionHistoryDetailsController() {
        return executionHistoryDetailsController;
    }

    public void setExecutionHistoryDetailsController(ExecutionHistoryDetailsController executionHistoryDetailsController) {
        this.executionHistoryDetailsController = executionHistoryDetailsController;
    }

    public FlowExecutionController getFlowExecutionController() {
        return flowExecutionController;
    }

    public void setFlowExecutionController(FlowExecutionController flowExecutionController) {
        this.flowExecutionController = flowExecutionController;
    }
}
