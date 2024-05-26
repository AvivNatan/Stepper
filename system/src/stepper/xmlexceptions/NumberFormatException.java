package stepper.xmlexceptions;

import java.io.Serializable;

public class NumberFormatException extends RuntimeException implements Serializable
{
    private String type;
    private final String EXCEPTION_MESSAGE = "The input is not of the type: %s ";

    public NumberFormatException(String type) {

        this.type=type;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,type);
    }
}
