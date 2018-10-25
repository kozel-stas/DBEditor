package model;

import org.eclipse.widgets.tableComponents.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Corespondent implements TableData {

    private static List<ColumnData> list = new ArrayList<>();

    static {
        list.add(new ColumnData("ID", true, true, true, ColumnType.DATA));
        list.add(new ColumnData("surname", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("name", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("last name", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("position", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("division", true, false, false, ColumnType.DATA));
        list.add(new ColumnData("chief", true, false, false, ColumnType.CHECKBOX));
    }

    private static TableObject tableObject = new TableObject(list, Corespondent.class, new Extractor<Corespondent>() {
        @Override
        public Corespondent extract(Map<ColumnData, String> data) {
            return new Corespondent(
                    Long.valueOf(data.get(list.get(0)) == null ? "0" : data.get(list.get(0))),
                    data.get(list.get(1)),
                    data.get(list.get(2)),
                    data.get(list.get(3)),
                    data.get(list.get(4)),
                    data.get(list.get(5)),
                    Boolean.valueOf(data.get(list.get(6)))
            );
        }
    });

    private final long id;
    private final String surname;
    private final String name;
    private final String lastName;
    private final String position;
    private final String division;
    private final boolean chief;

    public Corespondent(
            long id,
            String surname,
            String name,
            String lastName,
            String position,
            String division,
            boolean chief
    ) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.lastName = lastName;
        this.position = position;
        this.division = division;
        this.chief = chief;
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

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }

    public String getDivision() {
        return division;
    }

    @Override
    public Map<ColumnData, String> getMapData() {
        return Map.of(list.get(0), String.valueOf(id), list.get(1), surname, list.get(2),
                name, list.get(3), lastName, list.get(4), position, list.get(5), division, list.get(6), String.valueOf(isChief()));
    }

    public boolean isChief() {
        return chief;
    }
}
