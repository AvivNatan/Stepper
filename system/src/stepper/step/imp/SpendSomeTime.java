package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDescriptionImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.Serializable;

public class SpendSomeTime extends AbstractStepDefinition implements Serializable {
    public SpendSomeTime()
    {
        super("Spend Some Time", true);

        addInput(new DataDefinitionDescriptionImpl("TIME_TO_SPEND",
                DataNecessity.MANDATORY, "Total sleeping time (sec)", DataDefinitionTypes.NUMBER));

    }

    @Override
    public StepResult invoke(StepExecutionContext context)
    {
        long start = System.currentTimeMillis();

        int timeSleep = context.getDataValue("TIME_TO_SPEND", Integer.class,this);
        try {
            if (timeSleep <= 0)
            {
                context.addSummeryLine("Failed because a negative number or zero was received");
                context.updateDurationStep(start);
                return StepResult.FAILURE;
            }
            else
            {
                context.addLog(new StepsLog("About to sleep for " + timeSleep +" seconds…"));
                Thread.sleep(1000*timeSleep);
                context.addLog(new StepsLog("Done sleeping…"));
                context.addSummeryLine("Successful sleeping");
                context.updateDurationStep(start);
                return StepResult.SUCCESS;
            }
        }
        catch (InterruptedException e) {
            return StepResult.FAILURE;
        }
    }

}
