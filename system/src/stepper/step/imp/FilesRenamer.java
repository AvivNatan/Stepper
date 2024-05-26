package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.dd.imp.RelationDef.RelationData;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilesRenamer extends AbstractStepDefinition implements Serializable {
    public FilesRenamer() {
        super("Files Renamer", false);
        DataDefinitionDescription data;
        addInput(new DataDefinitionDescriptionImpl("FILES_TO_RENAME",
                DataNecessity.MANDATORY, "Files to rename", DataDefinitionTypes.LIST));
        data=new DataDefinitionDescriptionImpl("PREFIX",
                DataNecessity.OPTIONAL, "Add this prefix", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(false));
        addInput(data);
        data=new DataDefinitionDescriptionImpl("SUFFIX",
                DataNecessity.OPTIONAL, "Append this suffix", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(false));
        addInput(data);

        addOutput(new DataDefinitionDescriptionImpl("RENAME_RESULT", DataNecessity.NA,
                "Rename operation summary", DataDefinitionTypes.RELATION));

    }
    public void UpdateLogger(int FilesNum,String pre,String suf,StepExecutionContext context)
    {
        if(pre!=null && suf!=null)
            context.addLog(new StepsLog("About to start rename " + FilesNum +" files. Adding prefix: "+ pre +"; adding suffix: "+suf));
        else if (pre!=null)
            context.addLog(new StepsLog("About to start rename " + FilesNum +" files. Adding prefix: "+pre));
        else if(suf!=null)
            context.addLog(new StepsLog("About to start rename " + FilesNum +" files. adding suffix: "+suf));
        else
            context.addLog(new StepsLog("The files names dont need to be rename "));
    }

    public List<String> getCollOfRenameResult()
    {
        List<String> res=new ArrayList<>();
        res.add("serial number");
        res.add( "original name file");
        res.add( "name after rename");
        return res;
    }
    @Override
    public StepResult invoke(StepExecutionContext context)
    {
        long start = System.currentTimeMillis();
        DataInFlow data;
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        List<File> filesToRename = context.getDataValue("FILES_TO_RENAME", List.class,this);
        String prefix = context.getDataValue("PREFIX", String.class,this);
        String suffix = context.getDataValue("SUFFIX", String.class,this);
        RelationData RENAME_RESULT = new RelationData(getCollOfRenameResult());

        String[] dataFile = new String[3];
        boolean fail = false;
        int serial = 1;
        int FilesNum = filesToRename.size();
        if (FilesNum == 0) {
            context.addSummeryLine("Success because the number of files was zero");
            data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RENAME_RESULT"), RENAME_RESULT);
            context.storeDataValue("RENAME_RESULT",data);
            context.addLog(new StepsLog("number of files zero"));
            context.updateDurationStep(start);
            return StepResult.SUCCESS;
        }
        else
        {
            List<String> renameFile = new ArrayList<>();
            UpdateLogger(FilesNum, prefix, suffix,context);
            for (File file : filesToRename) {
                String FileName = file.getName();
                String OriginalFileName = FileName;
                if (!file.exists() || !file.isFile()) {
                    fail = true;
                    renameFile.add(OriginalFileName);
                    context.addLog(new StepsLog("Problem renaming file " + OriginalFileName));
                }
                else
                {
                    if (prefix!=null)
                        FileName = prefix+ FileName;

                    if (suffix!=null) {
                        int dotIndex = FileName.lastIndexOf(".");
                        String fileNameWithoutExtension = FileName.substring(0, dotIndex);
                        String fileExtension = FileName.substring(dotIndex);
                        FileName = fileNameWithoutExtension + suffix + fileExtension;
                    }

                    if (!file.renameTo(new File(file.getParent()+"\\"+FileName)))
                    {
                        fail = true;
                        renameFile.add(OriginalFileName);
                        context.addLog(new StepsLog("Problem renaming file " + OriginalFileName));
                    }
                    else {
                        dataFile[0] = String.valueOf(serial);
                        serial++;
                        dataFile[1] = OriginalFileName;
                        dataFile[2] = FileName;
                        RENAME_RESULT.addNewRow(dataFile);
                    }
                }
            }

            if (fail)
            {
                context.addSummeryLine("One or more files didnt rename" + renameFile);
                data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RENAME_RESULT"), RENAME_RESULT);
                context.storeDataValue("RENAME_RESULT",data);
                context.addLog(new StepsLog("problem rename some files"));
                context.updateDurationStep(start);
                return StepResult.WARNING;
            }
            else
            {
                context.addSummeryLine("Success rename all files");
                data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RENAME_RESULT"), RENAME_RESULT);
                context.storeDataValue("RENAME_RESULT",data);
                context.updateDurationStep(start);
                return StepResult.SUCCESS;
            }
        }


    }
}
