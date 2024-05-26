package stepper.xmlexceptions;

import java.io.Serializable;

public enum CustomMappingType implements Serializable
{
    TARGET,SOURCE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
