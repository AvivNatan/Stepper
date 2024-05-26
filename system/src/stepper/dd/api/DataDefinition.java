package stepper.dd.api;

import java.io.Serializable;

public interface DataDefinition extends Serializable {
    String getName();
    boolean isUserFriendly();
    Class<?> getType();
}
