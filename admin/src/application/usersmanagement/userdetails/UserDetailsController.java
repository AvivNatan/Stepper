package application.usersmanagement.userdetails;

import application.usersmanagement.UserManagementController;
import com.google.gson.Gson;
import dto.DtoFlowsDescriptions;
import dto.DtoHeaderResponse;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import java.util.Objects;
import java.util.Set;

public class UserDetailsController {

    private UserManagementController userManagementController;

    @FXML
    private Text userName;

    @FXML
    private Text numUserFlows;

    @FXML
    private Text numUserExecutions;

    @FXML
    private ListView<String> rolesList;
    @FXML
    private ListView<String> rolesNotAssigned;
    private String chosenRoleAdd;
    private String chosenRoleRemove;

    @FXML
    private CheckBox isManager;
    @FXML
    private Button addRole;
    @FXML
    private Button removeRole;
    private final SimpleBooleanProperty addButtonDisable;
    private final SimpleBooleanProperty removeButtonDisable;


    public UserDetailsController() {
        addButtonDisable =new SimpleBooleanProperty(true);
        removeButtonDisable =new SimpleBooleanProperty(true);

    }

    @FXML
    public void initialize()
    {
        rolesNotAssigned.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if(newValue!=null)
                    {
                        chosenRoleAdd = newValue;
                        addButtonDisable.setValue(rolesList.getItems().contains(chosenRoleAdd));
                    }
                    else
                        addButtonDisable.setValue(true);
                });
        addRole.disableProperty().bind(addButtonDisable);


        rolesList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if(newValue!=null)
                    {
                        chosenRoleRemove = newValue;
                        removeButtonDisable.setValue(rolesNotAssigned.getItems().contains(chosenRoleRemove));
                    }
                    else
                        removeButtonDisable.setValue(true);
                });
        removeRole.disableProperty().bind(removeButtonDisable);
    }
    @FXML
    void addRoleButtonAction(ActionEvent event)
    {
        RequestAddRoleToUser(this.userName.getText(), chosenRoleAdd);
    }
    @FXML
    void removeRoleButtonAction(ActionEvent event) {
        RequestRemoveRoleFromUser(this.userName.getText(),chosenRoleRemove);
    }

    @FXML
    void IsManagerCheckBoxAction(ActionEvent event)
    {
        if(isManager.isSelected())
            RequestAddRoleToUser(this.userName.getText(),"All Flows");
        else
            RequestRemoveRoleFromUser(this.userName.getText(),"All Flows");
    }

    public UserManagementController getUserManagementController() {
        return userManagementController;
    }

    public void setUserManagementController(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
    }
    public void addAllRolesToList(Set<String> list)
    {
        this.rolesList.getItems().clear();
        for(String role:list)
        {
            rolesList.getItems().add(role);
        }
    }
    public void addAllRolesNotToList(Set<String> list)
    {
        this.rolesNotAssigned.getItems().clear();
        for(String role:list)
        {
            rolesNotAssigned.getItems().add(role);
        }
    }
    public void setUserName(String userName) {
        this.userName.setText(userName);
    }

    public void setNumUserFlows(String numUserFlows) {
        this.numUserFlows.setText(numUserFlows);
    }

    public void setNumUserExecutions(String numUserExecutions) {
        this.numUserExecutions.setText(numUserExecutions);
    }
    public void setIsManager(Boolean isManager)
    {
        this.isManager.setSelected(isManager);
    }

    public void RequestAddRoleToUser(String userName,String roleName) {
        String finalUrl = HttpUrl
                .parse(Constants.ADD_ROLE_TO_USER)
                .newBuilder()
                .addQueryParameter("username", userName)
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
                            rolesNotAssigned.getSelectionModel().clearSelection();
                            userManagementController.getUserListController().RequestForUserDetails(userName);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void RequestRemoveRoleFromUser(String userName,String roleName)
    {
        String finalUrl = HttpUrl
                .parse(Constants.REMOVE_ROLE_FROM_USER)
                .newBuilder()
                .addQueryParameter("username", userName)
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
                            rolesList.getSelectionModel().clearSelection();
                            userManagementController.getUserListController().RequestForUserDetails(userName);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
}
