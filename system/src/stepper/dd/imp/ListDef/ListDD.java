package stepper.dd.imp.ListDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.Serializable;
import java.util.List;

public class ListDD extends AbstractDataDefinition implements Serializable {
    public ListDD() {
        super("List", false, List.class);
    }
}
