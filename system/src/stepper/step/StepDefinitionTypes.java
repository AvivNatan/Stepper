package stepper.step;

import stepper.dd.api.DataDefinition;
import stepper.flow.definition.api.DataKind;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.step.api.StepDefinition;
import stepper.step.api.StepResult;
import stepper.step.imp.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public enum StepDefinitionTypes implements StepDefinition , Serializable
{

    Spend_Some_Time(new SpendSomeTime()),
    Collect_Files_In_Folder(new CollectFilesInFolder()),
    CSV_Exporter(new CSVExporter()),
    File_Dumper(new FileDumper()),
    Files_Content_Extractor(new FilesContentExtractor()),
    Files_Deleter(new FilesDeleter()),
    Files_Renamer(new FilesRenamer()),
    Properties_Exporter(new PropertiesExporter()),
    Zipper(new Zipper()),
    Command_Line(new CommandLine()),
    Http_Call(new HttpCall()),
    To_Json(new ToJson()),
    Json_Data_Exporter(new JsonDataExtractor())
    ;
    private final StepDefinition stepDefinition;

   StepDefinitionTypes(StepDefinition stepDefinition)
   {
       this.stepDefinition=stepDefinition;
   }

    @Override
    public String originalStepName() {
        return stepDefinition.originalStepName();
    }

    @Override
    public boolean isReadonly() {
        return stepDefinition.isReadonly();
    }

    @Override
    public List<stepper.step.api.DataDefinitionDescription> inputs() {
        return stepDefinition.inputs();
    }

    @Override
    public List<stepper.step.api.DataDefinitionDescription> outputs() {
        return stepDefinition.outputs();
    }

    @Override
    public DataDefinition getDataDefinitionByNameOfData(String NameData) {
        return stepDefinition.getDataDefinitionByNameOfData(NameData);
    }

    @Override
    public DataKind isInputOrOutput(String originalName) {
        return stepDefinition.isInputOrOutput(originalName);
    }

    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }

    @Override
    public String toString() {
       return super.toString().replaceAll("_", " ");
    }

    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
        return stepDefinition.invoke(context);
    }

}
