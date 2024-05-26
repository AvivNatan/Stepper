package stepper.flow.definition.api;

public class InitialInputValue
{
    String inputName;
    String initialValue;

    public InitialInputValue(String inputName, String initialValue) {
        this.inputName = inputName;
        this.initialValue = initialValue;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }
}
