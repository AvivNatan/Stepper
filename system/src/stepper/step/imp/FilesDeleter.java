package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.flow.definition.api.DataUsageDescription;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDescriptionImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesDeleter extends AbstractStepDefinition implements Serializable
{
    public FilesDeleter()
    {
        super("Files Deleter", false);
        addInput(new DataDefinitionDescriptionImpl("FILES_LIST",
                DataNecessity.MANDATORY, "Files to delete", DataDefinitionTypes.LIST));

        addOutput(new DataDefinitionDescriptionImpl("DELETED_LIST", DataNecessity.NA,
                "Files failed to be deleted", DataDefinitionTypes.LIST));
        addOutput(new DataDefinitionDescriptionImpl("DELETION_STATS", DataNecessity.NA,
                "Deletion summary results", DataDefinitionTypes.MAPPING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context)
    {
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        DataInFlow data;
        long start = System.currentTimeMillis();
        List<File> filesList = context.getDataValue("FILES_LIST", List.class,this);//input
        List<String> DELETED_LIST = new ArrayList<>();//output 1
        Map<Integer,Integer> DELETION_STATS=new HashMap<>();//output 2

        int countSuc = 0;
        int countFai = 0;
        int sizeList =filesList.size();

        if (sizeList==0)
        {
            context.addSummeryLine("Success because the number of files was zero");
            DELETION_STATS.put(new Integer(0),new Integer(0));
        }
        else
        {
            context.addLog(new StepsLog("About to start delete "+ sizeList  + " files"));
            for (File file : filesList)
            {
                if (file.isFile()&&file.delete())
                    countSuc++;
                else {
                    countFai++;
                    DELETED_LIST.add(file.getPath());
                    context.addLog(new StepsLog("Failed to delete file " + file.getName()));
                }
            }

            if(countSuc==0)
            {
                context.addSummeryLine(countSuc+"files was deleted"+"\n"+countFai+"files wasn't deleted");
                context.addLog(new StepsLog("not all files was deleted"));
                data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("DELETION_STATS"), DELETION_STATS);
                context.storeDataValue("DELETION_STATS",data);
                data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("DELETED_LIST"),DELETED_LIST);
                context.storeDataValue("DELETED_LIST",data);
                context.updateDurationStep(start);
                return StepResult.WARNING;
            }
            context.addSummeryLine("Success delete all files");
            DELETION_STATS.put(new Integer(countSuc),new Integer(countFai));
        }
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("DELETION_STATS"), DELETION_STATS);
        context.storeDataValue("DELETION_STATS",data);
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("DELETED_LIST"),DELETED_LIST);
        context.storeDataValue("DELETED_LIST",data);
        context.updateDurationStep(start);
        return StepResult.SUCCESS;
    }
}
