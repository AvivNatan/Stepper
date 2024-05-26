package application.mainappclient.flowexecution.freeinputupdate;

import application.mainappclient.flowexecution.FlowExecutionController;
import com.google.gson.Gson;
import dto.DtoFreeInputDescription;
import dto.DtoFreeInputsOptionalExResponse;
import dto.DtoInputFillingResponse;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.dd.imp.EnumeratorDef.EnumeratorData;
import stepper.flow.excution.FlowExecution;
import stepper.xmlexceptions.NumberFormatException;
import util.Constants;
import util.http.HttpClientUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class FreeInputFillingController {

    private FlowExecutionController flowExecutionController;
    @FXML
    private Label dataType;
    @FXML
    private VBox fillInfo;

    public FlowExecutionController getFlowExecutionController() {
        return flowExecutionController;
    }

    public VBox getFillInfo() {
        return fillInfo;
    }

    public void setFlowExecutionController(FlowExecutionController flowExecutionController) {
        this.flowExecutionController = flowExecutionController;
    }

    public void removeAllFillingInfo()
    {
        fillInfo.getChildren().remove( 0,fillInfo.getChildren().size());
    }

    public void RequestToUpdateFreeInput(String content)
    {
        String finalUrl = HttpUrl
                .parse(Constants.UPDATE_INPUT_CONTENT)
                .newBuilder()
                .addQueryParameter("username", flowExecutionController.getMainController().getUserName())
                .addQueryParameter("content",content)
                .addQueryParameter("inputname",flowExecutionController.getFreeInputsExecutionController().getChosenFreeInput())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                flowExecutionController.RequestIsAllMandatoryInputsReceived();
                response.close();
            }
        });
    }

    public void getDataFreeInput(DtoInputFillingResponse dto)
    {
        removeAllFillingInfo();
        fillInfo.setSpacing(15);
        dataType.setText(dto.getType());
        Button setInfo=new Button("Click To Update");
        Object optionalObject=dto.getObj();
        switch (dto.getType())
        {
            case "String":
                BooleanBinding isTextEmpty;
                if ((Boolean) dto.getMoreInfo())
                {
                    Text textFile=new Text();
                    GridPane gridPane=new GridPane();
                    TextFlow textFlow=new TextFlow(textFile);
                    Button button = new Button("Load Path");
                    gridPane.add(button,0,1);
                    gridPane.add(textFlow,1,1);
                    gridPane.setPadding(new Insets(30,10,10,10));
                    gridPane.setHgap(20);
                    isTextEmpty= Bindings.createBooleanBinding(
                            () -> textFile.getText().isEmpty(),
                            textFile.textProperty());
                    if(optionalObject!=null)
                    {
                        textFile.setText((String)optionalObject);
                        BooleanBinding isTheSame=Bindings.createBooleanBinding(
                                () -> Objects.equals(textFile.getText(), (String) optionalObject),textFile.textProperty()
                        );
                        setInfo.disableProperty().bind(Bindings.or(isTextEmpty,isTheSame));
                    }
                    else
                    {
                        setInfo.disableProperty().bind(isTextEmpty);
                    }
                   button.setOnAction(event ->
                   {
                           JFileChooser fileChooser = new JFileChooser(".");
                           fileChooser.setMultiSelectionEnabled(true);
                           fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                           int ret = fileChooser.showOpenDialog(null);

                           if (ret == JFileChooser.APPROVE_OPTION) {
                               File[] selectedFiles = fileChooser.getSelectedFiles();

                               if (selectedFiles != null && selectedFiles.length > 0) {
                                   StringBuilder pathBuilder = new StringBuilder();
                                   for (File selectedFile : selectedFiles) {
                                       pathBuilder.append(selectedFile.getPath()).append(", ");
                                   }
                                   pathBuilder.delete(pathBuilder.length() - 2, pathBuilder.length());  // Remove the trailing comma and space
                                  textFile.setText(pathBuilder.toString());
                               }
                           }
                       });
                    setInfo.setOnAction(event -> {
                        RequestToUpdateFreeInput(textFile.getText());
                        flowExecutionController.getFreeInputsExecutionController().setIsFreeInputSelected(false);

                    });
                    fillInfo.getChildren().add(gridPane);
                }
                else
                {
                    TextArea text=new TextArea();
                    isTextEmpty= Bindings.createBooleanBinding(
                            () -> text.getText().isEmpty(),
                            text.textProperty());
                    if(optionalObject!=null)
                    {
                        text.setText((String)optionalObject);
                        BooleanBinding isTheSame=Bindings.createBooleanBinding(
                                () -> Objects.equals(text.getText(), (String) optionalObject),text.textProperty()
                        );
                        setInfo.disableProperty().bind(Bindings.or(isTextEmpty,isTheSame));
                    }
                    else
                    {
                        setInfo.disableProperty().bind(isTextEmpty);
                    }
                    setInfo.setOnAction(event -> {
                        RequestToUpdateFreeInput(text.getText());
                        flowExecutionController.getFreeInputsExecutionController().setIsFreeInputSelected(false);

                    });
                    fillInfo.getChildren().add(text);
                }
                fillInfo.getChildren().add(setInfo);
                break;
            case "EnumeratorData":
                ChoiceBox<String> choiceBox=new ChoiceBox<>();
                choiceBox.getItems().addAll((List<String>)dto.getMoreInfo());
                BooleanBinding isChoiceEmpty= Bindings.createBooleanBinding(
                        () -> choiceBox.getValue()==null,
                        choiceBox.valueProperty());

                String chosen=((String)((LinkedHashMap)optionalObject).get("chosenValue"));

                if(chosen!=null)
                {
                    choiceBox.setValue(chosen);
                    BooleanBinding isTheSame=Bindings.createBooleanBinding(
                            () -> Objects.equals(choiceBox.getValue(),chosen),choiceBox.valueProperty()
                    );
                    setInfo.disableProperty().bind(Bindings.or(isChoiceEmpty,isTheSame));
                }
                else
                {
                    setInfo.disableProperty().bind(isChoiceEmpty);
                }
                fillInfo.getChildren().add(choiceBox);
                fillInfo.getChildren().add(setInfo);
                setInfo.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        RequestToUpdateFreeInput(choiceBox.getValue());
                        flowExecutionController.getFreeInputsExecutionController().setIsFreeInputSelected(false);
                    }
                });
                break;
            case "Integer":
                TextArea textInt=new TextArea();
                BooleanBinding isTextEmptyInt= Bindings.createBooleanBinding(
                        () -> textInt.getText().isEmpty(),
                        textInt.textProperty());
                BooleanBinding isNumber=Bindings.createBooleanBinding(
                        () -> {
                            try {
                                if(!textInt.getText().isEmpty()) {
                                    int value = Integer.parseInt(textInt.getText());
                                }
                                return true;
                            }
                            catch (NumberFormatException exception)
                            {
                                return false;
                            }
                        },textInt.textProperty());
                TextFlow exception=new TextFlow(new Text("Not a Number"));
                exception.visibleProperty().bind(Bindings.and(isTextEmptyInt.not(),isNumber.not()));
                if(optionalObject!=null)
                {
                    Integer num=((Double)optionalObject).intValue();
                    textInt.setText(num.toString());
                    BooleanBinding isTheSame=Bindings.createBooleanBinding(
                            () -> Objects.equals(textInt.getText(), num.toString()),textInt.textProperty()
                    );
                    setInfo.disableProperty().bind(Bindings.or(isTheSame,Bindings.or(isTextEmptyInt,isNumber.not())));
                }
                else
                {
                    setInfo.disableProperty().bind(Bindings.or(isTextEmptyInt,isNumber.not()));
                }
                fillInfo.getChildren().add(textInt);
                fillInfo.getChildren().add(setInfo);
                fillInfo.getChildren().add(exception);
                setInfo.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        RequestToUpdateFreeInput(textInt.getText());
                        flowExecutionController.getFreeInputsExecutionController().setIsFreeInputSelected(false);
                    }
                });
                break;
        }
    }
}