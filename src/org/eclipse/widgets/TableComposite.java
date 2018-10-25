package org.eclipse.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.widgets.tableComponents.ColumnData;
import org.eclipse.widgets.tableComponents.TableData;
import org.eclipse.widgets.tableComponents.TableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableComposite<T extends TableData> extends Composite {

    private static final int WIDTH = 500;

    private Table table;
    private int numberLineTable = 30;
    private int currentPage = 0;
    private int maxPage = 1;
    private int maxNumberLineTable = 30;
    private Color color = new Color(null, 222, 204, 204);
    private Label labelCurrPage;
    private List<T> listOfObject;
    private TableObject test;

    public TableComposite(Composite composite, TableObject test, int i) {
        super(composite, i);
        this.test = test;
        setBackground(color);
        GridLayout gridLayout = new GridLayout(19, false);
        gridLayout.numColumns = 4;
        gridLayout.verticalSpacing = 8;
        setLayout(gridLayout);
        initTable();
        initNavigateInterface();
        setSize(WIDTH, 24 * (numberLineTable + 1) + 90);
    }

    public TableComposite(Composite composite, TableObject test, int i, int maxNumberLineTable) {
        super(composite, i);
        this.test = test;
        setBackground(color);
        GridLayout gridLayout = new GridLayout(19, false);
        gridLayout.numColumns = 4;
        gridLayout.verticalSpacing = 8;
        setLayout(gridLayout);
        this.maxNumberLineTable = maxNumberLineTable;
        numberLineTable = maxNumberLineTable;
        initTable();
        initNavigateInterface();
        setSize(WIDTH, 24 * (numberLineTable + 1) + 90);

    }

    private void initTable() {
        Table table = new Table(this, SWT.FULL_SELECTION);
        this.table = table;

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        int widthOfColumn = WIDTH / Objects.requireNonNull(test.getListOfColumns()).size();
        for (ColumnData column : test.getListOfColumns()) {
            if (column.isVisible()) {
                TableColumn tableColumnName = new TableColumn(table, SWT.CENTER);
                tableColumnName.setText(column.getName());
                tableColumnName.setResizable(false);
                tableColumnName.setWidth(widthOfColumn);
            }
        }

        GridData gridDataTable = new GridData(GridData.FILL_BOTH);
        gridDataTable.horizontalSpan = 4;
        table.setLayoutData(gridDataTable);

////////////////////////////////////////////////////////////////////////////////////table
    }

    private void initNavigateInterface() {
        labelCurrPage = new Label(this, SWT.NULL);
        labelCurrPage.setBackground(color);
        labelCurrPage.setText(String.valueOf(currentPage + 1) + '/' + String.valueOf(maxPage));
        GridData gridDataLabelCurr = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridDataLabelCurr.horizontalSpan = 2;
        labelCurrPage.setLayoutData(gridDataLabelCurr);

        Text curPage = new Text(this, SWT.SINGLE | SWT.BORDER);
        curPage.setText(String.valueOf(numberLineTable));

        Button updateButton = new Button(this, SWT.PUSH);
        updateButton.setText("UPDATE");
        updateButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                try {
                    if (Integer.parseInt(curPage.getText()) > 0 && Integer.parseInt(curPage.getText()) <= maxNumberLineTable) {
                        numberLineTable = Integer.parseInt(curPage.getText());
                        redraw();
                    }
                    curPage.setText(String.valueOf(numberLineTable));
                    labelCurrPage.setText(String.valueOf(currentPage + 1) + '/' + String.valueOf(maxPage));

                } catch (NumberFormatException ex) {
                    curPage.setText(String.valueOf(numberLineTable));
                }
            }
        });

        Button prevPageButton = new Button(this, SWT.PUSH);
        prevPageButton.setText("Prev page");
        prevPageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                if (currentPage > 0) {
                    currentPage--;
                    labelCurrPage.setText(String.valueOf(currentPage + 1) + '/');
                    redraw();
                }
            }
        });

        Button nextPageButton = new Button(this, SWT.PUSH);
        nextPageButton.setText("Next page");
        nextPageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                if (table.getItems().length == numberLineTable && currentPage + 1 < maxPage) {
                    currentPage++;
                    labelCurrPage.setText(String.valueOf(currentPage + 1));
                    redraw();
                }
            }
        });

        Button firstPage = new Button(this, SWT.PUSH);
        firstPage.setText("First page");
        firstPage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                currentPage = 0;
                redraw();
            }
        });

        Button lastPage = new Button(this, SWT.PUSH);
        lastPage.setText("Last page");
        lastPage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                currentPage = maxPage - 1;
                redraw();
            }
        });

    }

    public void setListOfObject(List<T> listOfObject) {
        this.listOfObject = listOfObject;
    }

    public void redraw(List<T> listOfObjects) {
        setListOfObject(listOfObjects);
        redraw();
    }

    public void setBounds(int i, int i1) {
        super.setBounds(i, i1, WIDTH, 24 * (numberLineTable + 1) + 90);
    }

    public void redraw() {
        if (listOfObject == null) {
            table.removeAll();
            table.redraw();
            super.redraw();
            return;
        }
        List<T> listOfVisibleObjects = new ArrayList<>();
        maxPage = (int) listOfObject.size() % numberLineTable == 0 ? listOfObject.size() / numberLineTable : listOfObject.size() / numberLineTable + 1;
        if (currentPage + 1 > maxPage) currentPage = 0;
        if (currentPage * numberLineTable < listOfObject.size())
            listOfVisibleObjects = listOfObject.subList(currentPage * numberLineTable, (currentPage + 1) * numberLineTable > listOfObject.size() ? listOfObject.size() : (currentPage + 1) * numberLineTable);
        table.removeAll();
        for (T t : listOfVisibleObjects) {
            TableItem tableItem = new TableItem(table, SWT.NULL);
            List<String> listOfData = new ArrayList<>();
            for (ColumnData column : test.getListOfColumns()) {
                if (column.isVisible()) {
                    listOfData.add(column.format(t.getMapData().get(column)));
                }
            }
            String[] list = new String[listOfData.size()];
            listOfData.toArray(list);
            tableItem.setText(list);
        }
        labelCurrPage.setText(String.valueOf(currentPage + 1) + '/' + String.valueOf(maxPage));
        table.redraw();
        super.redraw();
    }

    T getSelected() {
        if (table.getSelectionIndex() != -1) {
            int pos = currentPage * numberLineTable + table.getSelectionIndex();
            return listOfObject.get(pos);
        }
        return null;
    }
}