package stepper.dd.imp.DoubleDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.Serializable;

public class DoubleDD extends AbstractDataDefinition implements Serializable {
    public DoubleDD() {
        super("Double", true, Double.class);
    }
}
