package application.mainappclient.flowexecution.flowexecutiondetails.freeinputsexecutiondetails;

import application.mainappclient.flowexecution.flowexecutiondetails.FlowExecutionDescriptionController;
import application.mainappclient.flowexecution.flowexecutiondetails.freeinputexecutiondetails.FreeInputExecutionDetailsController;
import dto.DtoFreeInputOutputExecution;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class FreeInputsExecutionDetailsController {

    private FlowExecutionDescriptionController flowExecutionDescriptionController;
    @FXML
    private Accordion allFreeInputsDetails;

    public void setFreeInputDescription(DtoFreeInputOutputExecution dto, FreeInputExecutionDetailsController controller) {
        controller.setFinalInputName(dto.getFinalName());
        controller.setType(dto.getType());
        controller.setNecessity(dto.getNecessity().toString());
        controller.setContent(dto.getContent());
    }
    void RemoveAllTitlesFreeInputsFromAccordion()
    {
        allFreeInputsDetails.getPanes().remove(0,allFreeInputsDetails.getPanes().size());
    }

    public void addTitleFreeInput(DtoFreeInputOutputExecution dto)
    {
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/flowexecutiondetails/freeinputexecutiondetails/FreeInputExecutionDetails.fxml"));
            GridPane res=loader.load();
            FreeInputExecutionDetailsController controller =loader.getController();
            controller.setFreeInputsExecutionDetailsController(this);
            setFreeInputDescription(dto,controller);
            TitledPane titledPane=new TitledPane(dto.getFinalName(),res);
            allFreeInputsDetails.getPanes().add(titledPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setFlowExecutionDescriptionController(FlowExecutionDescriptionController flowExecutionDescriptionController) {
        this.flowExecutionDescriptionController = flowExecutionDescriptionController;
    }

}
