package stepper.step.api;

import stepper.dd.api.DataDefinition;

import java.io.Serializable;

public interface DataDefinitionDescription extends Serializable {
    String getOriginalDataName();
    DataNecessity necessity();
    String userString();
    DataDefinition dataDefinition();
    void setMoreInfoOfData(Object moreInfoOfData);
    public Object getMoreInfoOfData();

}
