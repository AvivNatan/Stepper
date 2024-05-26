package application.mainappclient.flowexecution.freeinputupdate;

import application.mainappclient.flowexecution.FlowExecutionController;
import com.google.gson.Gson;
import dto.DtoFreeInputDescription;
import dto.DtoFreeInputOptionalExResponse;
import dto.DtoFreeInputsOptionalExResponse;
import dto.DtoInputFillingResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;

public class FreeInputsExecutionController {

    private FlowExecutionController flowExecutionController;
    @FXML
    private ListView<GridPane> listFreeInputs;

    private String chosenFreeInput;

    private SimpleBooleanProperty isFreeInputSelected;


    public FreeInputsExecutionController() {
        isFreeInputSelected=new SimpleBooleanProperty(false);
    }


    public void setIsFreeInputSelected(boolean isFreeInputSelected) {
        this.isFreeInputSelected.set(isFreeInputSelected);
    }


   public void addFreeInputFillingScreen()
   {
       RequestDataFreeInput();
//       flowExecutionController.getFillingController().getDataFreeInput(flowExecutionController.getFreeInputChosenInfo(),
//               flowExecutionController.getOptionalExecution());
   }
    public void RequestDataFreeInput()
    {
        String finalUrl = HttpUrl
                .parse(Constants.GET_TYPE_INPUT)
                .newBuilder()
                .addQueryParameter("inputname",flowExecutionController.getFreeInputsExecutionController().getChosenFreeInput())
                .addQueryParameter("username",flowExecutionController.getMainController().getUserName())
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
                        DtoInputFillingResponse dto = new Gson().fromJson(response.body().string(), DtoInputFillingResponse.class);
                        Platform.runLater(() -> {
                            flowExecutionController.getFillingController().getDataFreeInput(dto);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void setChosenFreeInput(String chosenFreeInput) {
        this.chosenFreeInput = chosenFreeInput;
    }

    public void setFreeInputExecution(DtoFreeInputOptionalExResponse dto, FreeInputExecutionController controller)
    {
        controller.setInputName(dto.getFinalName());
        controller.setNecessity(dto.getNecessity().toString());
    }
    private void RemoveAllFreeInputsExecutionFromList()
    {
        listFreeInputs.getItems().remove(0,listFreeInputs.getItems().size());
    }
    public void addAllFreeInputsExecution(List<DtoFreeInputOptionalExResponse> list)
    {
        RemoveAllFreeInputsExecutionFromList();
        for(DtoFreeInputOptionalExResponse dto:list)
        {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/freeinputupdate/FreeInputExecution.fxml"));
                GridPane res=loader.load();
                FreeInputExecutionController controller=loader.getController();
                controller.setFreeInputsExecutionController(this);
                setFreeInputExecution(dto,controller);

                listFreeInputs.getItems().add(res);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public FlowExecutionController getFlowExecutionController() {
        return flowExecutionController;
    }

    public void setFlowExecutionController(FlowExecutionController flowExecutionController) {
        this.flowExecutionController = flowExecutionController;
    }

    public String getChosenFreeInput() {
        return chosenFreeInput;
    }

    public SimpleBooleanProperty isFreeInputSelectedProperty() {
        return isFreeInputSelected;
    }
}
