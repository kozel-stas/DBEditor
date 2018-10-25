package org.eclipse.widgets.tableComponents;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.locks.Lock;

public class ColumnData {

    private String name;
    private boolean visible;
    private boolean ignoreForAddWindow;
    private boolean ignoreForUpdateWindow;
    private ColumnType columnType;

    public ColumnData(String name, boolean visible, boolean ignoreForAddWindow, boolean ignoreForUpdateWindow, ColumnType columnType) {
        this.name = name;
        this.visible = visible;
        this.ignoreForAddWindow = ignoreForAddWindow;
        this.ignoreForUpdateWindow = ignoreForUpdateWindow;
        this.columnType = columnType;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isIgnoreForAddWindow() {
        return ignoreForAddWindow;
    }

    public boolean isIgnoreForUpdateWindow() {
        return ignoreForUpdateWindow;
    }

    public String format(String input) {
        if (isDate()) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(input)), ZoneId.systemDefault()).toString();
        }
        return input;
    }

    public boolean isDate() {
        return columnType == ColumnType.DATE;
    }

    public boolean isCheckBox() {
        return columnType == ColumnType.CHECKBOX;
    }

}
