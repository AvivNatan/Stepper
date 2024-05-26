package stepper.flow.definition.api;

import stepper.dd.api.DataDefinition;
import stepper.step.api.StepDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StepUsageDescriptionImp implements StepUsageDescription , Serializable
{
    private final StepDefinition stepDefinition;
    private final boolean skipIfFail;
    private final String finalStepName;
    private List<DataUsageDescription> dataDefinitionFlow;

    public StepUsageDescriptionImp(StepDefinition stepDefinition) {
        this(stepDefinition, false, stepDefinition.originalStepName());
    }

    public StepUsageDescriptionImp(StepDefinition stepDefinition, String name) {
        this(stepDefinition, false, name);
    }

    public StepUsageDescriptionImp(StepDefinition stepDefinition, boolean skipIfFail, String stepName) {
        this.stepDefinition = stepDefinition;
        this.skipIfFail = skipIfFail;
        this.finalStepName = stepName;
        this.dataDefinitionFlow=new ArrayList<>();
        initDataUsageList();
    }
    public void initDataUsageList()
    {
        for(stepper.step.api.DataDefinitionDescription data: stepDefinition.inputs())
            addDataUsage(new DataUsageDescriptionImp(DataKind.INPUT,data,finalStepName));
        for(stepper.step.api.DataDefinitionDescription data: stepDefinition.outputs())
            addDataUsage(new DataUsageDescriptionImp(DataKind.OUTPUT,data,finalStepName));
    }


    @Override
    public List<DataUsageDescription> getDataDefinitionFlow() {
        return dataDefinitionFlow;
    }

    @Override
    public DataUsageDescription getDataUsageDescriptionByOriginalName(String originalName) {
        for(DataUsageDescription data:dataDefinitionFlow)
        {
            if(Objects.equals(data.getOriginalName(), originalName))
                return data;
        }
        return null;
    }
    @Override
    public DataUsageDescription getDataUsageByFinalName(String finalName)
    {
        for(DataUsageDescription data:dataDefinitionFlow)
        {
            if(Objects.equals(data.getFinalName(), finalName))
                return data;
        }
        return null;
    }
    @Override
    public DataDefinition getDataDefinitionByFinalName(String finalName)
    {
        for(DataUsageDescription data:dataDefinitionFlow)
        {
            if(Objects.equals(data.getFinalName(), finalName))
                return data.getDataDefinition().dataDefinition();
        }
        return null;
    }
    @Override
    public boolean isDataUsageInStep(String finalName)
    {
        for(DataUsageDescription data:dataDefinitionFlow)
        {
            if(Objects.equals(data.getFinalName(), finalName))
                return true;
        }
        return false;
    }

    @Override
    public List<DataUsageDescription> getInputs() {
        List<DataUsageDescription> res=new ArrayList<>();
        for(DataUsageDescription data:dataDefinitionFlow)
        {
            if(data.isInput())
               res.add(data);
        }
        return res;
    }

    @Override
    public List<DataUsageDescription> getOutputs() {
        List<DataUsageDescription> res=new ArrayList<>();
        for(DataUsageDescription data:dataDefinitionFlow)
        {
            if(data.isOutput())
                res.add(data);
        }
        return res;
    }

    @Override
    public void addDataUsage(DataUsageDescription data) {
        dataDefinitionFlow.add(data);
    }

    @Override
    public String getFinalStepName() {
        return finalStepName;
    }
    @Override
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }

    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
}
