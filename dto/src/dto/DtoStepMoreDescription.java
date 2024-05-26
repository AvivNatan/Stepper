package dto;

import java.util.ArrayList;
import java.util.List;

public class DtoStepMoreDescription
{
   private String stepName;
   private List<DtoDataMoreDescription> inputs;
   private List<DtoDataMoreDescription> outputs;

   public DtoStepMoreDescription(String stepName) {
      this.stepName = stepName;
      this.inputs = new ArrayList<>();
      this.outputs = new ArrayList<>();
   }
   public void addInput(DtoDataMoreDescription dto)
   {
      inputs.add(dto);
   }
   public void addOutput(DtoDataMoreDescription dto)
   {
      outputs.add(dto);
   }

   public String getStepName() {
      return stepName;
   }

   public void setStepName(String stepName) {
      this.stepName = stepName;
   }

   public List<DtoDataMoreDescription> getInputs() {
      return inputs;
   }

   public void setInputs(List<DtoDataMoreDescription> inputs) {
      this.inputs = inputs;
   }

   public List<DtoDataMoreDescription> getOutputs() {
      return outputs;
   }

   public void setOutputs(List<DtoDataMoreDescription> outputs) {
      this.outputs = outputs;
   }
}
