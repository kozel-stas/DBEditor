package model;

import org.eclipse.widgets.tableComponents.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Event implements TableData {

    private static List<ColumnData> list = new ArrayList<>();

    static {
        list.add(new ColumnData("ID", true, true, true, ColumnType.DATA));
        list.add(new ColumnData("name", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("start date", true, false, true, ColumnType.DATE));
        list.add(new ColumnData("end date", true, false, true, ColumnType.DATE));
        list.add(new ColumnData("correspondent", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("orderID", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("completed", true, false, false, ColumnType.CHECKBOX));
    }

    private static TableObject tableObject = new TableObject(list, Event.class, new Extractor<Event>() {
        @Override
        public Event extract(Map<ColumnData, String> data) {
            return new Event(
                    Long.valueOf(data.get(list.get(0)) == null ? "0" : data.get(list.get(0))),
                    data.get(list.get(1)),
                    Long.valueOf(data.get(list.get(2))),
                    Long.valueOf(data.get(list.get(3))),
                    Long.valueOf(data.get(list.get(4))),
                    Long.valueOf(data.get(list.get(5))),
                    Boolean.valueOf(data.get(list.get(6))));
        }
    });

    private final long id;
    private final String name;
    private final long startDate;
    private final long endDate;
    private final long corespondentID;
    private final long orderID;
    private final boolean completed;

    public Event(
            long id,
            String name,
            long startDate,
            long endDate,
            long corespondentID,
            long orderID,
            boolean completed
    ) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.corespondentID = corespondentID;
        this.orderID = orderID;
        this.completed = completed;
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

    public String getName() {
        return name;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getCorespondentID() {
        return corespondentID;
    }

    public long getOrderID() {
        return orderID;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public Map<ColumnData, String> getMapData() {
        return Map.of(list.get(0), String.valueOf(id), list.get(1), name, list.get(2), String.valueOf(startDate),
                list.get(3), String.valueOf(endDate), list.get(4), String.valueOf(corespondentID),
                list.get(5), String.valueOf(orderID), list.get(6), String.valueOf(completed));
    }
}
