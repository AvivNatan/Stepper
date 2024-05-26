package application.executionhistory.flowexecutiondetails.stepsexecutiondetails;

import application.executionhistory.flowexecutiondetails.FlowExecutionDescriptionController;
import application.executionhistory.flowexecutiondetails.stepexecutiondetails.StepExecutionDescriptionController;
import dto.DtoStepExecution;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StepsExecutionDescriptionController {

    private FlowExecutionDescriptionController flowExecutionDescriptionController;
    @FXML
    private Accordion allSteps;


    public void setStepDescription(DtoStepExecution dto, StepExecutionDescriptionController controller) {
        controller.setStepName(dto.getFinalName());
        controller.setResult(dto.getStepResult().toString());
        controller.setTotalTime(dto.getTotalTime().toMillis());
        controller.setSummeryLine(dto.getSummeryLine());
        if(dto.getLogs().size()==0)
            controller.addRowNoLogsToGrid();
        for (int i = 0; i < dto.getLogs().size(); i++) {
            controller.addRowLogToGrid(dto.getLogs().get(i),i);
        }

    }
    void RemoveAllTitlesStepsFromAccordion()
    {
        allSteps.getPanes().remove(0,allSteps.getPanes().size());
    }

    public void addTitleStep(DtoStepExecution dto)
    {
            try
            {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/executionhistory/flowexecutiondetails/stepexecutiondetails/StepExecutionDescription.fxml"));
                VBox res=loader.load();
                StepExecutionDescriptionController controller =loader.getController();
                controller.setStepsExecutionDescriptionController(this);
                setStepDescription(dto,controller);
                TitledPane titledPane=new TitledPane(dto.getFinalName(),res);
                allSteps.getPanes().add(titledPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    public FlowExecutionDescriptionController getFlowExecutionDescriptionController() {
        return flowExecutionDescriptionController;
    }

    public void setFlowExecutionDescriptionController(FlowExecutionDescriptionController flowExecutionDescriptionController) {
        this.flowExecutionDescriptionController = flowExecutionDescriptionController;
    }
}
