package application.mainappclient.flowsdashboard.flowdetails.stepsdetails;

import application.mainappclient.flowsdashboard.flowdetails.FlowDetailsController;
import application.mainappclient.flowsdashboard.flowdetails.stepdetails.StepLessDescriptionController;
import dto.DtoStepDescription;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class StepsDescriptionController {

    private FlowDetailsController flowDetailsController;
    @FXML
    private ListView<GridPane> allSteps;
    @FXML
    private String chosenStepForMoreInfo;

    public void setStepLessDescription(DtoStepDescription dto, StepLessDescriptionController controller)
    {
        controller.setStepName(dto.getFinalName());
        if(dto.getIsReadOnly())
            controller.setStepReadOnly("Read only ");
        else
            controller.setStepReadOnly("Not read only");
    }

    public void setChosenStepForMoreInfo(String chosenStepForMoreInfo) {
        this.chosenStepForMoreInfo = chosenStepForMoreInfo;
    }

    public String getChosenStepForMoreInfo() {
        return chosenStepForMoreInfo;
    }

    private void RemoveAllStepsFromList()
    {

        allSteps.getItems().remove(0,allSteps.getItems().size());
    }
    public void addAllSteps(List<DtoStepDescription> list)
    {
        RemoveAllStepsFromList();
        for(DtoStepDescription dto:list)
        {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowsdashboard/flowdetails/stepdetails/StepLessDescription.fxml"));
                GridPane res=loader.load();
                StepLessDescriptionController controller=loader.getController();
                controller.setStepsDescriptionController(this);
                setStepLessDescription(dto,controller);

                allSteps.getItems().add(res);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setFlowDetailsController(FlowDetailsController flowDetailsController) {
        this.flowDetailsController = flowDetailsController;
    }

    public FlowDetailsController getFlowDetailsController() {
        return flowDetailsController;
    }
}
