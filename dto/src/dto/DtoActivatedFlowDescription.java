package dto;

import java.util.UUID;

public class DtoActivatedFlowDescription
{
    private UUID uniqueId;
    private String name;
    private String time;
    public DtoActivatedFlowDescription(UUID uniqueId, String name, String time)
    {
        this.name=name;
        this.time=time;
        this.uniqueId=uniqueId;
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getTime() {
        return time;
    }
}
