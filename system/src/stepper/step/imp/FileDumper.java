package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class FileDumper extends AbstractStepDefinition implements Serializable
{
    public FileDumper() {
        super("File Dumper", true);
        DataDefinitionDescription data;
        data=new DataDefinitionDescriptionImpl("CONTENT",
                DataNecessity.MANDATORY, "Content", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(false));
        addInput(data);
        data=new DataDefinitionDescriptionImpl("FILE_NAME",
                DataNecessity.MANDATORY, "Target file path", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(true));
        addInput(data);
        addOutput(new DataDefinitionDescriptionImpl("RESULT", DataNecessity.NA,
                "File Creation Result", DataDefinitionTypes.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {

        long start = System.currentTimeMillis();
        DataInFlow data;
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        String content=context.getDataValue("CONTENT",String.class,this);//input
        String fileName=context.getDataValue("FILE_NAME",String.class,this);//input
        String Result="";//output
        context.addLog(new StepsLog("About to create file named "+fileName));
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
            fileWriter.close();
        }
        catch (IOException e)
        {
            context.addSummeryLine("The file failed creation");
            context.addLog(new StepsLog("failed creation"));
            Result="Failed because cant create the file";
            data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),Result);
            context.storeDataValue("RESULT",data);
            context.updateDurationStep(start);
            return StepResult.FAILURE;
        }
        if(content=="")
        {
            Result="Success create empty file.";
            context.addSummeryLine("The file created but empty");
            context.addLog(new StepsLog("created empty"));
            data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),Result);
            context.storeDataValue("RESULT",data);
            context.updateDurationStep(start);
            return StepResult.WARNING;
        }
        Result="Success file created.";
        context.addSummeryLine("The file created successfully");
        context.addLog(new StepsLog("file created"));
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),Result);
        context.storeDataValue("RESULT",data);
        context.updateDurationStep(start);
        return StepResult.SUCCESS;

    }
}
