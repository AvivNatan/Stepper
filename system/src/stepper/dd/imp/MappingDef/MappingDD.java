package stepper.dd.imp.MappingDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.Serializable;
import java.util.Map;

public class MappingDD extends AbstractDataDefinition implements Serializable {
    public MappingDD() {
        super("Mapping", false, Map.class);
    }
}
