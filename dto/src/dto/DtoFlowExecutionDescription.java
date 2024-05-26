package dto;

import stepper.flow.excution.FlowExecutionResult;

import java.util.*;

public class DtoFlowExecutionDescription
{
    UUID uniqueId;
    String name;
    FlowExecutionResult resultExecution;

    Map<String,Object> userStringToObject;//formal outputs

    public DtoFlowExecutionDescription(UUID uniqueId, String name, FlowExecutionResult resultExecution) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.resultExecution = resultExecution;
        this.userStringToObject = new HashMap<>();
    }
    public void addUserStringAndName(String finalName,String userString,Object obj)
    {
        String res=String.format("%s (%s)",userString,finalName);
        userStringToObject.put(res,obj);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public FlowExecutionResult getResultExecution() {
        return resultExecution;
    }

    public Map<String, Object> getUserStringToObject() {
        return userStringToObject;
    }
}
