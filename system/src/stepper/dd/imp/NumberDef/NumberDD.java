package stepper.dd.imp.NumberDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.Serializable;

public class NumberDD extends AbstractDataDefinition implements Serializable
{
    public NumberDD() {
        super("Number", true, Integer.class);
    }
}
