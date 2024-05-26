package application.mainappclient;

import application.MainAppController;
import application.mainappclient.executionhistory.FlowExecutionHistoryController;
import application.mainappclient.flowexecution.FlowExecutionController;
import application.mainappclient.flowsdashboard.flowdetails.FlowDetailsController;
import application.mainappclient.flowsdashboard.flowsdescription.FlowsDescriptionController;
import application.mainappclient.headerclient.HeaderClientController;
import com.google.gson.Gson;
import dto.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import tasks.ExecutionRefresherForManager;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.UUID;
import java.util.function.Consumer;

import static util.Constants.REFRESH_RATE;

public class AppController implements Closeable
{
    private MainAppController mainAppController;
    @FXML
    private FlowDetailsController flowDetailsController;
    @FXML
    HBox flowDetails;
    @FXML
    private FlowsDescriptionController flowsDescriptionController;
    @FXML
    private Accordion flowsDescription;
    @FXML ScrollPane ToResetTheExecution;
    @FXML ScrollPane ToResetTheHistory;
    @FXML private Tab flowExecutionTab;
    @FXML private Tab flowDefinitionTab;
    @FXML private Tab historyTab;
    @FXML private TabPane allTabs;
    @FXML private VBox flowExecution;
    @FXML private FlowExecutionController flowExecutionController;
    @FXML private FlowExecutionHistoryController flowExecutionHistoryController;
    @FXML private VBox flowExecutionHistory;
    private GridPane ReExecute;
    @FXML private HeaderClientController headerClientController;
    @FXML private VBox headerClient;
    private String userName;
    private final SimpleBooleanProperty hasExecution;
    private ExecutionRefresherForManager executionRefresher;
    private Timer timer;
    private final SimpleIntegerProperty numFlowsExecute;

    public AppController() {
        this.hasExecution=new SimpleBooleanProperty(false);
        this.numFlowsExecute=new SimpleIntegerProperty(0);
    }

