package application.mainappclient.executionhistory;

import application.mainappclient.AppController;
import application.mainappclient.executionhistory.detailsofselectdexecution.ExecutionHistoryDetailsController;
import application.mainappclient.executionhistory.tableOfExecutions.TableExecutionHistoryController;
import com.google.gson.Gson;
import dto.DtoFlowDescription;
import dto.DtoFlowExecutionHistory;
import dto.DtoFlowsDescriptions;
import dto.DtoFlowsExecutionHistoryResponse;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FlowExecutionHistoryController
{
    private AppController mainController;
    @FXML
    private TableView<DtoFlowExecutionHistory> tableExecutionHistory;
    @FXML
    private TableExecutionHistoryController tableExecutionHistoryController;
    @FXML
    private ExecutionHistoryDetailsController treeAndDetailsController;
    @FXML
    private HBox treeAndDetails;
    @FXML
    private ComboBox<String> resultFilterComboBox;
    @FXML private VBox flowExecutionHistory;
    private GridPane reExecuteButton;

    @FXML
    public void initialize() {
        if (tableExecutionHistoryController != null && treeAndDetailsController != null) {
            tableExecutionHistoryController.setFlowExecutionHistoryController(this);
            treeAndDetailsController.setFlowExecutionHistoryController(this);
        }
        addSortComboBoxResult();
        tableExecutionHistory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DtoFlowExecutionHistory>() {
            @Override
            public void changed(ObservableValue<? extends DtoFlowExecutionHistory> observable, DtoFlowExecutionHistory oldValue, DtoFlowExecutionHistory newValue) {
                   if(newValue!=null)
                   {
                       treeAndDetailsController.setChosenFlowId(newValue.getId());
                       RemoveAndUpdateForReExecution();
                   }
            }
        });
    }
    public void RemoveAndUpdateForReExecution()
    {
        treeAndDetails.getChildren().remove(0,treeAndDetails.getChildren().size());
        flowExecutionHistory.getChildren().remove(treeAndDetailsController.getChooseContinuation());
        flowExecutionHistory.getChildren().remove(reExecuteButton);
        treeAndDetailsController.RequestHistoryDetails();
    }


    public void addReExecuteButtonToHistoryTab(UUID id)
    {
        reExecuteButton=new GridPane();
        Button button=new Button("Click Here To Execute Again");
        this.reExecuteButton.getChildren().add(button);
        this.reExecuteButton.setPadding(new Insets(5,50,5,900));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainController.getFlowDetailsController().RequestOptionalExecutionAndFreeInputs("false",null,null,id);
            }
        });
        flowExecutionHistory.getChildren().add(reExecuteButton);
    }
    public HBox getTreeAndDetails() {
        return treeAndDetails;
    }

    public ExecutionHistoryDetailsController getTreeAndDetailsController() {
        return treeAndDetailsController;
    }

    public void addContinuationDetails(List<String> list)
    {
        treeAndDetailsController.addContinuationInformation(list);
        if(treeAndDetailsController.getChooseContinuation()!=null)
        {
            flowExecutionHistory.getChildren().add(treeAndDetailsController.getChooseContinuation());
        }
    }

    public ComboBox<String> getResultFilterComboBox() {
        return resultFilterComboBox;
    }

    public void addSortComboBoxResult()
    {
       this.resultFilterComboBox.getItems().addAll("SUCCESS", "FAILURE", "WARNING","No Filter");
       this.resultFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
           RequestAllHistoryExecutions(newValue);
        });
    }

    public VBox getFlowExecutionHistory() {
        return flowExecutionHistory;
    }
    public void RequestAllHistoryExecutions(String value)
    {
        String finalUrl = HttpUrl
                .parse(Constants.GET_FLOWS_HISTORY)
                .newBuilder()
                .addQueryParameter("username",mainController.getUserName())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        DtoFlowsExecutionHistoryResponse listFlows = new Gson().fromJson(response.body().string(), DtoFlowsExecutionHistoryResponse.class);
                        List<DtoFlowExecutionHistory> dto=listFlows.getList();
                        Platform.runLater(() -> {
                            if (dto.size() != 0) {
                                ObservableList<DtoFlowExecutionHistory> flowExecutionList;
                                if (!Objects.equals(value, "No Filter")) {
                                    flowExecutionList = FXCollections.observableArrayList();
                                    for (DtoFlowExecutionHistory history : dto) {
                                        if (Objects.equals(history.getResult(), value))
                                            flowExecutionList.add(history);
                                    }
                                } else
                                    flowExecutionList = FXCollections.observableArrayList(dto);
                                tableExecutionHistory.setItems(flowExecutionList);
                            }
                        });
                    }}finally {
                    response.close();
                }
            }
        });
    }
    public AppController getMainController() {
        return mainController;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
