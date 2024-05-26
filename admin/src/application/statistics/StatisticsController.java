package application.statistics;

import application.AppController;
import application.statistics.flowstatistics.FlowStatisticsController;
import application.statistics.stepstatistics.StepStatisticsController;
import com.google.gson.Gson;
import dto.DtoActivatedFlowDescription;
import dto.DtoFlowDescription;
import dto.DtoFlowStepStatistics;
import dto.DtoStatistics;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;

public class StatisticsController
{
    private AppController mainController;
    @FXML
    private VBox flowsStatistics;
    @FXML
    private FlowStatisticsController flowsStatisticsController;
    @FXML
    private VBox stepsStatistics;
    @FXML
    private StepStatisticsController stepsStatisticsController;

    @FXML
    public void initialize() {
        if(flowsStatisticsController!=null&&stepsStatisticsController!=null)
        {
            flowsStatisticsController.setStatisticsController(this);
            stepsStatisticsController.setStatisticsController(this);
        }
    }
    public void RequestStatistics()
    {
        String finalUrl = HttpUrl
                .parse(Constants.GET_STATISTICS)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful())
                    {
                        DtoStatistics dto = new Gson().fromJson(response.body().string(), DtoStatistics.class);
                        Platform.runLater(() -> {
                            if(!dto.isEmpty())
                            {
                                getFlowStatisticsController().initFlowsStatistics(dto);
                                getStepStatisticsController().initStepsStatistics(dto);
                            }
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public FlowStatisticsController getFlowStatisticsController() {
        return flowsStatisticsController;
    }

    public StepStatisticsController getStepStatisticsController() {
        return stepsStatisticsController;
    }

    public AppController getMainController() {
        return mainController;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
