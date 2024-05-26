package stepper.flow.excution.runner;

import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.FlowExecutionResult;
import stepper.flow.excution.StepExecutionData;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepExecutionContextImp;
import stepper.step.api.StepResult;

import java.io.IOException;

public class FlowExecutor implements Runnable{

    private StepExecutionContext context;

    public FlowExecutor(StepExecutionContext context) {
        this.context = context;
    }

    @Override
    public void run()
    {
        context.getFlowExecution().setFlowExecuteNow(true);
        //System.out.println("Starting execution of flow: " + context.getFlowExecution().getFlowDefinition().getName() + " [ID: " + context.getFlowExecution().getUniqueId() + "]");
        long start = System.currentTimeMillis();
        // populate context with all free inputs (mandatory & optional) that were given from the user
        for(DataInFlow data: context.getFlowExecution().getFreeInputs().values())
        {
            if(data.getContent()!=null)
            {
                context.storeDataValue(data.getDataUsageDefinition().getOriginalName(),data);
            }
        }
        // (typically stored on top of the flow execution object)
        // start actual execution
        boolean warning=false;
        int sizeSteps=context.getFlowExecution().getFlowDefinition().getFlowSteps().size();
        FlowExecutionResult FlowResult= FlowExecutionResult.SUCCESS;
        for (int i = 0; i < sizeSteps; i++)
        {
            StepUsageDescription stepUsageDescription = context.getFlowExecution().getFlowDefinition().getFlowSteps().get(i);
            StepExecutionContext stepExecutionContext=new StepExecutionContextImp(context.getFlowExecution());
            stepExecutionContext.getFlowExecution().addStepExecutionData(new StepExecutionData(stepUsageDescription.getFinalStepName(), stepUsageDescription.getStepDefinition(),stepUsageDescription));
            stepExecutionContext.setFinalStepName(stepUsageDescription.getFinalStepName());
           // System.out.println("Starting to execute step: " + stepUsageDescription.getFinalStepName());
            StepResult stepResult = null;
            try {
                stepResult = stepUsageDescription.getStepDefinition().invoke(stepExecutionContext);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stepExecutionContext.addResult(stepResult);
            stepExecutionContext.getFlowExecution().addStepLogs(stepExecutionContext.getStepLogs());
            stepExecutionContext.getFlowExecution().addSummeryLineOfStep(stepExecutionContext.getStepSummeryLine());
           // System.out.println("Done executing step: " + stepUsageDescription.getFinalStepName() + ". Result: " + stepResult);
            if(!stepUsageDescription.skipIfFail() &&stepResult==StepResult.FAILURE)//all flow stop and fail
            {
                FlowResult=FlowExecutionResult.FAILURE;
                break;
            }
            if((i==sizeSteps-1) && stepResult==StepResult.FAILURE)//last one ends with fail
                FlowResult=FlowExecutionResult.FAILURE;


            if(stepResult==StepResult.WARNING)//at least one
                warning=true;
            context.getFlowExecution().setStepExecutionEndTime(stepUsageDescription.getFinalStepName());
        }

        if(warning&&FlowResult==FlowExecutionResult.SUCCESS)//check if flow had step that ends with warning
            FlowResult=FlowExecutionResult.WARNING;

        context.getFlowExecution().setFlowExecutionResult(FlowResult);
        System.out.println("End execution of flow " +
                context.getFlowExecution().getFlowDefinition().getName() +
                " [ID: " + context.getFlowExecution().getUniqueId() + "]. Status: " +
                context.getFlowExecution().getFlowExecutionResult());
        context.getFlowExecution().setTotalTime(start);
        context.getFlowExecution().setFlowExecuteNow(false);
    }

}
