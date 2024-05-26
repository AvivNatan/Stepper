package application.executionhistory.tableOfExecutions;

import application.AppController;
import application.executionhistory.FlowExecutionHistoryController;
import dto.DtoFlowExecutionHistory;
import javafx.fxml.FXML;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableExecutionHistoryController {

    private FlowExecutionHistoryController flowExecutionHistoryController;

    @FXML
    private TableColumn<DtoFlowExecutionHistory, String> flowNameCol;

    @FXML
    private TableColumn<DtoFlowExecutionHistory, String> startTimeCol;

    @FXML
    private TableColumn<DtoFlowExecutionHistory, String> resultCol;


    @FXML
    public void initialize() {
        flowNameCol.setCellValueFactory(new PropertyValueFactory<DtoFlowExecutionHistory,String>("flowName"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<DtoFlowExecutionHistory,String>("startTime"));
        resultCol.setCellValueFactory(new PropertyValueFactory<DtoFlowExecutionHistory,String>("result"));
    }
    public TableColumn<DtoFlowExecutionHistory, String> getFlowNameCol() {
        return flowNameCol;
    }

    public void setFlowNameCol(TableColumn<DtoFlowExecutionHistory, String> flowNameCol) {
        this.flowNameCol = flowNameCol;
    }

    public TableColumn<DtoFlowExecutionHistory, String> getStartTimeCol() {
        return startTimeCol;
    }

    public void setStartTimeCol(TableColumn<DtoFlowExecutionHistory, String> startTimeCol) {
        this.startTimeCol = startTimeCol;
    }

    public TableColumn<DtoFlowExecutionHistory, String> getResultCol() {
        return resultCol;
    }

    public void setResultCol(TableColumn<DtoFlowExecutionHistory, String> resultCol) {
        this.resultCol = resultCol;
    }

    public FlowExecutionHistoryController getFlowExecutionHistoryController() {
        return flowExecutionHistoryController;
    }

    public void setFlowExecutionHistoryController(FlowExecutionHistoryController flowExecutionHistoryController) {
        this.flowExecutionHistoryController = flowExecutionHistoryController;
    }
}
