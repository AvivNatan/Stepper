package stepper.dd.imp.JsonDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.File;
import java.io.Serializable;

public class JsonDD extends AbstractDataDefinition implements Serializable {
    public JsonDD() {
        super("Json", true, String.class);
    }
}


