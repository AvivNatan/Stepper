package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.dd.imp.RelationDef.RelationData;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDescriptionImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilesContentExtractor extends AbstractStepDefinition implements Serializable
{

    public FilesContentExtractor() {
        super("Files Content Extractor", true);

        addInput(new DataDefinitionDescriptionImpl("FILES_LIST",
                DataNecessity.MANDATORY, "Files to extract", DataDefinitionTypes.LIST));
        addInput(new DataDefinitionDescriptionImpl("LINE",
                DataNecessity.MANDATORY, "Line number to extract", DataDefinitionTypes.NUMBER));

        addOutput(new DataDefinitionDescriptionImpl("DATA", DataNecessity.NA,
                "Data extraction", DataDefinitionTypes.RELATION));

    }

    public List<String> getCollOfData() {
        List<String> res = new ArrayList<>();
        res.add("serial number");
        res.add("original name file");
        res.add("text");
        return res;
    }
    private String ReadLineFromFile(int num,BufferedReader br) throws IOException {
        String line;
        int lineNum = 1;
        while ((line = br.readLine()) != null && lineNum <= num) {
            if (lineNum == num) {
                // process the content of the desired line here
                return line;
            }
            lineNum++;
        }
        return null;
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        long start = System.currentTimeMillis();
        DataInFlow data;
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        List<File> filesList = context.getDataValue("FILES_LIST", List.class,this);//input
        int Line = context.getDataValue("LINE", Integer.class,this);//input
        RelationData Data = new RelationData(getCollOfData());//output
        String[] dataFile = new String[3];
        int serial = 1;
        int FilesNum = filesList.size();
        if (FilesNum == 0) {
            context.addSummeryLine("Success because the number of files was zero");
            context.addLog(new StepsLog("number of files was zero"));
            data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("DATA"),Data);
            context.storeDataValue("DATA", data);
            context.updateDurationStep(start);
            return StepResult.SUCCESS;
        }
        else
        {
            for (File file : filesList) {
                context.addLog(new StepsLog("About to start work on file "+file.getName()));
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file.getPath())))) {
                    String text = ReadLineFromFile(Line, in);
                    if (text == null)
                    {
                        context.addLog(new StepsLog("Problem extracting line number "+Line+" from file "+file.getName()));
                        Data.addNewRow(String.valueOf(serial), file.getName(),"No Such Line");
                    }
                    else
                        Data.addNewRow(String.valueOf(serial), file.getName(),text);

                    serial++;
                }
                catch (FileNotFoundException e)
                {
                    Data.addNewRow(String.valueOf(serial), file.getName(), "File not found");
                    serial++;
                }
                catch (IOException e) {}

            }

        }
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("DATA"),Data);
        context.storeDataValue("DATA",data);
        context.addSummeryLine("Success extract content lines ");
        context.updateDurationStep(start);
        return StepResult.SUCCESS;
    }
}

