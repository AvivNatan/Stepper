package application.rolesmanagement.roledetails;

import application.rolesmanagement.RoleManagementController;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class RoleDetailsController
{
    private RoleManagementController roleManagementController;
    @FXML
    private Text roleName;

    @FXML
    private Text roleDescription;
    @FXML
    private ListView<String> roleUsers;

    @FXML
    private ListView<String> flowsList;
    private String chosenFlowAdd;


    @FXML
    private Button removeFlow;

    @FXML
    private ListView<String> flowsNotAssigned;
    private String chosenFlowRemove;


    @FXML
    private Button addFlow;
    private final SimpleBooleanProperty addButtonDisable;
    private final SimpleBooleanProperty removeButtonDisable;


    public RoleDetailsController() {
        addButtonDisable =new SimpleBooleanProperty(true);
        removeButtonDisable =new SimpleBooleanProperty(true);

    }
    @FXML
    public void initialize() {
        flowsNotAssigned.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if(roleName.getText().equals("All Flows")||roleName.getText().equals("Read Only Flows"))
                    {
                        addButtonDisable.setValue(true);
                    }
                    else
                    {
                        if(newValue!=null)
                        {
                            chosenFlowAdd = newValue;
                            addButtonDisable.setValue(flowsList.getItems().contains(chosenFlowAdd));
                        }
                        else
                            addButtonDisable.setValue(true);
                    }
                });
        addFlow.disableProperty().bind(addButtonDisable);


        flowsList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if(roleName.getText().equals("All Flows")||roleName.getText().equals("Read Only Flows"))
                    {
                        removeButtonDisable.setValue(true);
                    }
                    else
                    {
                        if(newValue!=null)
                        {
                            chosenFlowRemove = newValue;
                            removeButtonDisable.setValue(flowsNotAssigned.getItems().contains(chosenFlowRemove));
                        }
                        else
                            removeButtonDisable.setValue(true);
                    }
                });
        removeFlow.disableProperty().bind(removeButtonDisable);
    }

    public RoleManagementController getRoleManagementController() {
        return roleManagementController;
    }

    @FXML
    void addFlowButtonAction(ActionEvent event) {
        RequestAddFlowToRole(chosenFlowAdd,this.roleName.getText());
    }

    @FXML
    void removeFlowButtonAction(ActionEvent event) {
        RequestRemoveFlowFromRole(chosenFlowRemove, this.roleName.getText());
    }
    public void addAllUsersToList(Set<String> list)
    {
        roleUsers.getItems().clear();
        for(String role:list)
        {
            roleUsers.getItems().add(role);
        }
    }
    public void addAllFlowsToList(Set<String> list)
    {
        flowsList.getItems().clear();
        for(String flow:list)
        {
            flowsList.getItems().add(flow);
        }
    }
    public void addAllFlowsNotToList(Set<String> list)
    {
        flowsNotAssigned.getItems().clear();
        for(String flow:list)
        {
            flowsNotAssigned.getItems().add(flow);
        }
    }
    public void setRoleManagementController(RoleManagementController roleManagementController) {
        this.roleManagementController = roleManagementController;
    }

    public void setRoleName(String roleName) {
        this.roleName.setText(roleName);
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription.setText(roleDescription);
    }
    public void RequestAddFlowToRole(String flowName,String roleName) {
        String finalUrl = HttpUrl
                .parse(Constants.ADD_FLOW_TO_ROLE)
                .newBuilder()
                .addQueryParameter("flowname", flowName)
                .addQueryParameter("rolename",roleName)
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
                        Platform.runLater(() -> {
                            flowsNotAssigned.getSelectionModel().clearSelection();
                            roleManagementController.getRolesListController().RequestForRoleDetails(roleName);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void RequestRemoveFlowFromRole(String flowName,String roleName)
    {
        String finalUrl = HttpUrl
                .parse(Constants.REMOVE_FLOW_FROM_ROLE)
                .newBuilder()
                .addQueryParameter("flowname", flowName)
                .addQueryParameter("rolename",roleName)
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
                        Platform.runLater(() -> {
                            flowsList.getSelectionModel().clearSelection();
                            roleManagementController.getRolesListController().RequestForRoleDetails(roleName);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
}
