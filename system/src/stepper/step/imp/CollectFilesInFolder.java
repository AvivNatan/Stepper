package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.nio.file.FileSystemAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class CollectFilesInFolder extends AbstractStepDefinition implements Serializable {
    public CollectFilesInFolder()
    {
        super("Collect Files In Folder", true);
        DataDefinitionDescription data;
        data=new DataDefinitionDescriptionImpl("FOLDER_NAME",
                DataNecessity.MANDATORY, "Folder name to scan", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(true));
        addInput(data);
        data=new DataDefinitionDescriptionImpl("FILTER",
                DataNecessity.OPTIONAL, "Filter only these files", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(false));
        addInput(data);
        addOutput(new DataDefinitionDescriptionImpl("FILES_LIST", DataNecessity.NA,
                "Files list", DataDefinitionTypes.LIST));
        addOutput(new DataDefinitionDescriptionImpl("TOTAL_FOUND", DataNecessity.NA,
                "Total files found", DataDefinitionTypes.NUMBER));
    }
    public String[] GetFilesNamesAfterFilter(File folder,String filter,StepExecutionContext context)
    {
        String[] files;
        if(filter!=null)
        {
            context.addLog(new StepsLog("Reading folder "+folder.getPath()+ " content with filter "+ filter));
            files = folder.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(filter);
                }
            });

            return files;
        }
        context.addLog(new StepsLog("Reading folder "+folder.getPath()+ " content without filter "));
        return folder.list();
    }
      @Override
      public StepResult invoke(StepExecutionContext context)
      {
          DataInFlow data;
          StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                  getStepUsageDescription(context.getFinalStepName());
          long start = System.currentTimeMillis();
          String folderName = context.getDataValue("FOLDER_NAME", String.class,this);
          String filter = context.getDataValue("FILTER", String.class,this);
          File folder=new File(folderName);
          if (!folder.exists() || !folder.isDirectory())
          {
              context.addLog(new StepsLog("path dose`nt exist or not a directory"));
              context.addSummeryLine("The path dose`nt exist or not a directory");
              context.updateDurationStep(start);
              return StepResult.FAILURE;
          }
          else
          {
              String[] filesAfterFilter = GetFilesNamesAfterFilter(folder,filter,context);
              if (folder.list().length == 0 || filesAfterFilter.length == 0) {

                  data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("FILES_LIST"),new ArrayList<>());
                  context.storeDataValue("FILES_LIST",data);//empty list
                  data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("TOTAL_FOUND"),new Integer(0));
                  context.storeDataValue("TOTAL_FOUND",data);//zero total files
                  context.addSummeryLine("The folder is empty");
                  context.addLog(new StepsLog("folder is empty"));
                  context.updateDurationStep(start);
                  return StepResult.WARNING;
              }
              else
              {
                  List<File> FILES_LIST = new ArrayList<>();
                  for (String path : filesAfterFilter)
                  {
                      String file=folder.getPath()+"\\"+path;
                      FILES_LIST.add(new File(file));
                  }
                  data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("FILES_LIST"),FILES_LIST);
                  context.storeDataValue("FILES_LIST",data);
                  data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("TOTAL_FOUND"),new Integer(filesAfterFilter.length));
                  context.storeDataValue("TOTAL_FOUND",data);
                  context.addSummeryLine("Success collecting files");
                  context.addLog(new StepsLog("Found "+filesAfterFilter.length+" files in folder matching the filter"));
              }
              context.updateDurationStep(start);
              return StepResult.SUCCESS;
          }
      }
}
