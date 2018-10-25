package org.eclipse.widgets.tableComponents;

import java.util.List;

public class TableObject {
    private List<ColumnData> list;
    private Class clazz;
    private Extractor extractor;

    public TableObject(List<ColumnData> list, Class clazz, Extractor extractor) {
        this.list = list;
        this.clazz = clazz;
        this.extractor = extractor;
    }

    public List<ColumnData> getListOfColumns() {
        return list;
    }

    public Class getClazz() {
        return clazz;
    }

    public Extractor getExtractor() {
        return extractor;
    }

}
