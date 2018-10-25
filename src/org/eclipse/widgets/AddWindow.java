package org.eclipse.widgets;

import controller.Controller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.widgets.tableComponents.ColumnData;
import org.eclipse.widgets.tableComponents.TableObject;
import view.MainWindow;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class AddWindow<T> {

    private MainWindow mainWindow;
    private Shell shell;
    private Color color = new Color(null, 222, 204, 204);
    private Controller controller = Controller.getInstance();
    private TableObject tableObject;
    // column -> text
    private final Map<ColumnData, Widget> textMap = new HashMap<>();
    //FIXME
    private final Map<ColumnData, List<DateTime>> dateMap = new HashMap<>();

    public AddWindow(Display display, MainWindow mainWindow, TableObject tableObject, boolean addNewRow, Map<ColumnData, String> oldData) {
        this.mainWindow = mainWindow;
        this.tableObject = tableObject;
        shell = new Shell(display, SWT.TITLE | SWT.CLOSE);
        shell.setBackground(color);
        GridLayout gridLayout = new GridLayout(tableObject.getListOfColumns().size() * 2 + 1, false);
        gridLayout.numColumns = 2;
        gridLayout.verticalSpacing = 8;
        shell.setLayout(gridLayout);
        shell.setText("AddWindow");
        shell.setSize(500, tableObject.getListOfColumns().size() * 2 * 50 + 50);
        if (addNewRow) {
            initAddDisplay();
        } else {
            initUpdateDisplay(oldData);
        }
        shell.open();
        shell.setFocus();
    }

    private void initTextComponents(Map<ColumnData, String> oldData, boolean addWindow) {
        for (ColumnData column : tableObject.getListOfColumns()) {
            if (addWindow && !column.isIgnoreForAddWindow()) {
                addTextComponent(column, oldData);
            } else {
                if (!addWindow && !column.isIgnoreForUpdateWindow()) {
                    addTextComponent(column, oldData);
                }
            }
        }
    }

    private void addTextComponent(ColumnData column, Map<ColumnData, String> oldData) {
        Label labelData = new Label(shell, SWT.NULL);
        labelData.setText(column.getName());
        labelData.setBackground(color);

        if (column.isDate()) {
            LocalDateTime dateTime;
            if (oldData.get(column) != null) {
                dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(oldData.get(column))), ZoneId.systemDefault());
            } else {
                dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
            }

            DateTime date = new DateTime(shell, SWT.DATE);
            GridData gridDate = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridDate.horizontalSpan = 5;
            date.setLayoutData(gridDate);
            date.setDay(dateTime.getDayOfMonth());
            date.setMonth(dateTime.getMonth().getValue() - 1);
            date.setYear(dateTime.getYear());

            DateTime time = new DateTime(shell, SWT.TIME);
            GridData gridTime = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridTime.horizontalSpan = 5;
            time.setLayoutData(gridTime);
            time.setHours(dateTime.getHour());
            time.setMinutes(dateTime.getMinute());

            dateMap.put(column, List.of(date, time));
        } else {
            if (column.isCheckBox()) {
                Button check = new Button(shell, SWT.CHECK);
                check.setSelection(oldData.get(column) == null ? false : Boolean.valueOf(oldData.get(column)));
                textMap.put(column, check);
            } else {
                Text data = new Text(shell, SWT.SINGLE | SWT.BORDER);
                data.setText(oldData.get(column) == null ? "" : oldData.get(column));
                GridData gridDataSurname = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
                gridDataSurname.horizontalSpan = 5;
                data.setLayoutData(gridDataSurname);
                textMap.put(column, data);
            }
        }
    }

    private void initAddDisplay() {

        initTextComponents(new HashMap<>(), true);

        Button add = new Button(shell, SWT.PUSH);
        add.setText("Add");
        add.addSelectionListener(new SelectionAdapter() {
            MessageBox messageBox = new MessageBox(shell);

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                Map<ColumnData, String> data = new HashMap<>();
                for (ColumnData column : tableObject.getListOfColumns()) {
                    if (!column.isIgnoreForAddWindow()) {
                        if (!column.isDate() && !column.isCheckBox() && (((Text) textMap.get(column)).getText() == null || ((Text) textMap.get(column)).getText().length() == 0)) {
                            return;
                        }
                        formatAndPutData(data, column, dateMap, textMap);
                    }
                }
                String ans = controller.add(tableObject.getExtractor().extract(data), tableObject.getClazz());
                openMessageBoxIfNeeded(ans, messageBox, mainWindow, shell);
            }
        });
    }

    private void initUpdateDisplay(Map<ColumnData, String> oldData) {
        initTextComponents(oldData, false);

        Button update = new Button(shell, SWT.PUSH);
        update.setText("Update");
        update.addSelectionListener(new SelectionAdapter() {
            MessageBox messageBox = new MessageBox(shell);

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                Map<ColumnData, String> data = new HashMap<>();
                for (ColumnData column : tableObject.getListOfColumns()) {
                    if (!column.isIgnoreForAddWindow()) {
                        if (!column.isDate() && !column.isCheckBox() && (((Text) textMap.get(column)).getText() == null || ((Text) textMap.get(column)).getText().length() == 0)) {
                            return;
                        }
                        formatAndPutData(data, column, dateMap, textMap);
                    } else {
                        data.put(column, oldData.get(column));
                    }
                }
                String ans = controller.update(tableObject.getExtractor().extract(data), tableObject.getClazz());
                openMessageBoxIfNeeded(ans, messageBox, mainWindow, shell);
            }

        });
    }

    private static void formatAndPutData(Map<ColumnData, String> data, ColumnData column, Map<ColumnData, List<DateTime>> dateMap, Map<ColumnData, Widget> textMap) {
        if (column.isDate()) {
            DateTime date = dateMap.get(column).get(0);
            DateTime time = dateMap.get(column).get(1);
            LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth() + 1, date.getDay(), time.getHours(), time.getMinutes());
            data.put(column, String.valueOf(Timestamp.valueOf(dateTime).getTime()));
        } else {
            if (column.isCheckBox()) {
                data.put(column, String.valueOf(((Button) textMap.get(column)).getSelection()));
            } else {
                data.put(column, ((Text) textMap.get(column)).getText());
            }
        }
    }

    private static void openMessageBoxIfNeeded(String ans, MessageBox messageBox, MainWindow mainWindow, Shell shell) {
        if (ans == null) {
            mainWindow.updateData();
            shell.close();
        } else {
            messageBox.setMessage(ans);
            messageBox.open();
        }
    }

}
