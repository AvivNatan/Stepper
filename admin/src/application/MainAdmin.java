package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stepper.engine.EngineSystem;
import stepper.engine.EngineSystemImp;

import java.net.URL;
public class MainAdmin extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/application/App.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        AppController appController = fxmlLoader.getController();
        appController.RequestIsAdminExist();
        appController.setPrimaryStage(primaryStage);
        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}
