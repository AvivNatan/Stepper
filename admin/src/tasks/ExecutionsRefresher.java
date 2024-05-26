package tasks;

import com.google.gson.Gson;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ExecutionsRefresher extends TimerTask {

    private Consumer<Integer> numExecutions;

    public ExecutionsRefresher(Consumer<Integer> numExecutions) {
        this.numExecutions=numExecutions;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.UPDATE_EXECUTIONS)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                try {
                    if (response.isSuccessful()) {
                        Integer res = new Gson().fromJson(response.body().string(), Integer.class);
                        Platform.runLater(() -> {
                            numExecutions.accept(res);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
}

