package stepper.step.api;

import stepper.dd.api.DataDefinition;

import java.io.Serializable;

public class DataDefinitionDescriptionImpl implements stepper.step.api.DataDefinitionDescription, Serializable
{
    private final String originalDataName;
    private final DataNecessity necessity;
    private final String userString;
    private final DataDefinition dataDefinition;
    private Object moreInfoOfData=null;

    public DataDefinitionDescriptionImpl(String name, DataNecessity necessity, String userString, DataDefinition dataDefinition) {
        this.originalDataName = name;
        this.necessity = necessity;
        this.userString = userString;
        this.dataDefinition = dataDefinition;
    }
    @Override
    public void setMoreInfoOfData(Object moreInfoOfData) {
        this.moreInfoOfData = moreInfoOfData;
    }

    @Override
    public Object getMoreInfoOfData() {
        return moreInfoOfData;
    }


    @Override
    public String getOriginalDataName() {
        return originalDataName;
    }

    @Override
    public DataNecessity necessity() {
        return necessity;
    }

    @Override
    public String userString() {
        return userString;
    }

    @Override
    public DataDefinition dataDefinition() {
        return dataDefinition;
    }
}
