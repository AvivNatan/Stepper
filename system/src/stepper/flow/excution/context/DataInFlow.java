package stepper.flow.excution.context;

import stepper.flow.definition.api.DataUsageDescription;

import java.io.Serializable;

public interface DataInFlow extends Serializable {
     DataUsageDescription getDataUsageDefinition();
     Object getContent();
     void setContent(Object content);

}
