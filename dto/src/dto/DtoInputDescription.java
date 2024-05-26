package dto;

import stepper.dd.api.DataDefinition;
import stepper.step.api.DataNecessity;

import java.util.List;

public class DtoInputDescription
{
    private String finalName;
    private String type;
    private List<String> namesSteps;
    private DataNecessity necessity;
    public DtoInputDescription(String finalName, String type, DataNecessity necessity, List<String> namesSteps)
    {
        this.finalName=finalName;
        this.necessity=necessity;
        this.namesSteps =namesSteps;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public DataNecessity getNecessity() {
        return necessity;
    }

    public String getFinalName() {
        return finalName;
    }


    public List<String> getNamesSteps() {
        return namesSteps;
    }
    public String getStringOfSteps()
    {
        StringBuilder res= new StringBuilder();
        for (int i = 0; i < namesSteps.size(); i++)
        {
            res.append(i + 1).append(". ").append(namesSteps.get(i)).append("\n");
        }
        return res.toString();
    }

    public void setNecessity(DataNecessity necessity) {
        this.necessity = necessity;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addNameStep(String nameStep) {
        this.namesSteps.add(nameStep);
    }
}
