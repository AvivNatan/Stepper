package application.rolesmanagement.newrole;

import application.rolesmanagement.RoleManagementController;
import com.google.gson.Gson;
import dto.DtoFlowsNames;
import dto.DtoNewRoleRequest;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class NewRoleController {

    private RoleManagementController roleManagementController;
    private final List<String> chosenFlows;
    @FXML
    private ListView<CheckBox> listFlows;

    @FXML
    private TextField fillRoleName;

    @FXML
    private TextField fillDescription;

    @FXML
    private Button saveRoleButton;
    @FXML
    private Label errorNewRole;

    @FXML
    public void initialize() {
        errorNewRole.setVisible(false);
        BooleanBinding isTextNameEmpty = Bindings.createBooleanBinding(
                () -> fillRoleName.getText().isEmpty(),
                fillRoleName.textProperty());
        BooleanBinding isTextDescriptionEmpty = Bindings.createBooleanBinding(
                () -> fillDescription.getText().isEmpty(),
                fillDescription.textProperty());

        saveRoleButton.disableProperty().bind(Bindings.or(Bindings.or(isTextNameEmpty, isTextDescriptionEmpty),errorNewRole.visibleProperty()));
    }


    public NewRoleController() {
        chosenFlows=new ArrayList<>();
    }

    @FXML
    void saveRoleButtonAction(ActionEvent event) {
        RequestNewRole();
    }

    public RoleManagementController getRoleManagementController() {
        return roleManagementController;
    }

    public void setRoleManagementController(RoleManagementController roleManagementController) {
        this.roleManagementController = roleManagementController;
    }

    public void addAllFlowsToList(Set<String> flows)
    {
        for(String str:flows)
        {
            addNewFlowToList(str);
        }
    }

    private void addNewFlowToList(String flowName) {
        CheckBox box = new CheckBox(flowName);
        box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (box.isSelected())
                    chosenFlows.add(flowName);
                else
                    chosenFlows.remove(flowName);

            }
        });
        listFlows.getItems().add(box);
    }

    public void RequestNewRole() {
        Gson gson = new Gson();

        DtoNewRoleRequest dto = new DtoNewRoleRequest(fillRoleName.getText(), fillDescription.getText(), chosenFlows);
        String resultJSON = gson.toJson(dto);

        String finalUrl = HttpUrl
                .parse(Constants.ADD_NEW_ROLE)
                .newBuilder()
                .build()
                .toString();
        RequestBody body=RequestBody.create(resultJSON.getBytes());
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorNewRole.setText(e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String result=new Gson().fromJson(response.body().string(), String.class);
                        Platform.runLater(() -> {
                            errorNewRole.setText(result);
                            onFinishNewRole();
                            roleManagementController.getRolesListController().getRolesList().getItems().add(fillRoleName.getText());
                        });
                    }
                } finally {
                    response.close();
                }
            }

        });
    }
    public void onFinishNewRole()
    {
        errorNewRole.setVisible(true);
        fillDescription.setDisable(true);
        fillRoleName.setDisable(true);
        listFlows.setDisable(true);
    }
}







