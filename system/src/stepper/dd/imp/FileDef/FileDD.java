package stepper.dd.imp.FileDef;

import stepper.dd.api.AbstractDataDefinition;

import java.io.File;
import java.io.Serializable;

public class FileDD extends AbstractDataDefinition implements Serializable {
    public FileDD() {
        super("File", false, File.class);
    }
}
