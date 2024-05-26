package application.statistics.flowstatistics;

import application.statistics.StatisticsController;
import dto.DtoFlowDescription;
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

public class FlowStatisticsController {

    private StatisticsController statisticsController;
    @FXML
    private TableView<DtoFlowStepStatistics> flowsStatistics;
    private ObservableList<DtoFlowStepStatistics> flowList;
    @FXML
    private TableColumn<DtoFlowStepStatistics, String> flowName;

    @FXML
    private TableColumn<DtoFlowStepStatistics, Integer> NumExecutions;

    @FXML
    private TableColumn<DtoFlowStepStatistics, Long> timeExecutions;
    @FXML
    private BarChart<String, Integer> flowStatisticsBar;

    @FXML
    public void initialize() {
        flowName.setCellValueFactory(new PropertyValueFactory<DtoFlowStepStatistics,String>("name"));
        NumExecutions.setCellValueFactory(new PropertyValueFactory<DtoFlowStepStatistics,Integer>("executionNum"));
        timeExecutions.setCellValueFactory(new PropertyValueFactory<DtoFlowStepStatistics,Long>("avg"));

    }

    public int getSizeFlowList() {
        return flowList.size();
    }

    public StatisticsController getStatisticsController() {
        return statisticsController;
    }

    public void setStatisticsController(StatisticsController statisticsController) {
        this.statisticsController = statisticsController;
    }
    public void initFlowsStatistics(DtoStatistics dto)
    {
        this.flowList= FXCollections.observableArrayList(
                dto.getAllFlowsStatistics());
        flowsStatistics.setItems(flowList);
        flowStatisticsBar.getData().clear();
        flowStatisticsBar.setAnimated(true);
        XYChart.Series series = new XYChart.Series();

        for (Map.Entry<String, Integer> stat : dto.getMapOfFlowsStatistics().entrySet()) {
            series.getData().add(new XYChart.Data(stat.getKey(), stat.getValue()));
        }
        flowStatisticsBar.getData().add(series);
        flowStatisticsBar.setAnimated(false);
    }
}
