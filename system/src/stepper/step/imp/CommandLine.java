package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class CommandLine extends AbstractStepDefinition implements Serializable
{
    public CommandLine()
    {
        super("Command Line", false);
        DataDefinitionDescription data;
        data=new DataDefinitionDescriptionImpl("COMMAND",
            DataNecessity.MANDATORY, "Command", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(false));
        addInput(data);
        data=new DataDefinitionDescriptionImpl("ARGUMENTS",
                DataNecessity.OPTIONAL, "Command arguments", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(false));
        addInput(data);
        addOutput(new DataDefinitionDescriptionImpl("RESULT", DataNecessity.NA,
                "Command output", DataDefinitionTypes.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        long start = System.currentTimeMillis();
        boolean error=false;
        DataInFlow data;
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        String command=context.getDataValue("COMMAND",String.class,this);
        String arguments=context.getDataValue("ARGUMENTS",String.class,this);
        String result=null;
        ProcessBuilder processBuilder;

        try {
            context.addLog(new StepsLog("About to invoke "+command+" "+arguments));

            // Create the ProcessBuilder instance
            if(arguments!=null)
                 processBuilder = new ProcessBuilder("cmd.exe", "/c", command,arguments);
            else
                 processBuilder = new ProcessBuilder("cmd.exe","/c",command);

            // Redirect the output to a StringBuilder
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output of the process
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            // Wait for the process to complete
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                context.addSummeryLine("Ended successfully");
                result=output.toString();
            } else {
                context.addSummeryLine("Error occurred");
                result="";
            }
        } catch (IOException | InterruptedException e) {}
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),result);
        context.storeDataValue("RESULT", data);
        context.updateDurationStep(start);
        return StepResult.SUCCESS;
    }
}
