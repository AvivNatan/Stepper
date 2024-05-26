package tasks;

import com.google.gson.Gson;
import dto.DtoUserList;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UserListRefresher extends TimerTask {
    private final Consumer<List<String>> addUsers;

    public UserListRefresher(Consumer<List<String>> addUsers) {
        this.addUsers = addUsers;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_USER_LIST)
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
                        DtoUserList res = new Gson().fromJson(response.body().string(), DtoUserList.class);
                        Platform.runLater(() -> {
                            addUsers.accept(res.getUsers());
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

}
