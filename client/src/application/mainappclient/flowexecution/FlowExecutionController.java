package application.mainappclient.flowexecution;

import application.mainappclient.AppController;
import application.mainappclient.flowexecution.continuationdetails.ChooseContinuationController;
import application.mainappclient.flowexecution.flowexecutiondetails.FlowExecutionDescriptionController;
import application.mainappclient.flowexecution.freeinputupdate.FreeInputFillingController;
import application.mainappclient.flowexecution.freeinputupdate.FreeInputsExecutionController;
import application.mainappclient.flowexecution.stepexecutiondetails.StepExecutionDetailsController;
import com.google.gson.Gson;
import dto.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

public class FlowExecutionController
{
    private AppController mainController;
    @FXML
    private VBox freeInputsExecution;
    @FXML
    private FreeInputsExecutionController freeInputsExecutionController;
    @FXML
    private FreeInputFillingController fillingController;
    @FXML
    private VBox filling;
    @FXML
    private Button excutionButton;

    private SimpleBooleanProperty isExecutable;
    private GridPane flowExecutionDescription;
    private FlowExecutionDescriptionController flowExecutionDescriptionController;

    private Timer timer;
    private TimerTask currentTask;
    private TreeView<GridPane> treeExecution;
    @FXML private HBox treeAndInformationHbox;
    private ChooseContinuationController chooseContinuationController;
    private HBox chooseContinuation;


    public FlowExecutionController() {
        this.treeExecution=new TreeView<>();
        this.isExecutable=new SimpleBooleanProperty(false);
    }


    @FXML
    public void initialize() {
        if(freeInputsExecutionController!=null&&fillingController!=null)
        {
            freeInputsExecutionController.setFlowExecutionController(this);
            fillingController.setFlowExecutionController(this);
        }
        excutionButton.visibleProperty().bind(isExecutable);
        treeExecution.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<GridPane>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<GridPane>> observable, TreeItem<GridPane> oldValue, TreeItem<GridPane> newValue)
            {
                treeAndInformationHbox.getChildren().remove(treeAndInformationHbox.getChildren().size()-1);
                RequestStepExecutionDescription(((Text)(newValue.getValue().getChildren().get(0))).getText());
            }
        });
    }
    public void RequestIsAllMandatoryInputsReceived()
    {
        String finalUrl = HttpUrl
                .parse(Constants.IS_MANDATORY_INPUTS)
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
                        boolean result = new Gson().fromJson(response.body().string(), boolean.class);
                        Platform.runLater(() -> {
                            isExecutable.setValue(result);
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

    public SimpleBooleanProperty isExecutableProperty() {
        return isExecutable;
    }

    public FlowExecutionDescriptionController getFlowExecutionDescriptionController() {
        return flowExecutionDescriptionController;
    }

    public TreeView<GridPane> getTreeExecution() {
        return treeExecution;
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
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
    public void addFlowExecutionInformation()
    {
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/flowexecutiondetails/FlowExecutionDescription.fxml"));
            this.flowExecutionDescription=loader.load();
            this.flowExecutionDescriptionController =loader.getController();
            flowExecutionDescriptionController.setFlowExecutionController(this);
            treeAndInformationHbox.getChildren().add(flowExecutionDescription);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void  addStepExecutionInformation(DtoStepExecutionDetails dto, StepExecutionDetailsController controller)
    {
        controller.setStepName(dto.getFinalName());
        controller.setResult(dto.getResult().toString());
        controller.setStartTime(dto.getStartTime());
        controller.setEndTime(dto.getEndTime());
        controller.setTotalTime(dto.getTotalTime().toMillis());
        controller.addAllInputsToList(dto);
        controller.addAllOutputsToList(dto);
        controller.addAllLogsToList(dto);
    }
    public void addStepExecutionInformation(DtoStepExecutionDetails dto)
    {
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/stepexecutiondetails/StepExecutionDetails.fxml"));
            GridPane stepDetails=loader.load();
            StepExecutionDetailsController controller =loader.getController();
            controller.setFlowExecutionController(this);
            addStepExecutionInformation(dto,controller);
            treeAndInformationHbox.getChildren().add(stepDetails);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void UpdateToNewExecution()
    {
        this.treeAndInformationHbox.getChildren().remove(0,this.treeAndInformationHbox.getChildren().size());
        RequestIsAllMandatoryInputsReceived();
        if(this.chooseContinuationController!=null)
            mainController.getFlowExecution().getChildren().remove(this.chooseContinuation);
    }
    public void RequestStepExecutionDescription(String valueSelected) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_STEPEX_DESCRIPTION)
                .newBuilder()
                .addQueryParameter("username", mainController.getUserName())
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
                        Platform.runLater(() ->
                        {
                            if(!dto.isFlowDetailsSelected())
                                addStepExecutionInformation(dto.getDto());
                            else
                                treeAndInformationHbox.getChildren().add(flowExecutionDescription);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void RequestExecutionFlow()
    {
        String finalUrl = HttpUrl
                .parse(Constants.EXECUTE_FLOW)
                .newBuilder()
                .addQueryParameter("username",mainController.getUserName())
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
                    if (response.isSuccessful()) {
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
                            treeAndInformationHbox.getChildren().add(treeExecution);
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

    @FXML
    public void ExecutionButtonAction(ActionEvent event) {
        RequestExecutionFlow();
    }
    public void addContinuationInformation(List<String> names)
    {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/continuationdetails/ChooseContinuation.fxml"));
            this.chooseContinuation=loader.load();
            this.chooseContinuationController=loader.getController();
            chooseContinuationController.setFlowExecutionController(this);
            chooseContinuationController.addAllContinuationFlowsNames(names);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void collectFlowExecutionDataTask(UUID id)
    {
        this.currentTask=new CollectFlowExecutionDataTask(
                id,mainController.getUserName(), this, null,
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
                list -> mainController.addContinuationDetails(list)
                , uuid -> mainController.addReExecuteButtonToExecutionTab(uuid));
        timer = new Timer();
        timer.schedule(this.currentTask, REFRESH_RATE, REFRESH_RATE);
    }
    public FreeInputsExecutionController getFreeInputsExecutionController() {
        return freeInputsExecutionController;
    }

    public VBox getFilling() {
        return filling;
    }

    public FreeInputFillingController getFillingController() {
        return fillingController;
    }

    public void addAllFreeInputsForExecution(List<DtoFreeInputOptionalExResponse> list)
    {
        freeInputsExecutionController.addAllFreeInputsExecution(list);
    }

    public AppController getMainController() {
        return mainController;
    }

}
