package stepper.step.imp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import stepper.dd.imp.DataDefinitionTypes;
import stepper.dd.imp.EnumeratorDef.EnumeratorData;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
public class HttpCall extends AbstractStepDefinition implements Serializable
{
    public HttpCall()
    {
        super("HTTP Call", false);
        DataDefinitionDescription data;
        Set<String> list;

        data=new DataDefinitionDescriptionImpl("RESOURCE",
                DataNecessity.MANDATORY, "Resource Name (include query parameters)", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(false);
        addInput(data);


        data=new DataDefinitionDescriptionImpl("ADDRESS",
                DataNecessity.MANDATORY, "Domain:Port", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(false);
        addInput(data);


        data=new DataDefinitionDescriptionImpl("PROTOCOL",
                DataNecessity.MANDATORY, "protocol", DataDefinitionTypes.ENUMERATOR);
        list=new HashSet<>();
        list.add("http");
        list.add("https");
        data.setMoreInfoOfData(list);
        addInput(data);

        data=new DataDefinitionDescriptionImpl("METHOD",
                DataNecessity.OPTIONAL, "Method", DataDefinitionTypes.ENUMERATOR);
        list=new HashSet<>();
        list.add("PUT");
        list.add("POST");
        list.add("DELETE");
        list.add("GET");
        data.setMoreInfoOfData(list);
        addInput(data);


        data=new DataDefinitionDescriptionImpl("BODY",
                DataNecessity.OPTIONAL, "Request Body", DataDefinitionTypes.JSON);
        data.setMoreInfoOfData(false);
        addInput(data);


        addOutput(new DataDefinitionDescriptionImpl("CODE", DataNecessity.NA,
                "Response code", DataDefinitionTypes.NUMBER));

        addOutput(new DataDefinitionDescriptionImpl("RESPONSE_BODY", DataNecessity.NA,
                "Response body", DataDefinitionTypes.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {

        long start = System.currentTimeMillis();
        DataInFlow code;
        DataInFlow res;
        int responseCode;
        String MethodVal;
        StepResult stepResult;
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        String resource=context.getDataValue("RESOURCE",String.class,this);//input
        String address=context.getDataValue("ADDRESS",String.class,this);//input
        EnumeratorData protocol=context.getDataValue("PROTOCOL", EnumeratorData.class,this);//input
        EnumeratorData Method=context.getDataValue("METHOD", EnumeratorData.class,this);//input

        if(Method.getChosenValue()==null)
            Method.setChosenValue("GET");

        MethodVal=Method.getChosenValue();
        String body=context.getDataValue("BODY", String.class,this);//input
        String Response=null;

        try {
            String urlString;
            if(!resource.startsWith("/"))
            {
                context.addLog(new StepsLog("About to invoke http request <request details: \n" +
                        protocol.getChosenValue()  + "://" + MethodVal + "/" + address + "/" + resource + "\n"));

                urlString =  protocol.getChosenValue() + "://" + address + "/" + resource;
            }
            else {

                context.addLog(new StepsLog( "About to invoke http request <request details: \n" +
                        protocol.getChosenValue()  + "://" + MethodVal + "/" + address + resource + "\n"));
                urlString = protocol.getChosenValue() + "://" + address + resource;
            }

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(MethodVal);

            if (MethodVal.equals("POST") || MethodVal.equals("PUT")) {
                connection.setDoOutput(true);
                // Convert JsonElement to JSON string
                connection.getOutputStream().write(body.getBytes("UTF-8"));
            }
            responseCode = connection.getResponseCode();

            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 400) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                stepResult = StepResult.SUCCESS;
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                stepResult = StepResult.FAILURE;
            }

            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            reader.close();
            Response=responseBody.toString();

        } catch (Exception e) {
            responseCode=-1;
            Response=e.getMessage();
            stepResult = StepResult.FAILURE;
            context.addLog(new StepsLog("The request could not be sent."));
            context.addSummeryLine("The request could not be sent.");
        }


        context.addLog(new StepsLog("Received Response. Status code:"+responseCode));
        code=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("CODE"),  responseCode);
        res=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESPONSE_BODY"), Response);
        context.storeDataValue("CODE",code);
        context.storeDataValue("RESPONSE_BODY",res);
        context.updateDurationStep(start);
        return stepResult;

    }
}
