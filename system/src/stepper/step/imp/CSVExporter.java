package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.dd.imp.RelationDef.RelationData;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CSVExporter extends AbstractStepDefinition implements Serializable
{
    public CSVExporter() {
        super("CSV Exporter", true);
        addInput(new DataDefinitionDescriptionImpl("SOURCE",
                DataNecessity.MANDATORY, "Source data", DataDefinitionTypes.RELATION));
        addOutput(new DataDefinitionDescriptionImpl("RESULT", DataNecessity.NA,
                "CSV export result", DataDefinitionTypes.STRING));
    }

    private String addHeadline(List<String> cols)
    {
        String res=cols.get(0);
        for(int i=1;i<cols.size();i++)
            res=res+","+cols.get(i);
        return res;
    }
    @Override
    public StepResult invoke(StepExecutionContext context) {

        long start = System.currentTimeMillis();
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        DataInFlow data;
        RelationData source=context.getDataValue("SOURCE",RelationData.class,this);
        String Result=addHeadline(source.getColumns())+"\n";//all cols of relation
        List<String> line;
        int sizeRows=source.getRows().size();
        if(sizeRows!=0)
        {
            context.addLog(new StepsLog("About to process " +sizeRows+ " lines of data"));
            for(int i=0;i<sizeRows;i++)
            {
                line=source.getRowDataByColumnsOrder(i);
                for (int j = 0; j < line.size(); j++) {
                    if(j==0)
                    {
                        Result=Result+line.get(j);
                    }
                    else
                        Result=Result+", "+line.get(j);
                }
                Result=Result+"\n";
            }
            data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),Result);
            context.storeDataValue("RESULT",data);
            context.addSummeryLine("Success exporter to CSV");
            context.updateDurationStep(start);
            return StepResult.SUCCESS;
        }
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),Result);
        context.storeDataValue("RESULT",data);
        context.addLog(new StepsLog("The table is empty"));
        context.addSummeryLine("The table is empty");
        context.updateDurationStep(start);
        return StepResult.WARNING;
    }
}
