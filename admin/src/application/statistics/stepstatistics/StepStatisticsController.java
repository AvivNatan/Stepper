package application.statistics.stepstatistics;

import application.statistics.StatisticsController;
import dto.DtoFlowStepStatistics;
import dto.DtoStatistics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;

public class StepStatisticsController {

    private StatisticsController statisticsController;
    @FXML
    private TableView<DtoFlowStepStatistics> stepsStatistics;
    private ObservableList<DtoFlowStepStatistics> stepList;
    @FXML
    private TableColumn<DtoFlowStepStatistics, String> stepName;

    @FXML
    private TableColumn<DtoFlowStepStatistics, Integer> NumExecutions;

    @FXML
    private TableColumn<DtoFlowStepStatistics, Long> timeExecutions;
    @FXML
    private BarChart<String, Integer> stepStatisticsBar;


    @FXML
    public void initialize() {
        stepName.setCellValueFactory(new PropertyValueFactory<DtoFlowStepStatistics,String>("name"));
        NumExecutions.setCellValueFactory(new PropertyValueFactory<DtoFlowStepStatistics,Integer>("executionNum"));
        timeExecutions.setCellValueFactory(new PropertyValueFactory<DtoFlowStepStatistics,Long>("avg"));

    }
    @FXML
    public StatisticsController getStatisticsController() {
        return statisticsController;
    }

    public void setStatisticsController(StatisticsController statisticsController) {
        this.statisticsController = statisticsController;
    }
    public void initStepsStatistics(DtoStatistics dto)
    {
            this.stepList= FXCollections.observableArrayList(
                    dto.getAllStepsStatistics());
            stepsStatistics.setItems(stepList);
            stepStatisticsBar.getData().clear();
            stepStatisticsBar.setAnimated(true);
            stepStatisticsBar.getData().clear();
            stepStatisticsBar.setAnimated(true);

            XYChart.Series series = new XYChart.Series();

            for (Map.Entry<String, Integer> stat : dto.getMapOfStepsStatistics().entrySet()) {
            series.getData().add(new XYChart.Data(stat.getKey(), stat.getValue()));
        }
            stepStatisticsBar.getData().add(series);
            stepStatisticsBar.setAnimated(false);
    }
}
