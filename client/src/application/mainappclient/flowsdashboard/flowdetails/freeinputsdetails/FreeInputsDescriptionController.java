package application.mainappclient.flowsdashboard.flowdetails.freeinputsdetails;

import application.mainappclient.flowsdashboard.flowdetails.FlowDetailsController;
import application.mainappclient.flowsdashboard.flowdetails.freeinputdetails.FreeInputDescriptionController;
import dto.DtoInputDescription;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class FreeInputsDescriptionController
{
    private FlowDetailsController flowDetailsController;
    @FXML
    private Accordion allFreeInputs;

    public void setFreeInputDescription(DtoInputDescription dto, FreeInputDescriptionController controller)
    {
        controller.setName(dto.getFinalName());
        controller.setType(dto.getType());
        controller.setStepsOfInput(dto.getStringOfSteps());
        controller.setNecessity(dto.getNecessity().toString());
    }
    void RemoveAllTitlesFromAccordionInputs()
    {
        allFreeInputs.getPanes().remove(0,allFreeInputs.getPanes().size());
    }
    public void addAllFreeInputs(List<DtoInputDescription> list)
    {
        RemoveAllTitlesFromAccordionInputs();
        for(DtoInputDescription dto:list)
        {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowsdashboard/flowdetails/freeinputdetails/FreeInputDescription.fxml"));
                GridPane res=loader.load();
                FreeInputDescriptionController controller=loader.getController();
                controller.setFreeInputsController(this);
                setFreeInputDescription(dto,controller);
                TitledPane titledPane=new TitledPane(dto.getFinalName(),res);
                allFreeInputs.getPanes().add(titledPane);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setFlowDetailsController(FlowDetailsController flowDetailsController) {
        this.flowDetailsController = flowDetailsController;
    }
}
