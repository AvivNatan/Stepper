package dto;

public class DtoStepDescription
{
    private String finalName;
    private String originalName;
    private boolean isReadOnly;
    public DtoStepDescription(String finalName,String originalName,boolean isReadOnly)
    {
        this.isReadOnly=isReadOnly;
        this.originalName=originalName;
        this.finalName=finalName;
    }

    public boolean getIsReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public void setName(String name) {
        this.finalName = name;
    }

    public String getFinalName() {
        return finalName;
    }

    public String getOriginalName() {
        return originalName;
    }
}
