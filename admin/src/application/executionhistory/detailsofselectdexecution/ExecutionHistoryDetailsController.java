package application.executionhistory.detailsofselectdexecution;

import application.executionhistory.FlowExecutionHistoryController;
import application.executionhistory.flowexecutiondetails.FlowExecutionDescriptionController;
import application.executionhistory.stepexecutiondetails.StepExecutionDetailsController;
import com.google.gson.Gson;
import dto.DtoActivatedFlowDescription;
import dto.DtoStepExecutionDetails;
import dto.DtoStepExecutionDetailsResponse;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
import stepper.flow.excution.FlowExecution;
import tasks.CollectFlowExecutionDataTask;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ExecutionHistoryDetailsController
{

    private FlowExecutionHistoryController flowExecutionHistoryController;
    private TreeView<GridPane> treeExecution;
    private UUID chosenFlowId;
    private GridPane flowExecutionDescription;
    private FlowExecutionDescriptionController flowExecutionDescriptionController;
    private Task<Boolean> currentTask;

    public ExecutionHistoryDetailsController() {
        this.treeExecution = new TreeView<>();
    }

    @FXML
    public void initialize() {
        treeExecution.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<GridPane>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<GridPane>> observable, TreeItem<GridPane> oldValue, TreeItem<GridPane> newValue) {
                 flowExecutionHistoryController.getTreeAndDetails().getChildren().remove(1,flowExecutionHistoryController.getTreeAndDetails().getChildren().size());
                RequestStepExecutionDescription(((Text)(newValue.getValue().getChildren().get(0))).getText());
            }
        });
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

    public void addFlowExecutionInformation()
    {
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/executionhistory/flowexecutiondetails/FlowExecutionDescription.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/executionhistory/stepexecutiondetails/StepExecutionDetails.fxml"));
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
    public void RequestStepExecutionDescription(String valueSelected) {
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
                            collectFlowExecutionDataTask();
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void collectFlowExecutionDataTask()
    {
        this.currentTask=new CollectFlowExecutionDataTask(
                chosenFlowId, this,
                s -> flowExecutionDescriptionController.setUserNameProperty(s),
                s -> flowExecutionDescriptionController.setRoleProperty(s),
                this::addNewStepToTree,
                s -> flowExecutionDescriptionController.setFlowNameProperty(s),
                s -> flowExecutionDescriptionController.setStartTimeFlowProperty(s),
                s -> flowExecutionDescriptionController.setUniqueIdProperty(s),
                s -> flowExecutionDescriptionController.setResultFlowProperty(s),
                aLong -> flowExecutionDescriptionController.setTotalTimeFlowProperty(aLong),
                dto -> flowExecutionDescriptionController.getFreeInputsInfoController().addTitleFreeInput(dto),
                dto -> flowExecutionDescriptionController.getOutputsInfoController().addTitleOutput(dto),
                dto -> flowExecutionDescriptionController.getStepsInfoController().addTitleStep(dto));
        new Thread(currentTask).start();
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
            image = new Image("/application/executionhistory/imagesofresult/success.png");
        else if (Objects.equals(result, "FAILURE"))
            image = new Image("/application/executionhistory/imagesofresult/fail.png");
        else
            image = new Image("/application/executionhistory/imagesofresult/warning.png");

        ImageView view=new ImageView(image);

        view.setFitHeight(45);
        view.setFitWidth(36);
        return view;
    }
}
