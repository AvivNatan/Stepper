package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
public class MainClient extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
       // EngineSystem system=new EngineSystemImp();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/application/MainApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        MainAppController appMainController = fxmlLoader.getController();
       // appController.setSystem(system);
        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}
