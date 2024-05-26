package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DtoFreeInputs
{
    List<DtoFreeInputDescription> freeInputs;
    public DtoFreeInputs()
    {
        freeInputs=new ArrayList<>();
    }
    public void addFreeInput(DtoFreeInputDescription dtoFreeInputDescription)
    {
        freeInputs.add(dtoFreeInputDescription);
    }
    public DtoFreeInputDescription getFreeInput(int index)
    {
        return freeInputs.get(index);
    }

    public void setFreeInputs(List<DtoFreeInputDescription> freeInputs) {
        this.freeInputs = freeInputs;

    }
    public DtoFreeInputDescription findFreeInputByFinalName(String name)
    {
        for(DtoFreeInputDescription dto:freeInputs)
            if(Objects.equals(dto.getFinalName(), name))
                return dto;
        return null;
    }

    public List<DtoFreeInputDescription> getFreeInputs() {
        return freeInputs;
    }
}
