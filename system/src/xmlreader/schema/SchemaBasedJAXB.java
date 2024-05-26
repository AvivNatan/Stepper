package xmlreader.schema;

import xmlreader.schema.generated.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SchemaBasedJAXB
{
     private final static String JAXB_XML_GAME_PACKAGE_NAME = "xmlreader.schema.generated";

    public STStepper getStepperFromXmlFile(InputStream file)
    {
        STStepper stepper=null;
        try {
            stepper = deserializeFrom(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return stepper;
    }
    private static STStepper deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (STStepper) u.unmarshal(in);
    }
}


