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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PropertiesExporter extends AbstractStepDefinition implements Serializable
{
    public PropertiesExporter() {
        super("Properties Exporter", true);
        addInput(new DataDefinitionDescriptionImpl("SOURCE",
                DataNecessity.MANDATORY, "Source data", DataDefinitionTypes.RELATION));

        addOutput(new DataDefinitionDescriptionImpl("RESULT", DataNecessity.NA,
                "Properties export result", DataDefinitionTypes.STRING));
    }
    public String RelationDataToString(RelationData data)
    {
        String res="";
        for (int i = 0; i < data.getNumberOfRows(); i++)
        {
            res=res+"row-"+(i+1)+".";
            List<String> row=data.getRowDataByColumnsOrder(i);
            for (int j = 0; j < row.size(); j++) {
                if(row.size()-1==j)
                    res=res+data.getColumns().get(j)+"="+row.get(j);
                else
                    res=res+data.getColumns().get(j)+"="+row.get(j)+",";
            }
            if(data.getNumberOfRows()-1!=i)
                res=res+"\n";
        }
        return res;

    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        long start = System.currentTimeMillis();
        DataInFlow data;
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        RelationData source=context.getDataValue("SOURCE",RelationData.class,this);//input
        String Result="";//output
        int sizeRows=source.getRows().size();
        int totalPro=sizeRows*source.getColumns().size();
        if(sizeRows!=0)
        {
            context.addLog(new StepsLog("About to process "+sizeRows+" lines of data"));
            Result=RelationDataToString(source);
            data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"), Result);
            context.storeDataValue("RESULT",data);
            context.addLog(new StepsLog("Extracted total of "+totalPro));
            context.addSummeryLine("Success exporter to properties");
            context.updateDurationStep(start);
            return StepResult.SUCCESS;
        }
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"), Result);
        context.storeDataValue("RESULT",data);
        context.addLog(new StepsLog("The table is empty"));
        context.addSummeryLine("The table is empty");
        context.updateDurationStep(start);
        return StepResult.WARNING;
    }

}

