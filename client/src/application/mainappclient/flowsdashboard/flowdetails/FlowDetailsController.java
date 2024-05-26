package application.mainappclient.flowsdashboard.flowdetails;

import application.mainappclient.AppController;
import application.mainappclient.flowsdashboard.flowdetails.freeinputsdetails.FreeInputsDescriptionController;
import application.mainappclient.flowsdashboard.flowdetails.outputsdetails.OutputsDescriptionController;
import application.mainappclient.flowsdashboard.flowdetails.stepdetails.StepMoreDescriptionController;
import application.mainappclient.flowsdashboard.flowdetails.stepsdetails.StepsDescriptionController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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

public class FlowDetailsController {


    private AppController mainController;
    @FXML
    private Text Description;

    @FXML
    private Button ExecuteFlowButton;

    @FXML
    private Label NameFlow;

    @FXML
    private Text FormalOutputs;

    @FXML
    private Label ReadOnly;
    @FXML
    private StepMoreDescriptionController stepMoreDescriptionController;
    @FXML
    private GridPane stepMoreDescription;

    @FXML
    private StepsDescriptionController stepsDescriptionController;
    @FXML
    private ListView<GridPane> stepsDescription;
    @FXML
    private FreeInputsDescriptionController freeInputsDescriptionController;
    @FXML
    private Accordion freeInputsDescription;
    @FXML
    private OutputsDescriptionController outputsDescriptionController;
    @FXML
    private Accordion outputsDescription;

    @FXML
    public void initialize() {
        if (freeInputsDescriptionController != null&& outputsDescriptionController!=null&&
                stepsDescriptionController!=null&&stepMoreDescriptionController!=null) {
            freeInputsDescriptionController.setFlowDetailsController(this);
            outputsDescriptionController.setFlowDetailsController(this);
            stepsDescriptionController.setFlowDetailsController(this);
            stepMoreDescriptionController.setFlowDetailsController(this);
        }
    }
    @FXML
    void ExecuteFlowButtonAction(ActionEvent event)
    {
        ExecuteFlow(mainController.getChosenFlowName(),null,null);
    }
    public void ExecuteFlow(String name, UUID optionalContinue,UUID optionalReExecute) {
        RequestOptionalExecutionAndFreeInputs("false",name, optionalContinue, optionalReExecute);
    }
    public void RequestOptionalExecutionAndFreeInputs(String isConCurrEx,String name, UUID optionalContinue,UUID optionalReExecute)
    {
        String finalUrl = HttpUrl
                .parse(Constants.OPTIONAL_AND_INPUTS)
                .newBuilder()
                .addQueryParameter("nameflow",name)
                .addQueryParameter("idcontinue",optionalContinue==null ? "":optionalContinue.toString())
                .addQueryParameter("iscontinuecurrex",isConCurrEx)
                .addQueryParameter("idrexecute",optionalReExecute==null ? "":optionalReExecute.toString())
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
                        DtoFreeInputsOptionalExResponse dto = new Gson().fromJson(response.body().string(), DtoFreeInputsOptionalExResponse.class);
                        Platform.runLater(() -> {
                            mainController.getFlowExecutionController().addAllFreeInputsForExecution(dto.getList());
                            mainController.openExecutionTab();
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public void addStepMoreDescription(List<DtoStepMoreDescription> list)
    {
        for(DtoStepMoreDescription dto:list)
        {
            if(Objects.equals(dto.getStepName(), stepsDescriptionController.getChosenStepForMoreInfo()))
            {
                stepMoreDescriptionController.setStepName(dto.getStepName());
                stepMoreDescriptionController.addAllInputsStep(dto.getInputs());
                stepMoreDescriptionController.addAllOutputsStep(dto.getOutputs());
                stepMoreDescription.setVisible(true);
                break;
            }
        }

    }

    public GridPane getStepMoreDescription() {
        return stepMoreDescription;
    }

    public void setDescription(String description) {
        Description.setText(description);
    }

    public void setNameFlow(String nameFlow) {
        NameFlow.setText(nameFlow);
    }

    public void setFormalOutputs(String formalOutputs) {
        FormalOutputs.setText(formalOutputs);
    }
    public void setReadOnly(String readOnly) {
        ReadOnly.setText(readOnly);
    }

    public void setFreeInputsDescription(List<DtoInputDescription> list) {
        freeInputsDescriptionController.addAllFreeInputs(list);
    }
    public void setOutputsDescription(List<DtoOutputDescription> list) {
        outputsDescriptionController.addAllOutputs(list);
    }
    public void setStepsDescription(List<DtoStepDescription> list) {
        stepsDescriptionController.addAllSteps(list);
    }

    public AppController getMainController() {
        return mainController;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
