package application.mainappclient.flowsdashboard.flowdetails.outputsdetails;

import application.mainappclient.flowsdashboard.flowdetails.FlowDetailsController;
import application.mainappclient.flowsdashboard.flowdetails.outputdetails.OutputDescriptionController;
import dto.DtoOutputDescription;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class OutputsDescriptionController
{

    private FlowDetailsController flowDetailsController;
    @FXML
    private Accordion allOutputs;

    public void setOutputDescription(DtoOutputDescription dto, OutputDescriptionController controller)
    {
        controller.setName(dto.getFinalName());
        controller.setType(dto.getType());
        controller.setStepOfOutput(dto.getNameStep());
    }
    void RemoveAllTitlesFromAccordionOutputs()
    {
        allOutputs.getPanes().remove(0,allOutputs.getPanes().size());
    }
    public void addAllOutputs(List<DtoOutputDescription> list)
    {
        RemoveAllTitlesFromAccordionOutputs();
        for(DtoOutputDescription dto:list)
        {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowsdashboard/flowdetails/outputdetails/OutputDescription.fxml"));
                GridPane res=loader.load();
                OutputDescriptionController controller=loader.getController();
                controller.setOutputsController(this);
                setOutputDescription(dto,controller);
                TitledPane titledPane=new TitledPane(dto.getFinalName(),res);
                allOutputs.getPanes().add(titledPane);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setFlowDetailsController(FlowDetailsController flowDetailsController) {
        this.flowDetailsController = flowDetailsController;
    }

}
