package tasks;
import application.executionhistory.detailsofselectdexecution.ExecutionHistoryDetailsController;
import com.google.gson.Gson;
import dto.DtoFlowPrevExecutionDescription;
import dto.DtoFlowPrevExecutionDescriptionResponse;
import dto.DtoFreeInputOutputExecution;
import dto.DtoStepExecution;
import javafx.application.Platform;
import javafx.concurrent.Task;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

public class CollectFlowExecutionDataTask extends Task<Boolean> {

    private final UUID idExecution;
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
    private final  Consumer<String> userNameConsumer;
    private final Consumer<String> roleConsumer;

    public CollectFlowExecutionDataTask(UUID execution,
                                        ExecutionHistoryDetailsController executionHistoryDetailsController,
                                        Consumer<String> userNameConsumer,Consumer<String> roleConsumer,
                                        Consumer<String> stepConsumer, Consumer<String> flowName,
                                        Consumer<String> startTime, Consumer<String> uniqueId,
                                        Consumer<String> resultFlow, Consumer<Long> totalTimeFlow,
                                        Consumer<DtoFreeInputOutputExecution> freeInputsInfo,
                                        Consumer<DtoFreeInputOutputExecution> outputsInfo,
                                        Consumer<DtoStepExecution> stepsInfo) {
        this.idExecution = execution;
        this.roleConsumer=roleConsumer;
        this.userNameConsumer=userNameConsumer;
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
    }

    @Override
    protected Boolean call() throws Exception {
        RequestFlowExecutionDescription();
        return true;
    }

    public void RequestFlowExecutionDescription() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_FLOWEX_DESCRIPTION)
                .newBuilder()
                .addQueryParameter("id", idExecution.toString())
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

                        DtoFlowPrevExecutionDescription dto = new Gson().fromJson(response.body().string(), DtoFlowPrevExecutionDescription.class);
                        updateUiForHistory(dto);
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public void updateUiForHistory(DtoFlowPrevExecutionDescription dto) {
        Platform.runLater(
                () -> {
                    for (DtoStepExecution step : dto.getSteps())
                        stepConsumer.accept(step.getFinalName() + "-" + step.getStepResult());


                    if (dto.getName() != null)
                        flowName.accept(dto.getName());

                    if(dto.getUserName()!=null)
                        userNameConsumer.accept(dto.getUserName());

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
                        if (step.getStepResult() != null)
                            stepsInfo.accept(step);
                    }


                    for (DtoFreeInputOutputExecution output : dto.getOutputs()) {
                        if (output.getFinalName() != null && output.getType() != null
                                && output.getStepRelated() != null) {
                            outputsInfo.accept(output);
                        }
                    }

                });

    }
}
