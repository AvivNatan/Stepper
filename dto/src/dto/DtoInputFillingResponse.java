package dto;

public class DtoInputFillingResponse
{
    private Object obj;
    private String type;
    private Object moreInfo;

    public DtoInputFillingResponse(Object obj, String type, Object moreInfo) {
        this.obj = obj;
        this.type = type;
        this.moreInfo = moreInfo;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(Object moreInfo) {
        this.moreInfo = moreInfo;
    }
}
