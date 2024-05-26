package application.usersmanagement.availableusers;

import application.executionhistory.stepexecutiondetails.inputoutputstepExecution.InputOutputStepExecutionController;
import application.usersmanagement.UserManagementController;
import com.google.gson.Gson;
import dto.DtoInputOutputForStepDetails;
import dto.DtoStepExecutionDetails;
import dto.DtoUserDetails;
import dto.DtoUserList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
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

public class AvailableUsersController {

    private UserManagementController userManagementController;
    @FXML
    private ListView<String> usersList;





    @FXML
    public void initialize() {
        usersList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> RequestForUserDetails(newValue));
    }


    public ListView<String> getUsersList() {
        return usersList;
    }

    public void setUserManagementController(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
    }

    void RemoveAllUsers()
    {
        usersList.getItems().remove(0,usersList.getItems().size());
    }
    public void addAllUsersToList(List<String> list)
    {
        if(list.size()!=usersList.getItems().size())
        {
            RemoveAllUsers();
            for(String user:list)
            {
                usersList.getItems().add(user);
            }
        }
    }
    public void RequestForUserDetails(String userName) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_USER_DETAILS)
                .newBuilder()
                .addQueryParameter("username", userName)
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
                        DtoUserDetails result = new Gson().fromJson(response.body().string(), DtoUserDetails.class);
                        Platform.runLater(() -> {
                            userManagementController.addUserDetails(result);

                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }


}
