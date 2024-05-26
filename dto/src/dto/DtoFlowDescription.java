package dto;

import java.util.ArrayList;
import java.util.List;

public class DtoFlowDescription
{
    private String Name;
    private String description;
    private boolean isReadOnly;
    private List<String> formalOutputs;

    private List<DtoStepDescription> steps;
    private List<DtoStepMoreDescription> stepsMoreInfo;
    private List<DtoInputDescription> freeInput;
    private List<DtoOutputDescription> outputs;
    private Integer StepsNum;
    private Integer FreeInputsNum;
    private Integer ContinuationsNum;


    public DtoFlowDescription(String Name,String description,boolean isReadOnly)
    {
        this.Name=Name;
        this.isReadOnly=isReadOnly;
        this.description=description;
        this.formalOutputs=new ArrayList<>();
        this.steps=new ArrayList<>();
        this.freeInput=new ArrayList<>();
        this.outputs=new ArrayList<>();
        this.stepsMoreInfo=new ArrayList<>();
    }

    public List<DtoStepMoreDescription> getStepsMoreInfo() {
        return stepsMoreInfo;
    }

    public void setStepsMoreInfo(List<DtoStepMoreDescription> stepsMoreInfo) {
        this.stepsMoreInfo = stepsMoreInfo;
    }
    public void addStepMoreInfo(DtoStepMoreDescription dto ) {
        this.stepsMoreInfo.add(dto);
    }

    public String getStringOfFormalOutputs()
    {
        StringBuilder res= new StringBuilder();
        for (int i = 0; i < formalOutputs.size(); i++)
        {
            res.append(i + 1).append(". ").append(formalOutputs.get(i)).append("\n");
        }
        return res.toString();
    }
    public void setFreeInput(List<DtoInputDescription> freeInput) {
        this.freeInput = freeInput;
    }

    public void setOutputs(List<DtoOutputDescription> outputs) {
        this.outputs = outputs;
    }

    public Integer getStepsNum() {
        return StepsNum;
    }

    public Integer getFreeInputsNum() {
        return FreeInputsNum;
    }

    public Integer getContinuationsNum() {
        return ContinuationsNum;
    }

    public void addFreeInput(DtoInputDescription dtoInputDescription)
    {
        freeInput.add(dtoInputDescription);
    }
    public void addOutput(DtoOutputDescription dtoOutputDescription)
    {
        outputs.add(dtoOutputDescription);
    }
    public void addStep(DtoStepDescription dtoStepDescription)
    {
        this.steps.add(dtoStepDescription);
    }

    public String getName() {
        return Name;
    }

    public List<DtoInputDescription> getFreeInput() {
        return freeInput;
    }

    public List<String> getFormalOutputs() {
        return formalOutputs;
    }

    public List<DtoOutputDescription> getOutputs() {
        return outputs;
    }

    public List<DtoStepDescription> getSteps() {
        return steps;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setFormalOutputs(List<String> formalOutputs) {
        this.formalOutputs = formalOutputs;
    }

    public void setStepsNum() {
        StepsNum = this.steps.size();
    }

    public void setFreeInputsNum() {
        FreeInputsNum = this.freeInput.size();
    }

    public void setContinuationsNum(int num) {
        ContinuationsNum = num;
    }
}
