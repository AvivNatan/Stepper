package tasks;

import application.mainappclient.executionhistory.detailsofselectdexecution.ExecutionHistoryDetailsController;
import application.mainappclient.flowexecution.FlowExecutionController;
import com.google.gson.Gson;
import dto.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
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
import java.util.TimerTask;
import java.util.UUID;
import java.util.function.Consumer;

public class CollectFlowExecutionDataTask extends TimerTask
{
        private final UUID idExecution;
        private final String userName;
        private final FlowExecutionController flowExecutionController;
        private final ExecutionHistoryDetailsController executionHistoryDetailsController;
        private final Consumer<String> flowName;
        private final Consumer<String> startTime;
        private final Consumer<String> uniqueId;
        private final Consumer<String> resultFlow;
        private final Consumer<Long> totalTimeFlow;
        private final Consumer<DtoFreeInputOutputExecution> freeInputsInfo;
        private final Consumer<DtoFreeInputOutputExecution> outputsInfo;
        private final Consumer<DtoStepExecution> stepsInfo;
        private final Consumer<String> stepConsumer;
        private final Consumer<List<String>> addContinuation;
        private final Consumer<UUID> addReExecute;
        private final Consumer<String> roleConsumer;
        private final Consumer<String> userConsumer;

    public CollectFlowExecutionDataTask(UUID execution,String userName, FlowExecutionController flowExecutionController,
                                        ExecutionHistoryDetailsController executionHistoryDetailsController,
                                        Consumer<String> roleConsumer, Consumer<String> userConsumer,
                                        Consumer<String> stepConsumer, Consumer<String> flowName,
                                        Consumer<String> startTime, Consumer<String> uniqueId,
                                        Consumer<String> resultFlow, Consumer<Long> totalTimeFlow,
                                        Consumer<DtoFreeInputOutputExecution> freeInputsInfo,
                                        Consumer<DtoFreeInputOutputExecution> outputsInfo,
                                        Consumer<DtoStepExecution> stepsInfo,
                                        Consumer<List<String>> addContinuation, Consumer<UUID> addReExecute) {
        this.idExecution = execution;
        this.userName=userName;
        this.roleConsumer=roleConsumer;
        this.userConsumer=userConsumer;
        this.flowExecutionController = flowExecutionController;
        this.executionHistoryDetailsController = executionHistoryDetailsController;
        this.stepConsumer = stepConsumer;
        this.flowName = flowName;
        this.startTime = startTime;
        this.uniqueId = uniqueId;
        this.resultFlow = resultFlow;
        this.totalTimeFlow = totalTimeFlow;
        this.freeInputsInfo = freeInputsInfo;
        this.outputsInfo = outputsInfo;
        this.stepsInfo = stepsInfo;
        this.addContinuation = addContinuation;
        this.addReExecute = addReExecute;
    }
        @Override
        public void run() {
            String finalUrl = HttpUrl
                    .parse(Constants.GET_FLOWEX_DESCRIPTION)
                    .newBuilder()
                    .addQueryParameter("id", idExecution.toString())
                    .addQueryParameter("username",this.userName)
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
                            DtoFlowPrevExecutionDescriptionResponse dto = new Gson().fromJson(response.body().string(), DtoFlowPrevExecutionDescriptionResponse.class);
                            if (executionHistoryDetailsController == null) {
                                if (dto.isRes()) {
                                    updateUi(dto.getDto());
                                    if (dto.getDto().getResultExecution() != null) {
                                        updateOutputsInputsUi(dto.getDto());
                                        updateContinuationDetailsUi(dto.getDto().getContinuationDetails());
                                        cancel();
                                    }
                                }
                            } else {
                                updateUiForHistory(dto.getDto());
                                if(dto.isReExecute())
                                    addReExecuteUi(idExecution);
                                cancel();
                            }
                        }
                    } finally {
                        response.close();
                    }
                }
            });
    }
    public void updateUi(DtoFlowPrevExecutionDescription dto) {

        Platform.runLater(
                () ->
                {
                    boolean found;
                    for (DtoStepExecution step : dto.getSteps()) {
                        if (step.getStepResult() != null) {
                            found = false;
                            for (TreeItem<GridPane> item : flowExecutionController.getTreeExecution().getRoot().getChildren()) {
                                if (Objects.equals(((Text) (item.getValue().getChildren().get(0))).getText(), step.getFinalName())) {
                                    found = true;
                                }
                            }
                            if (!found)
                                stepConsumer.accept(step.getFinalName() + "-" + step.getStepResult());
                        }
                    }
                });
        Platform.runLater(
                () ->
                {
                    if (dto.getName() != null)
                        flowName.accept(dto.getName());

                    if (dto.getUserName() != null)
                        userConsumer.accept(dto.getUserName());

                    if (dto.getRole() != null)
                        roleConsumer.accept(dto.getRole());

                    if (dto.getStartTime() != null)
                        startTime.accept(dto.getStartTime());


                    if (dto.getUniqueId() != null)
                        uniqueId.accept(dto.getUniqueId().toString());

                    if (dto.getTotalTime() != null)
                        totalTimeFlow.accept(dto.getTotalTime().toMillis());


                    if (dto.getResultExecution() != null)
                        resultFlow.accept(dto.getResultExecution().toString());


                    boolean found = false;
                    for (DtoStepExecution step : dto.getSteps()) {
                        if (step.getStepResult() != null) {
                            found = false;
                            for (TitledPane pane : flowExecutionController.getFlowExecutionDescriptionController().getStepsInfo().getPanes()) {
                                if (Objects.equals(pane.getText(), step.getFinalName())) {
                                    found = true;
                                }
                            }
                            if (!found)
                                stepsInfo.accept(step);
                        }
                    }
                }
        );
    }
    public void updateUiForHistory(DtoFlowPrevExecutionDescription dto) {
        Platform.runLater(
                () -> {
                    for (DtoStepExecution step : dto.getSteps()) {
                        stepConsumer.accept(step.getFinalName() + "-" + step.getStepResult());
                    }
                    if (dto.getName() != null)
                        flowName.accept(dto.getName());

                    if(dto.getUserName()!=null)
                        userConsumer.accept(dto.getUserName());

                    if(dto.getRole()!=null)
                        roleConsumer.accept(dto.getRole());

                    if (dto.getStartTime() != null)
                        startTime.accept(dto.getStartTime());


                    if (dto.getUniqueId() != null)
                        uniqueId.accept(dto.getUniqueId().toString());


                    if (dto.getTotalTime() != null)
                        totalTimeFlow.accept(dto.getTotalTime().toMillis());


                    if (dto.getResultExecution() != null)
                        resultFlow.accept(dto.getResultExecution().toString());


                    for (DtoFreeInputOutputExecution freeInput : dto.getFreeInputs()) {
                        if (freeInput.getContent() != null)
                            freeInputsInfo.accept(freeInput);
                    }


                    for (DtoStepExecution step : dto.getSteps()) {
                        if (step.getStepResult() != null) {
                            stepsInfo.accept(step);
                        }
                    }


                    for (DtoFreeInputOutputExecution output : dto.getOutputs()) {
                        if (output.getFinalName() != null && output.getType() != null
                                && output.getStepRelated() != null) {
                            outputsInfo.accept(output);
                        }
                    }

                });

    }
    private void updateOutputsInputsUi(DtoFlowPrevExecutionDescription dto)
    {
        Platform.runLater(
                () -> {
                    for (DtoFreeInputOutputExecution freeInput : dto.getFreeInputs()) {
                        if (freeInput.getContent() != null)
                            freeInputsInfo.accept(freeInput);
                    }
                    boolean found;
                    for(DtoFreeInputOutputExecution output:dto.getOutputs())
                    {
                        if(output.getFinalName()!=null&&output.getType()!=null
                                &&output.getStepRelated()!=null)
                        {
                            found=false;
                            for(TitledPane pane:flowExecutionController.getFlowExecutionDescriptionController().getOutputsInfo().getPanes())
                            {
                                if(Objects.equals(pane.getText(), output.getFinalName()))
                                {
                                    found=true;
                                }
                            }
                            if(!found)
                                outputsInfo.accept(output);
                        }
                    }

                }
        );
    }
    private void updateContinuationDetailsUi(List<String> names)
    {
        Platform.runLater(
                () -> {
                    if(names.size()!=0)
                        addContinuation.accept(names);
                });
    }
    private void addReExecuteUi(UUID id)
    {
        Platform.runLater(
                () -> addReExecute.accept(id));
    }
}
