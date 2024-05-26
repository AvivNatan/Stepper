package stepper.dd.imp.RelationDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.Serializable;

public class RelationDD extends AbstractDataDefinition implements Serializable {
    public RelationDD() {
        super("Relation", false, RelationData.class);
    }
}