    @FXML
    public void initialize() {
        if (flowsDescriptionController!=null &&flowDetailsController!=null &&
                flowExecutionController!=null&&flowExecutionHistoryController!=null
        &&headerClientController!=null) {
            flowsDescriptionController.setMainController(this);
            flowDetailsController.setMainController(this);
            flowExecutionController.setMainController(this);
            flowExecutionHistoryController.setMainController(this);
            headerClientController.setMainController(this);
        }
        if(flowExecutionController.getFilling()!=null)
        {
            flowExecutionController.getFilling().visibleProperty().bind(Bindings.and(flowExecutionController.getFreeInputsExecutionController().isFreeInputSelectedProperty(),
                    this.flowExecutionTab.disableProperty().not()));
        }

        flowExecutionTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event)
            {
                RequestIsCurrentExecution();
            }
        });
        historyTab.disableProperty().bind(hasExecution.not());
        historyTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ResetTheHistory();
                flowExecutionHistoryController.getResultFilterComboBox().setValue("No Filter");
                flowExecutionHistoryController.RequestAllHistoryExecutions("No Filter");
                flowExecutionHistoryController.getTreeAndDetails().getChildren().remove(0, flowExecutionHistoryController.getTreeAndDetails().getChildren().size());
                flowExecutionHistoryController.getFlowExecutionHistory().getChildren().remove(3,flowExecutionHistoryController.getFlowExecutionHistory().getChildren().size());

            }
        });
        numFlowsExecute.addListener((observable, oldValue, newValue) ->
        {
            if(newValue.intValue()==1)
            {
                hasExecution.setValue(true);
            }
            this.flowExecutionHistoryController.
                    RequestAllHistoryExecutions(this.flowExecutionHistoryController.
                            getResultFilterComboBox().getValue());
        });
        startExecutionsRefresher();
    }

    public FlowExecutionHistoryController getFlowExecutionHistoryController() {
        return flowExecutionHistoryController;
    }

    public HeaderClientController getHeaderClientController() {
        return headerClientController;
    }

    public Tab getFlowExecutionTab() {
        return flowExecutionTab;
    }
    public void setMainAppController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }
    public void RequestIsCurrentExecution()
    {
        String finalUrl = HttpUrl
                .parse(Constants.IS_EXECUTION_CURRENT)
                .newBuilder()
                .addQueryParameter("username",userName)
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
                        DtoCurrentExecutionResponse dto = new Gson().fromJson(response.body().string(), DtoCurrentExecutionResponse.class);
                        Platform.runLater(() -> {
                            if(dto.isResult())
                            {
                                if(dto.getResult()!=null)
                                    flowExecution.setVisible(false);
                            }

                        });
                    }}finally {
                    response.close();
                }
            }
        });
    }
    public void startExecutionsRefresher()
    {
        this.executionRefresher = new ExecutionRefresherForManager(this::setNumFlowsExecute);
        timer = new Timer();
        timer.schedule(this.executionRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public void setNumFlowsExecute(int numFlowsExecute) {
        this.numFlowsExecute.set(numFlowsExecute);
    }

    public void addReExecuteButtonToExecutionTab(UUID id)
    {
        this.ReExecute=new GridPane();
        Button button=new Button("Click Here To Execute Again");
        this.ReExecute.getChildren().add(button);
        this.ReExecute.setPadding(new Insets(5,50,5,900));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                flowDetailsController.RequestOptionalExecutionAndFreeInputs("false",null,null,id);
            }
        });
        flowExecution.getChildren().add(this.ReExecute);
    }
    public void ResetTheExecution()
    {
        ToResetTheExecution.setHvalue(0);
        ToResetTheExecution.setVvalue(0);
    }
    public void ResetTheHistory()
    {
        ToResetTheHistory.setHvalue(0);
        ToResetTheHistory.setVvalue(0);
    }
    @FXML void changeFillingNotSelected()
    {
        flowExecutionController.getFreeInputsExecutionController().setIsFreeInputSelected(false);
    }
    public void addContinuationDetails(List<String> list)
    {
        flowExecutionController.addContinuationInformation(list);
        if(flowExecutionController.getChooseContinuation()!=null)
        {
            flowExecution.getChildren().add(flowExecutionController.getChooseContinuation());
        }
    }

    public VBox getFlowExecution() {
        return flowExecution;
    }

    public FlowDetailsController getFlowDetailsController() {
        return flowDetailsController;
    }

    public String getChosenFlowName()
    {
        return flowsDescriptionController.getChosenFlowName();
    }

    public void RequestForFlowsDescriptions(Consumer<List<DtoFlowDescription>> funcForFlowsDescription) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_DEFINITION)
                .newBuilder()
                .addQueryParameter("username",userName)
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
                        DtoFlowsDescriptions listFlows = new Gson().fromJson(response.body().string(), DtoFlowsDescriptions.class);
                        List<DtoFlowDescription> listFlowsDetails = listFlows.getFlows();
                        Platform.runLater(() -> {
                            funcForFlowsDescription.accept(listFlowsDetails);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });

    }

    public String getUserName() {
        return userName;
    }

    public void UpdateDetailsOfChosenFlow(List<DtoFlowDescription> listFlowsDetails)
    {
        for(DtoFlowDescription dto:listFlowsDetails)
        {
            if(Objects.equals(dto.getName(), flowsDescriptionController.getChosenFlowName()))
            {
                flowDetailsController.setNameFlow(dto.getName());
                flowDetailsController.setDescription(dto.getDescription());
                flowDetailsController.setFormalOutputs(dto.getStringOfFormalOutputs());
                if(dto.isReadOnly())
                    flowDetailsController.setReadOnly("yes");
                else
                    flowDetailsController.setReadOnly("No");
                flowDetailsController.setFreeInputsDescription(dto.getFreeInput());
                flowDetailsController.setOutputsDescription(dto.getOutputs());
                flowDetailsController.setStepsDescription(dto.getSteps());
                break;
            }
        }

    }
    public void openExecutionTab()
    {
        flowExecutionTab.disableProperty().setValue(false);
        flowExecution.setVisible(true);
        allTabs.getSelectionModel().select(flowExecutionTab);
        ResetTheExecution();
        flowExecution.getChildren().remove(this.ReExecute);
        flowExecutionController.UpdateToNewExecution();
    }
    public void closeExecutionTab()
    {
        flowExecutionController.getFreeInputsExecutionController().isFreeInputSelectedProperty().setValue(false);
        allTabs.getSelectionModel().select(flowDefinitionTab);
        flowExecutionController.isExecutableProperty().setValue(false);
    }

    public FlowExecutionController getFlowExecutionController() {
        return flowExecutionController;
    }

    public void ShowMoreDescriptionStepsOfChosenFlow(List<DtoFlowDescription> list) {

        for(DtoFlowDescription dto:list) {
            if (Objects.equals(dto.getName(), flowsDescriptionController.getChosenFlowName())) {
                flowDetailsController.
                        addStepMoreDescription(dto.getStepsMoreInfo());
            }
        }
    }

    public void updateVisibleStepMoreInfo()
    {
        flowDetailsController.getStepMoreDescription().setVisible(false);
    }

    public void updateVisibleDefinitions()
    {
        flowsDescription.setVisible(true);
    }
    public void updateNotVisibleDefinitions()
    {
        flowsDescription.setVisible(false);
    }
    public void updateVisibleDetails()
    {
        flowDetails.setVisible(true);
    }
    public void updateNotVisibleDetails()
    {
        flowDetails.setVisible(false);
    }
    public void disableHistoryTab()
    {
        historyTab.disableProperty().setValue(true);
    }
    public void notDisableHistoryTab()
    {
        historyTab.disableProperty().setValue(false);
    }
    public void updateUserNameInHeaderClient(String userName)
    {
        headerClientController.setNameClient(userName);
        this.userName=userName;
    }
    @Override
    public void close() throws IOException {
        flowsDescriptionController.close();
        if (timer != null&&this.executionRefresher!=null) {
            this.executionRefresher.cancel();
            this.timer.cancel();
        }
    }
    public void setActive() {
        flowsDescriptionController.startListRefresher();
        headerClientController.startHeaderRefresher();
    }

}
