package application;

import application.executionhistory.FlowExecutionHistoryController;
import application.rolesmanagement.RoleManagementController;
import application.statistics.StatisticsController;
import application.stepperheader.LoadFileController;
import application.usersmanagement.UserManagementController;
import com.google.gson.Gson;
import dto.DtoRoleList;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import tasks.ExecutionsRefresher;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;

import static utils.Constants.REFRESH_RATE;

public class AppController implements Closeable
{
    @FXML
    private HBox statistics;
    @FXML
    private StatisticsController statisticsController;
    @FXML private VBox LoadFile;
    @FXML private LoadFileController LoadFileController;
    @FXML ScrollPane ToResetTheHistory;
    @FXML private Tab rolesManagementTab;
    @FXML private Tab usersManagementTab;
    @FXML private Tab historyTab;
    @FXML private Tab statisticsTab;
    @FXML private TabPane allTabs;
    @FXML private FlowExecutionHistoryController flowExecutionHistoryController;
    @FXML private VBox flowExecutionHistory;
    @FXML private HBox userManagement;
    @FXML private UserManagementController userManagementController;
    @FXML private HBox roleManagement;
    @FXML private RoleManagementController roleManagementController;
    private final SimpleBooleanProperty hasExecution;
    private ExecutionsRefresher executionRefresher;
    private Timer timer;
    private final SimpleIntegerProperty numFlowsExecute;
    private Stage primaryStage;

    public AppController() {

        this.hasExecution=new SimpleBooleanProperty(false);
        this.numFlowsExecute=new SimpleIntegerProperty(0);
    }
    @FXML
    public void initialize() {
        if (LoadFileController != null&&statisticsController!=null
        &&flowExecutionHistoryController!=null&&userManagementController!=null
        &&roleManagementController!=null) {
            LoadFileController.setMainController(this);
            statisticsController.setMainController(this);
            flowExecutionHistoryController.setMainController(this);
            userManagementController.setMainController(this);
            roleManagementController.setMainController(this);
        }
        historyTab.disableProperty().bind(hasExecution.not());
        statisticsTab.disableProperty().bind(hasExecution.not());
        usersManagementTab.setOnSelectionChanged(event ->
        {
            userManagement.getChildren().remove(userManagementController.getUserDetails());
            userManagementController.removeChoice();
        });
        historyTab.setOnSelectionChanged(event -> {
            ResetTheHistory();
            flowExecutionHistoryController.getTreeAndDetails().getChildren().remove(0, flowExecutionHistoryController.getTreeAndDetails().getChildren().size());
            flowExecutionHistoryController.getFlowExecutionHistory().getChildren().remove(3,flowExecutionHistoryController.getFlowExecutionHistory().getChildren().size());

        });
        rolesManagementTab.setOnSelectionChanged(
                event ->
                {
                    roleManagementController.RemovePrevDetailsOrNewRole();
                    roleManagementController.getRolesListController().RequestForRoleList();

                });
        numFlowsExecute.addListener((observable, oldValue, newValue) ->
        {
                if(newValue.intValue()==1)
                {
                    hasExecution.setValue(true);
                }
                this.statisticsController.RequestStatistics();
                this.flowExecutionHistoryController.
                        RequestAllHistoryExecutions(this.flowExecutionHistoryController.
                                getResultFilterComboBox().getValue());
        });

        startExecutionsRefresher();
        userManagementController.startUserListRefresher();
    }
    public void handleFailureAdminLogin(String msg)
    {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.setWidth(300);
        alert.show();
    }
    public void RequestIsAdminExist() {
        String finalUrl = HttpUrl
                .parse(Constants.IS_ADMIN_EXIST)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String massage=response.body().string();
                    if (!response.isSuccessful()) {
                        Platform.runLater(() -> {
                            handleFailureAdminLogin(massage);
                            primaryStage.close();
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public RoleManagementController getRoleManagementController() {
        return roleManagementController;
    }

    public HBox getRoleManagement() {
        return roleManagement;
    }

    public void setNumFlowsExecute(int numFlowsExecute) {
        this.numFlowsExecute.set(numFlowsExecute);
    }

    public HBox getUserManagement() {
        return userManagement;
    }

    public void ResetTheHistory()
    {
        ToResetTheHistory.setHvalue(0);
        ToResetTheHistory.setVvalue(0);
    }

    public void startExecutionsRefresher()
    {
        this.executionRefresher = new ExecutionsRefresher(this::setNumFlowsExecute);
        timer = new Timer();
        timer.schedule(this.executionRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    @Override
    public void close() {
        if (timer != null&&this.executionRefresher!=null) {
            this.executionRefresher.cancel();
            this.timer.cancel();
        }
    }

}
