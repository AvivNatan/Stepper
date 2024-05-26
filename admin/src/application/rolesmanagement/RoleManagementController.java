package application.rolesmanagement;

import application.AppController;
import application.rolesmanagement.availableroles.AvailableRolesController;
import application.rolesmanagement.newrole.NewRoleController;
import application.rolesmanagement.roledetails.RoleDetailsController;
import dto.DtoRoleDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Set;


public class RoleManagementController
{
    private AppController mainController;
    @FXML
    private VBox rolesList;
    @FXML
    private AvailableRolesController rolesListController;
    private RoleDetailsController roleDetailsController;
    private VBox roleDetails;
    private VBox newRole;
    private NewRoleController newRoleController;


//    private UserListRefresher userListRefresher;
//    private Timer timer;

    @FXML
    public void initialize() {
        if(rolesListController!=null)
            rolesListController.setRoleManagementController(this);
    }

    public VBox getRoleDetails() {
        return roleDetails;
    }

    private void setRoleDetails(DtoRoleDetails roleDetails)
    {
        this.roleDetailsController.setRoleName(roleDetails.getRoleName());
        this.roleDetailsController.setRoleDescription(roleDetails.getRoleDescription());
        this.roleDetailsController.addAllUsersToList(roleDetails.getUsers());
        this.roleDetailsController.addAllFlowsNotToList(roleDetails.getFlowsNotAssigned());
        this.roleDetailsController.addAllFlowsToList(roleDetails.getFlows());
    }
    public void addRoleDetails(DtoRoleDetails roleDetails)
    {
        RemovePrevDetailsOrNewRole();
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/rolesmanagement/roledetails/RoleDetails.fxml"));
            this.roleDetails=loader.load();
            this.roleDetailsController=loader.getController();
            this.roleDetailsController.setRoleManagementController(this);
            setRoleDetails(roleDetails);
            mainController.getRoleManagement().getChildren().add(this.roleDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void RemovePrevDetailsOrNewRole()
    {
        mainController.getRoleManagement().getChildren().remove(this.roleDetails);
        mainController.getRoleManagement().getChildren().remove(this.newRole);
    }
    public void addNewRoleFXML(Set<String> flows)
    {
        RemovePrevDetailsOrNewRole();
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/rolesmanagement/newrole/NewRole.fxml"));
            this.newRole=loader.load();
            this.newRoleController=loader.getController();
            this.newRoleController.setRoleManagementController(this);
            setNewRole(flows);
            mainController.getRoleManagement().getChildren().add(this.newRole);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void setNewRole(Set<String> flows)
    {
        this.newRoleController.addAllFlowsToList(flows);
    }
    public AppController getMainController() {
        return mainController;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public AvailableRolesController getRolesListController() {
        return rolesListController;
    }

    public void setRolesListController(AvailableRolesController rolesListController) {
        this.rolesListController = rolesListController;
    }
}
