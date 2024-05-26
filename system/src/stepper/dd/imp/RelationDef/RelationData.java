package stepper.dd.imp.RelationDef;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

public class RelationData implements Serializable
{
    private List<String> columns;
    private List<SingleRow> rows;

    public RelationData(List<String> columns) {
        this.columns = columns;
        rows = new ArrayList<>();
    }
    public List<List<String>> getTable()
    {
        List<List<String>> res=new ArrayList<>();
        res.add(columns);
        for (int i = 0; i < rows.size(); i++) {
            res.add(getRowDataByColumnsOrder(i));
        }
        return res;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<SingleRow> getRows() {
        return rows;
    }

    public List<String> getRowDataByColumnsOrder(int rowId)
    {
        return rows.get(rowId).getRowValues();
    }
    public int getNumberOfRows()
    {
        return rows.size();
    }
    public void addNewRow(String...args)
    {
        SingleRow newRow=new SingleRow();
        for(int i=0;i<args.length;i++)
           newRow.addData(columns.get(i),args[i]);

        rows.add(newRow);
    }
    public void addRowFromJson(LinkedHashMap<String,String> row)
    {
        rows.add(new SingleRow(row));
    }


    @Override
    public String toString()
    {
       String res="\n"+"Names Of cols: "+columns.get(0);

        for (int i = 1; i < columns.size(); i++) {
                res=res+", "+columns.get(i);
        }
        res=res+"\n"+"Number of rows: "+rows.size();
        return res;
    }
    public static class SingleRow implements Serializable {
        private Map<String, String> data;

        public SingleRow() {
            data = new LinkedHashMap<>();
        }
        public SingleRow(LinkedHashMap<String,String> map)
        {
            data = new LinkedHashMap<>();
            this.data.putAll(map);
        }
        public void addData(String columnName, String value) {
            data.put(columnName, value);
        }
        public List<String> getRowValues()
        {
            List<String> res=new ArrayList<>();
            for(String val:data.values())
                res.add(val);

            return res;
        }
    }
}
