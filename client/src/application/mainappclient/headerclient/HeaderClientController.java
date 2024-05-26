package application.mainappclient.headerclient;

import application.mainappclient.AppController;
import application.mainappclient.flowsdashboard.flowsdescription.FlowsDescriptionRefresher;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.Closeable;
import java.util.*;

import static util.Constants.REFRESH_RATE;

public class HeaderClientController implements Closeable
{
    private AppController mainController;
    @FXML
    private Text nameClient;

    @FXML
    private Text isManager;

    @FXML
    private Text roles;
    private Timer timer;
    private TimerTask headerRefresher;

    public AppController getMainController() {
        return mainController;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setNameClient(String nameClient) {
        this.nameClient.setText(nameClient);
    }

    public void setIsManager(Boolean isManager)
    {
        if(isManager)
        {
            if(!Objects.equals("No", this.isManager.getText()))
                this.isManager.setText("yes");
        }
        else
        {
            if(!Objects.equals("Yes", this.isManager.getText()))
                this.isManager.setText("No");
        }

    }

    public void setRoles(Set<String> roles)
    {
        String res=getRolesString(roles);
        if(!Objects.equals(res, this.roles.getText()))
              this.roles.setText(res);
    }
    private String getRolesString(Set<String> roles)
    {
        StringBuilder res= new StringBuilder();
        List<String> list = new ArrayList<>(roles);
        for (int i = 0; i < list.size(); i++)
            if (i == list.size() - 1)
                res.append(list.get(i));
            else
                res.append(list.get(i)).append(" ,");
        return res.toString();
    }
    public void startHeaderRefresher()
    {
        this.headerRefresher = new HeaderRefresher(mainController.getUserName(),
                this::setIsManager,this::setRoles);
        timer = new Timer();
        timer.schedule(this.headerRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        if (this.headerRefresher != null && timer != null) {
            this.headerRefresher.cancel();
            timer.cancel();
        }
    }
}
