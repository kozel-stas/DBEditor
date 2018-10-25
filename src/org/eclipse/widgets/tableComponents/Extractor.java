package org.eclipse.widgets.tableComponents;

import java.util.Map;

public interface Extractor<T> {

    T extract(Map<ColumnData, String> data);

}
