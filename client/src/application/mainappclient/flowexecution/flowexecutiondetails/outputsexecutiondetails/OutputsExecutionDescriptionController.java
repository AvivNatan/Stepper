package application.mainappclient.flowexecution.flowexecutiondetails.outputsexecutiondetails;

import application.mainappclient.flowexecution.flowexecutiondetails.FlowExecutionDescriptionController;
import application.mainappclient.flowexecution.flowexecutiondetails.outputexecutiondetails.OutputExecutionDescriptionController;
import dto.DtoFreeInputOutputExecution;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import java.io.IOException;

public class OutputsExecutionDescriptionController {

    private FlowExecutionDescriptionController flowExecutionDescriptionController;
    @FXML
    private Accordion allOutputsDetails;

    public void setOutputDescription(DtoFreeInputOutputExecution dto, OutputExecutionDescriptionController controller) {
        controller.setFinalOutputName(dto.getFinalName());
        controller.setType(dto.getType());
        controller.setStepRelated(dto.getStepRelated());
    }
    void RemoveAllTitlesOutputsFromAccordion()
    {
        allOutputsDetails.getPanes().remove(0,allOutputsDetails.getPanes().size());
    }

    public void addTitleOutput(DtoFreeInputOutputExecution dto)
    {
        try
        {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/mainappclient/flowexecution/flowexecutiondetails/outputexecutiondetails/OutputExecutionDescription.fxml"));
            GridPane res=loader.load();
            OutputExecutionDescriptionController controller =loader.getController();
            controller.setOutputsExecutionDescriptionController(this);
            setOutputDescription(dto,controller);
            controller.setContent(dto.getContent(),res);
            TitledPane titledPane=new TitledPane(dto.getFinalName(),res);
            allOutputsDetails.getPanes().add(titledPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setFlowExecutionDescriptionController(FlowExecutionDescriptionController flowExecutionDescriptionController) {
        this.flowExecutionDescriptionController = flowExecutionDescriptionController;
    }

}
