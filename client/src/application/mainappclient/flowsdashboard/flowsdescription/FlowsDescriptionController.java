package application.mainappclient.flowsdashboard.flowsdescription;

import application.mainappclient.AppController;
import application.mainappclient.flowsdashboard.flowdescription.FlowDescriptionController;
import dto.DtoFlowDescription;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static util.Constants.REFRESH_RATE;

public class FlowsDescriptionController implements Closeable
{

    private AppController mainController;
    private String chosenFlowName;
    @FXML
    private Accordion allFlows;
    private Timer timer;
    private TimerTask FlowsDefinitionRefresher;

    public void setChosenFlowName(String chosenFlowName) {
        this.chosenFlowName = chosenFlowName;
    }

    public String getChosenFlowName() {
        return chosenFlowName;
    }


    public void setFlowDescription(DtoFlowDescription dto,FlowDescriptionController controller)
    {
        controller.setName(dto.getName());
        controller.setDescription(dto.getDescription());
        controller.setStepsNum(dto.getStepsNum());
        controller.setFreeInputsNum(dto.getFreeInputsNum());
        controller.setContinuationsNum(dto.getContinuationsNum());
    }
    void RemoveAllTitlesFromAccordion()
    {
        allFlows.getPanes().remove(0,allFlows.getPanes().size());
    }
    public void startListRefresher()
    {
        this.FlowsDefinitionRefresher = new FlowsDescriptionRefresher(mainController.getUserName(),
                this::addAllFlows);
        timer = new Timer();
        timer.schedule(this.FlowsDefinitionRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        if (this.FlowsDefinitionRefresher != null && timer != null) {
            this.FlowsDefinitionRefresher.cancel();
            timer.cancel();
        }
    }
    private boolean checkIfTheSameListDefinition(List<DtoFlowDescription> list)
    {
        boolean res;
        if(list.size()!=allFlows.getPanes().size())
            return false;
        for(TitledPane pane:allFlows.getPanes())
        {
            res=false;
            for (DtoFlowDescription dto:list)
            {
                if(Objects.equals(pane.getText(), dto.getName()))
                {
                    res=true;
                    break;
                }
            }
            if(!res)
                return false;
        }
        return true;
    }
    public void addAllFlows(List<DtoFlowDescription> list)  {
        if(!checkIfTheSameListDefinition(list)) {
            if(mainController.getFlowExecutionHistoryController().getTreeAndDetailsController().getChosenFlowId()!=null)
            {
                mainController.getFlowExecutionHistoryController().RemoveAndUpdateForReExecution();
            }
            RemoveAllTitlesFromAccordion();
            for (DtoFlowDescription dto : list) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/mainappclient/flowsdashboard/flowdescription/FlowDescription.fxml"));
                    GridPane res = loader.load();
                    FlowDescriptionController controller = loader.getController();
                    controller.setFlowsDescriptionController(this);
                    setFlowDescription(dto, controller);
                    TitledPane titledPane = new TitledPane(dto.getName(), res);
                    titledPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mainController.closeExecutionTab();
                            mainController.updateNotVisibleDetails();
                        }
                    });
                    allFlows.getPanes().add(titledPane);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void ShowDetailsOfFlow()
    {
        mainController.updateVisibleStepMoreInfo();
        mainController.RequestForFlowsDescriptions(new Consumer<List<DtoFlowDescription>>() {
            @Override
            public void accept(List<DtoFlowDescription> dtoFlowDescriptions) {
                mainController.UpdateDetailsOfChosenFlow(dtoFlowDescriptions);
            }
        });
    }



    public AppController getMainController() {
        return mainController;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
