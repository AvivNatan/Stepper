package application.mainappclient.flowexecution.flowexecutiondetails;

import application.mainappclient.executionhistory.detailsofselectdexecution.ExecutionHistoryDetailsController;
import application.mainappclient.flowexecution.FlowExecutionController;
import application.mainappclient.flowexecution.flowexecutiondetails.freeinputsexecutiondetails.FreeInputsExecutionDetailsController;
import application.mainappclient.flowexecution.flowexecutiondetails.outputsexecutiondetails.OutputsExecutionDescriptionController;
import application.mainappclient.flowexecution.flowexecutiondetails.stepsexecutiondetails.StepsExecutionDescriptionController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;

public class FlowExecutionDescriptionController {

    private FlowExecutionController flowExecutionController;
    private ExecutionHistoryDetailsController executionHistoryDetailsController;
    @FXML private Label userName;
    @FXML private Label role;

    @FXML private Label flowName;
    @FXML private Label startTime;
    @FXML private Label uniqueId;
    @FXML private Label resultFlow;
    @FXML private Label totalTimeFlow;
    @FXML private Accordion freeInputsInfo;
    @FXML private FreeInputsExecutionDetailsController freeInputsInfoController;
    @FXML private Accordion outputsInfo;
    @FXML private OutputsExecutionDescriptionController outputsInfoController;
    @FXML private Accordion stepsInfo;
    @FXML private StepsExecutionDescriptionController stepsInfoController;

    private final SimpleStringProperty flowNameProperty;
    private final SimpleStringProperty userNameProperty;
    private final SimpleStringProperty roleProperty;
    private final SimpleStringProperty uniqueIdProperty;
    private final SimpleStringProperty resultFlowProperty;
    private final SimpleLongProperty totalTimeFlowProperty;
    private final SimpleStringProperty startTimeFlowProperty;


    public FlowExecutionDescriptionController()
    {
        this.flowNameProperty=new SimpleStringProperty();
        this.userNameProperty=new SimpleStringProperty();
        this.roleProperty=new SimpleStringProperty();
        this.uniqueIdProperty=new SimpleStringProperty();
        this.resultFlowProperty=new SimpleStringProperty();
        this.totalTimeFlowProperty=new SimpleLongProperty();
        this.startTimeFlowProperty=new SimpleStringProperty();
    }

    @FXML
    public void initialize()
    {
        if(freeInputsInfoController!=null&&outputsInfoController!=null&&stepsInfoController!=null)
        {
            freeInputsInfoController.setFlowExecutionDescriptionController(this);
            outputsInfoController.setFlowExecutionDescriptionController(this);
            stepsInfoController.setFlowExecutionDescriptionController(this);
        }
        flowName.textProperty().bind(flowNameProperty);
        userName.textProperty().bind(userNameProperty);
        role.textProperty().bind(roleProperty);
        uniqueId.textProperty().bind(uniqueIdProperty);
        resultFlow.textProperty().bind(resultFlowProperty);
        totalTimeFlow.textProperty().bind(Bindings.format("%,d", totalTimeFlowProperty));
        startTime.textProperty().bind(startTimeFlowProperty);

    }

    public ExecutionHistoryDetailsController getExecutionHistoryDetailsController() {
        return executionHistoryDetailsController;
    }

    public void setExecutionHistoryDetailsController(ExecutionHistoryDetailsController executionHistoryDetailsController) {
        this.executionHistoryDetailsController = executionHistoryDetailsController;
    }

    public Accordion getFreeInputsInfo() {
        return freeInputsInfo;
    }

    public Accordion getOutputsInfo() {
        return outputsInfo;
    }

    public Accordion getStepsInfo() {
        return stepsInfo;
    }

    public void setFlowName(Label flowName) {
        this.flowName = flowName;
    }

    public void setFlowNameProperty(String flowNameProperty) {
        this.flowNameProperty.set(flowNameProperty);
    }

    public FlowExecutionController getFlowExecutionController() {
        return flowExecutionController;
    }

    public FreeInputsExecutionDetailsController getFreeInputsInfoController() {
        return freeInputsInfoController;
    }

    public OutputsExecutionDescriptionController getOutputsInfoController() {
        return outputsInfoController;
    }

    public StepsExecutionDescriptionController getStepsInfoController() {
        return stepsInfoController;
    }

    public void setUniqueIdProperty(String uniqueIdProperty) {
        this.uniqueIdProperty.set(uniqueIdProperty);
    }

    public void setResultFlowProperty(String resultFlowProperty) {
        this.resultFlowProperty.set(resultFlowProperty);
    }

    public void setTotalTimeFlowProperty(long totalTimeFlowProperty) {
        this.totalTimeFlowProperty.set(totalTimeFlowProperty);
    }

    public void setUserNameProperty(String userNameProperty) {
        this.userNameProperty.set(userNameProperty);
    }

    public void setRoleProperty(String roleProperty) {
        this.roleProperty.set(roleProperty);
    }

    public void setStartTimeFlowProperty(String startTimeFlowProperty) {
        this.startTimeFlowProperty.set(startTimeFlowProperty);
    }



    public void setFlowExecutionController(FlowExecutionController flowExecutionController) {
        this.flowExecutionController = flowExecutionController;
    }
}