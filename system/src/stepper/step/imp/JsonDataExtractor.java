package stepper.step.imp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import stepper.dd.imp.DataDefinitionTypes;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

public class JsonDataExtractor extends AbstractStepDefinition implements Serializable {
    public JsonDataExtractor() {
        super("Json Data Extractor", true);
        DataDefinitionDescription data;

        data = new DataDefinitionDescriptionImpl("JSON",
                DataNecessity.MANDATORY, "Json source", DataDefinitionTypes.JSON);
        data.setMoreInfoOfData(false);
        addInput(data);

        data = new DataDefinitionDescriptionImpl("JSON_PATH",
                DataNecessity.MANDATORY, "data", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(false);
        addInput(data);


        addOutput(new DataDefinitionDescriptionImpl("VALUE", DataNecessity.NA,
                "Data value", DataDefinitionTypes.STRING));

    }


    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
    long start = System.currentTimeMillis();
    DataInFlow data;
    StepUsageDescription usage = context.getFlowExecution().getFlowDefinition().
            getStepUsageDescription(context.getFinalStepName());
    String json = context.getDataValue("JSON", String.class, this);//input
    String JSON_PATH = context.getDataValue("JSON_PATH", String.class, this);//input
//    Gson gson = new Gson();
//    JsonElement JSON= gson.toJsonTree(json);

        String VALUE = "";
        StepResult stepResult;
        try {
            String[] dataArray = JSON_PATH.split("\\|");
            StringBuilder value = new StringBuilder();

            for (String path : dataArray) {
                value.append(JsonPath.read(json, path.trim()).toString()).append(", ");
            }
            context.addLog(new StepsLog("Extracting data " + JSON_PATH + ". Value: " + value));
            context.addSummeryLine("Extracting data " + JSON_PATH + ". Value: " + value);
            if (value.toString().equals("")) {
                context.addLog(new StepsLog("No value found for json path jsonPath"));
                context.addSummeryLine("No value found for json path jsonPath");
            }

            String stringValue = value.toString();
            if (stringValue.endsWith(", ")) {
                // Remove the trailing comma
                stringValue = stringValue.substring(0, stringValue.length() - 2);
            }

            VALUE = stringValue;
            stepResult = StepResult.SUCCESS;
        } catch (Exception e) {
            context.addLog(new StepsLog(e.getMessage()));
            context.addSummeryLine(e.getMessage());
            stepResult = StepResult.FAILURE;
        }
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("VALUE"),VALUE);
        context.storeDataValue("VALUE",data);
        context.updateDurationStep(start);
        return stepResult;
    }

//        long start = System.currentTimeMillis();
//        DataInFlow data;
//        StepUsageDescription usage = context.getFlowExecution().getFlowDefinition().
//                getStepUsageDescription(context.getFinalStepName());
//        String json = context.getDataValue("JSON", String.class, this);//input
//        String jsonPath = context.getDataValue("JSON_PATH", String.class, this);//input
//        String value = "";//output
//        Gson gson = new Gson();
//
//        JsonElement jsonElement= gson.toJsonTree(json);
//        StringBuilder extractedInfo = new StringBuilder();
//        String[] expressions = jsonPath.split("\\|");
//        StepResult stepResult = null;
//
//        for (String expression : expressions) {
//            if (!expression.startsWith("$")) {
//                context.addSummeryLine("Invalid JSONPath expression: " + expression);
//                stepResult = StepResult.FAILURE;
//                continue;
//            }
//
//            try {
//                Object result = JsonPath.read(jsonElement.toString(), expression);
//                if (result != null) {
//                    extractedInfo.append(result.toString()).append(", ");
//                } else
//                    context.addLog(new StepsLog("No value found for json path " + jsonPath));
//                stepResult = StepResult.SUCCESS;
//            } catch (PathNotFoundException e) {
//                stepResult = StepResult.FAILURE;
//                context.addLog(new StepsLog("Path not found for expression: " + expression));
//                context.addSummeryLine("Path not found for expression: " + expression);
//            }
//        }
//        // Remove the trailing comma and space if there is any extracted information
//        if (extractedInfo.length() > 0) {
//            extractedInfo.setLength(extractedInfo.length() - 2);
//            context.addLog(new StepsLog("Extracting data " + jsonPath + ". Value: " + extractedInfo.toString()));
//        }
//        value=extractedInfo.toString();
//        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("VALUE"),value);
//        context.storeDataValue("VALUE",data);
//        context.updateDurationStep(start);
//        return stepResult;
}

