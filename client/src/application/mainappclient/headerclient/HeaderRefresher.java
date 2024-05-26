package application.mainappclient.headerclient;

import com.google.gson.Gson;
import dto.DtoFlowDescription;
import dto.DtoFlowsDescriptions;
import dto.DtoHeaderResponse;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.Consumer;

public class HeaderRefresher extends TimerTask
{
    private final Consumer<Boolean> updateIsManager;
    private final Consumer<Set<String>> updateRoles;
    private final String userName;

    public HeaderRefresher(String userName, Consumer<Boolean> updateIsManager,Consumer<Set<String>> updateRoles) {
        this.updateIsManager = updateIsManager;
        this.updateRoles=updateRoles;
        this.userName=userName;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.HEADER_REFRESHER)
                .newBuilder()
                .addQueryParameter("username",userName)
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
                        DtoHeaderResponse dtoHeaderResponse = new Gson().fromJson(response.body().string(), DtoHeaderResponse.class);
                        Platform.runLater(() -> {
                            updateRoles.accept(dtoHeaderResponse.getRoles());
                            updateIsManager.accept(dtoHeaderResponse.getManager());
                        });
                    }}finally {
                    response.close();
                }
            }
        });
    }
}
