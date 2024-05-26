package application.stepperheader;

import application.AppController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;

public class LoadFileController
{

    private AppController mainController;

    @FXML
    private Button loadFileButton;

    @FXML
    private Label exception;

    @FXML
    private Label filePath;

    @FXML
    void fileChooserDialogLoadFileAction(ActionEvent event) {
        exception.setVisible(false);
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(window);

        if (file == null) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.LOAD_FILE)
                .newBuilder()
                .addQueryParameter("path", file.getPath())
                .build()
                .toString();

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("xmlFile", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
                        .build();


        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    exception.setText(e.getMessage());
                    exception.setVisible(true);
                    filePath.setText("");
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Platform.runLater(() -> {
                            filePath.setText(file.getPath());
                            exception.setText(res);
                            exception.setVisible(true);
                            mainController.getRoleManagementController().RemovePrevDetailsOrNewRole();

                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
