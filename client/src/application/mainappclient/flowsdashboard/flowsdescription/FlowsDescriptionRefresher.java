package application.mainappclient.flowsdashboard.flowsdescription;

import com.google.gson.Gson;
import dto.DtoFlowDescription;
import dto.DtoFlowsDescriptions;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

public class FlowsDescriptionRefresher extends TimerTask
{
    private final Consumer<List<DtoFlowDescription>> flowsDefinitionsConsumer;
    private final String userName;

    public FlowsDescriptionRefresher(String userName, Consumer<List<DtoFlowDescription>> flowsDefinitionsConsumer) {
        this.flowsDefinitionsConsumer = flowsDefinitionsConsumer;
        this.userName=userName;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_DEFINITION)
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
                        DtoFlowsDescriptions listFlows = new Gson().fromJson(response.body().string(), DtoFlowsDescriptions.class);
                        List<DtoFlowDescription> listFlowsDetails=listFlows.getFlows();
                        Platform.runLater(() -> {
                                flowsDefinitionsConsumer.accept(listFlowsDetails);
                        });
                    }}finally {
                    response.close();
                }
            }
        });
    }
}
