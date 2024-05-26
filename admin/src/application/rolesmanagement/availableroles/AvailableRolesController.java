package application.rolesmanagement.availableroles;

import application.rolesmanagement.RoleManagementController;
import com.google.gson.Gson;
import dto.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AvailableRolesController
{
    private RoleManagementController roleManagementController;
    @FXML
    private ListView<String> rolesList;
    @FXML
    private Button newRoleButton;

    @FXML
    public void initialize() {
        rolesList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                        RequestForRoleDetails(newValue);
                });
    }
    @FXML
    void newRoleButtonAction(ActionEvent event) {
        RequestAllFlowsForNewRole();
    }

    public ListView<String> getRolesList() {
        return rolesList;
    }

    void RemoveAllRoles()
    {
        rolesList.getItems().remove(0,rolesList.getItems().size());
    }
    public void addAllRolesToList(Set<String> list)
    {
            RemoveAllRoles();
            for(String role:list)
            {
                rolesList.getItems().add(role);
            }
    }
    public void RequestAllFlowsForNewRole() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_FLOWS_NEW_ROLE)
                .newBuilder()
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
                        DtoFlowsNames result = new Gson().fromJson(response.body().string(), DtoFlowsNames.class);
                        Platform.runLater(() -> {
                            roleManagementController.addNewRoleFXML(new HashSet<>(result.getFlowsNames()));
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void RequestForRoleDetails(String roleName) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_ROLE_DETAILS)
                .newBuilder()
                .addQueryParameter("rolename", roleName)
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
                        DtoRoleDetails result = new Gson().fromJson(response.body().string(), DtoRoleDetails.class);
                        Platform.runLater(() -> {
                            roleManagementController.addRoleDetails(result);

                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public void RequestForRoleList() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_ROLE_LIST)
                .newBuilder()
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
                        DtoRoleList res = new Gson().fromJson(response.body().string(), DtoRoleList.class);
                        Platform.runLater(() -> {
                            addAllRolesToList(res.getRoles());
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public RoleManagementController getRoleManagementController() {
        return roleManagementController;
    }

    public void setRoleManagementController(RoleManagementController roleManagementController) {
        this.roleManagementController = roleManagementController;
    }
}
