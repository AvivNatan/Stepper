package stepper.dd.imp.StringDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.Serializable;

public class StringDD extends AbstractDataDefinition implements Serializable {
    public StringDD()
    {
        super("String", true, String.class);
    }
}
