package dto;

import stepper.step.api.DataNecessity;

public class DtoDataMoreDescription
{
    private String dataFinalName;
    private DataNecessity necessity;
    private boolean isConnected;
    private String stepConnectedName=null;
    private String dataConnectedName=null;

    public DtoDataMoreDescription(String dataFinalName, DataNecessity necessity) {
        this.dataFinalName = dataFinalName;
        this.necessity = necessity;
        this.isConnected=false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getDataFinalName() {
        return dataFinalName;
    }

    public void setDataFinalName(String dataFinalName) {
        this.dataFinalName = dataFinalName;
    }

    public DataNecessity getNecessity() {
        return necessity;
    }

    public void setNecessity(DataNecessity necessity) {
        this.necessity = necessity;
    }

    public String getStepConnectedName() {
        return stepConnectedName;
    }

    public void setStepConnectedName(String stepConnectedName) {
        this.stepConnectedName = stepConnectedName;
    }

    public String getDataConnectedName() {
        return dataConnectedName;
    }

    public void setDataConnectedName(String dataConnectedName) {
        this.dataConnectedName = dataConnectedName;
    }
}
