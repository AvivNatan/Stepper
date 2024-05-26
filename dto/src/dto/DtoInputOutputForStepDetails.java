package dto;

public class DtoInputOutputForStepDetails
{
    private String name;
    private Object content;
    private String type;

    public DtoInputOutputForStepDetails(String name, Object content,String type) {
        this.name = name;
        this.content = content;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
