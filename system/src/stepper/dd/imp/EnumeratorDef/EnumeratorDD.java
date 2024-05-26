package stepper.dd.imp.EnumeratorDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.Serializable;

public class EnumeratorDD extends AbstractDataDefinition implements Serializable {
    public EnumeratorDD() {
        super("Enumerator", true, EnumeratorData.class);
    }
}
