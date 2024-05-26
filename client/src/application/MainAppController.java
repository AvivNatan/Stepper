package application;

import application.loginclient.LoginClientController;
import application.mainappclient.AppController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

public class MainAppController
{
    private AppController appClientController;
    private BorderPane appClient;

    private GridPane loginClient;
    private LoginClientController loginClientController;
    @FXML
    private AnchorPane mainPanel;

    @FXML
    public void initialize()
    {
        // prepare components

        loadLoginPage();
        loadMainAppPage();
    }

    private void setAnchorSizes(Parent pane,Double bottom,Double top,Double left,Double right)
    {
        AnchorPane.setBottomAnchor(pane, bottom);
        AnchorPane.setTopAnchor(pane, top);
        AnchorPane.setLeftAnchor(pane, left);
        AnchorPane.setRightAnchor(pane, right);
    }
    private void setMainPanelTo(Parent pane,Double bottom,Double top,Double left,Double right) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        setAnchorSizes(pane,bottom,top,left,right);
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource("/application/loginclient/login.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginClient = fxmlLoader.load();
            loginClientController = fxmlLoader.getController();
            loginClientController.setMainAppController(this);
            setMainPanelTo(loginClient,200.0,100.0,200.0,200.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMainAppPage()
    {
        URL loginPageUrl = getClass().getResource("/application/mainappclient/App.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            appClient = fxmlLoader.load();
            appClientController = fxmlLoader.getController();
            appClientController.setMainAppController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void switchToMainApp() {
        setMainPanelTo(appClient,2.0,2.0,2.0,2.0);
        appClientController.setActive();
        appClientController.updateVisibleDefinitions();
    }
    public void updateUserName(String userName) {
        appClientController.updateUserNameInHeaderClient(userName);
    }
}
