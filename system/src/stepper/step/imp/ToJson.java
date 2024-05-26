package stepper.step.imp;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import stepper.dd.imp.DataDefinitionTypes;
import stepper.dd.imp.RelationDef.RelationData;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

public class ToJson extends AbstractStepDefinition implements Serializable {
    public ToJson() {
        super("To Json", true);
        DataDefinitionDescription data;
        Set<String> list;

        data=new DataDefinitionDescriptionImpl("CONTENT",
                DataNecessity.MANDATORY, "content", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(false);
        addInput(data);


        addOutput(new DataDefinitionDescriptionImpl("JSON", DataNecessity.NA,
                "Json representation", DataDefinitionTypes.JSON));
    }


    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {

        long start = System.currentTimeMillis();
        DataInFlow data;
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        String content=context.getDataValue("CONTENT",String.class,this);//input

        String json="";//output

        Gson gson = new Gson();
        try {
            Object resultJSON = gson.fromJson(content,Object.class);
            context.addSummeryLine("valid JSON-success");
            context.addLog(new StepsLog("Content is JSON string. Converting it to json…"));
            data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("JSON"),gson.toJson(content));
            context.storeDataValue("JSON",data);
            context.updateDurationStep(start);
            return StepResult.SUCCESS;
        } catch (Exception e) {
            context.addSummeryLine("not a valid JSON-failed");
            context.addLog(new StepsLog("Content is not a valid JSON representation"));
            context.updateDurationStep(start);
            return StepResult.FAILURE;
        }

    }
}


