package application.usersmanagement;

import application.AppController;
import application.usersmanagement.availableusers.AvailableUsersController;
import application.usersmanagement.userdetails.UserDetailsController;
import dto.DtoUserDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tasks.UserListRefresher;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;

import static utils.Constants.REFRESH_RATE;

public class UserManagementController implements Closeable
{
    private AppController mainController;
    @FXML
    private VBox userList;
    @FXML
    private AvailableUsersController userListController;

    private UserDetailsController userDetailsController;
    private VBox userDetails;

    private UserListRefresher userListRefresher;
    private Timer timer;

    @FXML
    public void initialize() {
       if(userListController!=null)
           userListController.setUserManagementController(this);
    }
    public void removeChoice()
    {
        userListController.getUsersList().getSelectionModel().clearSelection();
    }

    public AppController getMainController() {
        return mainController;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public AvailableUsersController getUserListController() {
        return userListController;
    }

    public application.usersmanagement.userdetails.UserDetailsController getUserDetailsController() {
        return userDetailsController;
    }

    public VBox getUserDetails() {
        return userDetails;
    }

    public void startUserListRefresher()
    {
        this.userListRefresher = new UserListRefresher(
                strings -> userListController.addAllUsersToList(strings));
        timer = new Timer();
        timer.schedule(this.userListRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    @Override
    public void close() {
        if (this.userListRefresher != null && timer != null) {
            this.userListRefresher.cancel();
            timer.cancel();
        }
    }
    private void setUserDetails(DtoUserDetails userDetails)
    {
        userDetailsController.addAllRolesToList(userDetails.getRoles());
        userDetailsController.setUserName(userDetails.getUserName());
        userDetailsController.setNumUserFlows(((Integer)userDetails.getNumUserFlows()).toString());
        userDetailsController.setNumUserExecutions(((Integer)userDetails.getNumUserExecutions()).toString());
        userDetailsController.setIsManager(userDetails.isManager());
        userDetailsController.addAllRolesNotToList(userDetails.getRolesNotAssigned());
    }
    public void addUserDetails(DtoUserDetails userDetails)
    {
        mainController.getUserManagement().getChildren().remove(this.userDetails);
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/usersmanagement/userdetails/UserDetails.fxml"));
            this.userDetails=loader.load();
            this.userDetailsController=loader.getController();
            this.userDetailsController.setUserManagementController(this);
            setUserDetails(userDetails);
            mainController.getUserManagement().getChildren().add(this.userDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
