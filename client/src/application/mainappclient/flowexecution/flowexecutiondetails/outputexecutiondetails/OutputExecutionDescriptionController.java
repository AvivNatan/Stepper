package application.mainappclient.flowexecution.flowexecutiondetails.outputexecutiondetails;

import application.mainappclient.flowexecution.flowexecutiondetails.outputsexecutiondetails.OutputsExecutionDescriptionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import stepper.dd.imp.RelationDef.RelationData;

import java.util.*;

public class OutputExecutionDescriptionController {

     private OutputsExecutionDescriptionController outputsExecutionDescriptionController;
    @FXML
    private Label finalOutputName;

    @FXML
    private Label type;
    @FXML
    private Label stepRelated;

    public OutputsExecutionDescriptionController getOutputsExecutionDescriptionController() {
        return outputsExecutionDescriptionController;
    }

    public void setOutputsExecutionDescriptionController(OutputsExecutionDescriptionController outputsExecutionDescriptionController) {
        this.outputsExecutionDescriptionController = outputsExecutionDescriptionController;
    }

    public void setFinalOutputName(String finalOutputName) {
        this.finalOutputName.setText(finalOutputName);
    }

    public void setType(String type) {
        this.type.setText(type);
    }

    public void setContent(Object content, GridPane grid)
    {
        Integer num;
        String car;
        String cdr;
        if(content==null)
        {
            Text text=new Text("Output Not created due to failure in flow");
            TextFlow textFlow=new TextFlow(text);
            ScrollPane scrollPane=new ScrollPane(textFlow);
            grid.add(scrollPane,1,2);
        }
        else
        {

            if(Objects.equals(type.getText(), "RelationData"))
            {
                RelationData data=convertContentToRelationData(content);
                TableView<List<String>> table=createTableView(data);
                grid.add(table,1,2);
            }
            else if(Objects.equals(type.getText(), "List"))
            {
                String res="";

                for (int i = 0; i < ((List)content).size(); i++) {
                    if(((List)content).size()-1==i)
                    {
                        res=res+(i+1)+". "+((List)content).get(i);
                    }
                    else
                    {
                        res=res+(i+1)+". "+((List)content).get(i)+"\n";
                    }
                }
                Text text=new Text(res);
                TextFlow textFlow=new TextFlow(text);
                ScrollPane scrollPane=new ScrollPane(textFlow);
                grid.add(scrollPane,1,2);
            }
            else if(Objects.equals(type.getText(), "Map"))
            {
                StringBuilder res= new StringBuilder();
                for(Map.Entry<Object,Object> entry :((Map<Object,Object>)content).entrySet())
                {
                    if(entry.getKey() instanceof Double)
                    {
                         num=((Double)entry.getKey()).intValue();
                         car=num.toString();
                    }
                    else
                        car=entry.getKey().toString();

                    if(entry.getValue() instanceof Double)
                    {
                         num=((Double)entry.getValue()).intValue();
                         cdr=num.toString();
                    }
                    else
                        cdr=entry.getValue().toString();

                    res.append("car: ").append("<").append(car).append(">").append("\n");
                    res.append("cdr: ").append("<").append(cdr).append(">").append("\n");
                }
                Text text=new Text(res.toString());
                TextFlow textFlow=new TextFlow(text);
                ScrollPane scrollPane=new ScrollPane(textFlow);
                grid.add(scrollPane,1,2);
            }
            else if(Objects.equals(type.getText(), "Integer"))
            {
                num=((Double)content).intValue();
                String str=num.toString();
                Text text=new Text(str);
                TextFlow textFlow=new TextFlow(text);
                ScrollPane scrollPane=new ScrollPane(textFlow);
                grid.add(scrollPane,1,2);
            }
            else
            {
                Text text=new Text(content.toString());
                TextFlow textFlow=new TextFlow(text);
                ScrollPane scrollPane=new ScrollPane(textFlow);
                grid.add(scrollPane,1,2);
            }
        }


    }

    public void setStepRelated(String stepRelated) {
        this.stepRelated.setText(stepRelated);
    }
    public TableView<List<String>> createTableView(RelationData relationData)
    {
        List<List<String>> table = relationData.getTable();
        List<String> columnNames = table.get(0);

        TableView<List<String>> tableView = new TableView<>();
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            final int index = columnIndex; // Need final variable for lambda expression
            TableColumn<List<String>, String> column = new TableColumn<>(columnNames.get(columnIndex));
            column.setPrefWidth(135);
            column.setCellValueFactory(cellData -> {
                List<String> rowData = cellData.getValue();
                if (rowData != null && index < rowData.size()) {
                    return new SimpleStringProperty(rowData.get(index));
                } else {
                    return new SimpleStringProperty("");
                }
            });
            tableView.getColumns().add(column);
        }

        for (int rowIndex = 1; rowIndex < table.size(); rowIndex++) {
            List<String> rowData = table.get(rowIndex);
            tableView.getItems().add(rowData);
        }
        return tableView;
    }
    public RelationData convertContentToRelationData(Object content)
    {
        RelationData res=new RelationData(((LinkedHashMap<String, ArrayList>)content).get("columns"));
        ArrayList<LinkedHashMap<String,LinkedHashMap>> rows=((LinkedHashMap<String, ArrayList<LinkedHashMap<String, LinkedHashMap>>>)content).get("rows");
        for (LinkedHashMap<String,LinkedHashMap> map : rows)
        {
            res.addRowFromJson(map.get("data"));
        }
        return res;
    }
}
