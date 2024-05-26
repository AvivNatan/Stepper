package stepper.flow.excution.context;

import stepper.flow.definition.api.DataUsageDescription;

import java.io.Serializable;

public class DataInFlowImp implements DataInFlow , Serializable
{

    private DataUsageDescription dataDefinition;
    private Object content;

    public DataInFlowImp(DataUsageDescription dataDefinition) {
        this(dataDefinition,null);
    }
    public DataInFlowImp(DataUsageDescription dataDefinition,Object obj) {
        this.dataDefinition = dataDefinition;
        this.content = obj;
    }

    @Override
    public DataUsageDescription getDataUsageDefinition() {
        return dataDefinition;
    }

    @Override
    public Object getContent() {
        return content;
    }

    @Override
    public void setContent(Object content)
    {
        this.content=content;
    }
}
