package model;

import org.eclipse.widgets.tableComponents.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Order implements TableData {

    private static List<ColumnData> list = new ArrayList<>();

    static {
        list.add(new ColumnData("ID", true, true, true, ColumnType.DATA));
        list.add(new ColumnData("content", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("start date", true, false, true, ColumnType.DATE));
        list.add(new ColumnData("owner", true, false, true, ColumnType.DATA));
    }

    private static TableObject tableObject = new TableObject(list, Order.class, new Extractor<Order>() {
        @Override
        public Order extract(Map<ColumnData, String> data) {
            return new Order(
                    Long.valueOf(data.get(list.get(0)) == null ? "0" : data.get(list.get(0))),
                    data.get(list.get(1)),
                    Long.valueOf(data.get(list.get(2))),
                    Long.valueOf(data.get(list.get(3)))
            );
        }
    });

    private final long id;
    private final String content;
    private final long startDate;
    private final long owner;

    public Order(long id, String content, long startDate, long owner) {
        this.id = id;
        this.content = content;
        this.startDate = startDate;
        this.owner = owner;
    }

    public static TableObject getTableObject() {
        return tableObject;
    }

    @Override
    public long getKey() {
        return getId();
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getOwner() {
        return owner;
    }

    @Override
    public Map<ColumnData, String> getMapData() {
        return Map.of(list.get(0), String.valueOf(id), list.get(1), content, list.get(2), String.valueOf(startDate), list.get(3), String.valueOf(owner));
    }

}
