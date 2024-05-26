package application.mainappclient.executionhistory.detailsofselectdexecution;

import application.mainappclient.executionhistory.FlowExecutionHistoryController;
import application.mainappclient.flowexecution.continuationdetails.ChooseContinuationController;
import application.mainappclient.flowexecution.flowexecutiondetails.FlowExecutionDescriptionController;
import application.mainappclient.flowexecution.stepexecutiondetails.StepExecutionDetailsController;
import com.google.gson.Gson;
import dto.DtoActivatedFlowDescription;
import dto.DtoStepExecutionDetails;
import dto.DtoStepExecutionDetailsResponse;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import tasks.CollectFlowExecutionDataTask;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;

import static util.Constants.REFRESH_RATE;

public class ExecutionHistoryDetailsController {

    private FlowExecutionHistoryController flowExecutionHistoryController;
    private TreeView<GridPane> treeExecution;
    private UUID chosenFlowId;
    private GridPane flowExecutionDescription;
    private FlowExecutionDescriptionController flowExecutionDescriptionController;
    private Timer timer;
    private TimerTask currentTask;
    private ChooseContinuationController chooseContinuationController;
    private HBox chooseContinuation;


    public ExecutionHistoryDetailsController() {
        this.treeExecution = new TreeView<>();
    }

