package stepper.dd.imp.EnumeratorDef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnumeratorData implements Serializable
{
    private Set<String> values;
    private String chosenValue=null;

    public EnumeratorData(Set<String> values)
    {
        this.values = values;
    }

    public Set<String> getValues() {
        return values;
    }

    public String getChosenValue() {
        return chosenValue;
    }

    public void setChosenValue(String chosenValue) {
        this.chosenValue = chosenValue;
    }

    @Override
    public String toString() {
        return getChosenValue();
    }
}
