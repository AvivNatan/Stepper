package stepper.xmlexceptions;

import java.io.Serializable;

public class MenuException extends Throwable implements Serializable {
    private final String EXCEPTION_MESSAGE = "Go back to Menu";

    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