    @FXML
    public void initialize() {
        treeExecution.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<GridPane>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<GridPane>> observable, TreeItem<GridPane> oldValue, TreeItem<GridPane> newValue) {

                flowExecutionHistoryController.getTreeAndDetails().getChildren().remove(flowExecutionHistoryController.getTreeAndDetails().getChildren().size() - 1);
                RequestStepExecutionDescription(((Text)(newValue.getValue().getChildren().get(0))).getText());
           }
        });
    }

    public void RequestStepExecutionDescription(String valueSelected)
    {
        String finalUrl = HttpUrl
                .parse(Constants.GET_STEP_HISTORY)
                .newBuilder()
                .addQueryParameter("id",chosenFlowId.toString())
                .addQueryParameter("valueselected",valueSelected)
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
                        DtoStepExecutionDetailsResponse dto=new Gson().fromJson(response.body().string(), DtoStepExecutionDetailsResponse.class);
                        Platform.runLater(() -> {
                            if(!dto.isFlowDetailsSelected())
                                addStepExecutionDetails(dto.getDto());
                            else
                                flowExecutionHistoryController.getTreeAndDetails().getChildren().add(flowExecutionDescription);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public HBox getChooseContinuation() {
        return chooseContinuation;
    }

    public void setChooseContinuation(HBox chooseContinuation) {
        this.chooseContinuation = chooseContinuation;
    }

    public FlowExecutionHistoryController getFlowExecutionHistoryController() {
        return flowExecutionHistoryController;
    }

    public void setFlowExecutionHistoryController(FlowExecutionHistoryController flowExecutionHistoryController) {
        this.flowExecutionHistoryController = flowExecutionHistoryController;
    }

    public UUID getChosenFlowId() {
        return chosenFlowId;
    }

    public void setChosenFlowId(UUID chosenFlowId) {
        this.chosenFlowId = chosenFlowId;
    }
    public void RequestHistoryDetails()
    {
        String finalUrl = HttpUrl
                .parse(Constants.GET_HISTORY_DESCRIPTION)
                .newBuilder()
                .addQueryParameter("id",chosenFlowId.toString())
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
                        DtoActivatedFlowDescription dto = new Gson().fromJson(response.body().string(), DtoActivatedFlowDescription.class);
                        Platform.runLater(() -> {
                            GridPane grid=new GridPane();
                            grid.getChildren().add(new Text(dto.getName()));
                            if(treeExecution.getRoot()!=null)
                            {
                                treeExecution.getRoot().getChildren().clear();
                                treeExecution.getRoot().setValue(grid);
                                treeExecution.getRoot().setExpanded(true);
                            }
                            else
                            {
                                treeExecution.setRoot(new TreeItem<>(grid));
                                treeExecution.getRoot().setExpanded(true);
                            }
                            flowExecutionHistoryController.getTreeAndDetails().getChildren().add(treeExecution);
                            addFlowExecutionInformation();
                            collectFlowExecutionDataTask(dto.getUniqueId());
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public void addFlowExecutionInformation()
    {
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/flowexecutiondetails/FlowExecutionDescription.fxml"));
            this.flowExecutionDescription=loader.load();
            this.flowExecutionDescriptionController =loader.getController();
            flowExecutionDescriptionController.setExecutionHistoryDetailsController(this);
            flowExecutionHistoryController.getTreeAndDetails().getChildren().add(flowExecutionDescription);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void addStepExecutionDetails(DtoStepExecutionDetails dto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/stepexecutiondetails/StepExecutionDetails.fxml"));
            GridPane stepDetails = loader.load();
            StepExecutionDetailsController controller = loader.getController();
            controller.setExecutionHistoryDetailsController(this);
            addStepExecutionInformation(dto, controller);
            flowExecutionHistoryController.getTreeAndDetails().getChildren().add(stepDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addStepExecutionInformation(DtoStepExecutionDetails dto, StepExecutionDetailsController controller) {
        controller.setStepName(dto.getFinalName());
        controller.setResult(dto.getResult().toString());
        controller.setStartTime(dto.getStartTime());
        controller.setEndTime(dto.getEndTime());
        controller.setTotalTime(dto.getTotalTime().toMillis());
        controller.addAllInputsToList(dto);
        controller.addAllOutputsToList(dto);
        controller.addAllLogsToList(dto);
    }
    public void collectFlowExecutionDataTask(UUID id)
    {
        this.currentTask=new CollectFlowExecutionDataTask(
                chosenFlowId,flowExecutionHistoryController.getMainController().getUserName(), null, this,
                s -> flowExecutionDescriptionController.setRoleProperty(s),
                s -> flowExecutionDescriptionController.setUserNameProperty(s),
                this::addNewStepToTree,
                s -> flowExecutionDescriptionController.setFlowNameProperty(s),
                s -> flowExecutionDescriptionController.setStartTimeFlowProperty(s),
                s -> flowExecutionDescriptionController.setUniqueIdProperty(s),
                s -> flowExecutionDescriptionController.setResultFlowProperty(s),
                aLong -> flowExecutionDescriptionController.setTotalTimeFlowProperty(aLong),
                dto -> flowExecutionDescriptionController.getFreeInputsInfoController().addTitleFreeInput(dto),
                dto -> flowExecutionDescriptionController.getOutputsInfoController().addTitleOutput(dto),
                dto -> flowExecutionDescriptionController.getStepsInfoController().addTitleStep(dto),
                list -> flowExecutionHistoryController.addContinuationDetails(list),
                uuid -> flowExecutionHistoryController.addReExecuteButtonToHistoryTab(uuid));
        timer = new Timer();
        timer.schedule(this.currentTask, REFRESH_RATE, REFRESH_RATE);
    }

    public void addContinuationInformation(List<String> names)
    {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/continuationdetails/ChooseContinuation.fxml"));
            this.chooseContinuation=loader.load();
            this.chooseContinuationController=loader.getController();
            chooseContinuationController.setExecutionHistoryDetailsController(this);
            chooseContinuationController.addAllContinuationFlowsNames(names);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void addNewStepToTree(String namePlusResult)
    {
        String[] parts = namePlusResult.split("-");
        String name=parts[0];
        String result=parts[1];
        GridPane grid=new GridPane();
        Text text=new Text(name);
        grid.add(text,0,0);
        grid.add(addImageOfResult(result),1,0);
        grid.setHgap(5);
        TreeItem<GridPane> newStep=new TreeItem<>(grid);
        treeExecution.getRoot().getChildren().add(newStep);
    }
    public ImageView addImageOfResult(String result)
    {
        Image image;
        if(Objects.equals(result, "SUCCESS"))
            image = new Image("/application/mainappclient/flowexecution/imagesofresult/success.png");
        else if (Objects.equals(result, "FAILURE"))
            image = new Image("/application/mainappclient/flowexecution/imagesofresult/fail.png");
        else
            image = new Image("/application/mainappclient/flowexecution/imagesofresult/warning.png");

        ImageView view=new ImageView(image);

        view.setFitHeight(45);
        view.setFitWidth(36);
        return view;
    }
}
