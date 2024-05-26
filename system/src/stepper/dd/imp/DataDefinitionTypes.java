package stepper.dd.imp;

import stepper.dd.api.DataDefinition;
import stepper.dd.imp.DoubleDef.DoubleDD;
import stepper.dd.imp.EnumeratorDef.EnumeratorDD;
import stepper.dd.imp.FileDef.FileDD;
import stepper.dd.imp.JsonDef.JsonDD;
import stepper.dd.imp.ListDef.ListDD;
import stepper.dd.imp.MappingDef.MappingDD;
import stepper.dd.imp.NumberDef.NumberDD;
import stepper.dd.imp.RelationDef.RelationDD;
import stepper.dd.imp.StringDef.StringDD;

import java.io.Serializable;

public enum DataDefinitionTypes implements DataDefinition, Serializable
{
    STRING(new StringDD()),
    DOUBLE(new DoubleDD()),
    NUMBER(new NumberDD()),
    LIST(new ListDD()),
    FILE(new FileDD()),
    MAPPING(new MappingDD()),
    RELATION(new RelationDD()),
    ENUMERATOR(new EnumeratorDD()),
    JSON(new JsonDD())
    ;

    DataDefinitionTypes(DataDefinition dataDefinition) {
        this.dataDefinition = dataDefinition;
    }

    private final DataDefinition dataDefinition;

    @Override
    public String getName() {
        return dataDefinition.getName();
    }

    @Override
    public boolean isUserFriendly() {
        return dataDefinition.isUserFriendly();
    }

    @Override
    public Class<?> getType() {
        return dataDefinition.getType();
    }
}
