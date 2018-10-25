package org.eclipse.widgets;

import controller.Controller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.widgets.tableComponents.TableData;
import org.eclipse.widgets.tableComponents.TableObject;
import view.MainWindow;

import java.util.HashMap;
import java.util.List;

public class DBTableComposite<T extends TableData> extends Composite {

    private static final int WIDTH = 500;
    private static final int NUMBER_OF_LINE = 30;

    private TableComposite<T> tableComposite;
    private Display display;
    private MainWindow mainWindow;
    private Controller controller = Controller.getInstance();
    private TableObject tableObject;

    public DBTableComposite(Composite composite, TableObject tableObject, int i, Display display, MainWindow mainWindow) {
        super(composite, i);
        this.display = display;
        this.mainWindow = mainWindow;
        this.tableObject = tableObject;
        Color color = new Color(null, 222, 204, 204);
        setBackground(color);
        GridLayout gridLayout = new GridLayout(19, false);
        gridLayout.numColumns = 4;
        gridLayout.verticalSpacing = 8;
        setLayout(gridLayout);

        setSize(WIDTH, 24 * (NUMBER_OF_LINE + 1) + 150);

        tableComposite = new TableComposite<T>(this, tableObject, i);
        GridData gridDataTable = new GridData();
        gridDataTable.horizontalSpan = 4;
        tableComposite.setLayoutData(gridDataTable);

        initNavigateInterface();
    }

    private void initNavigateInterface() {
        Button insert = new Button(this, SWT.PUSH);
        insert.setText("Insert");
        insert.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                new AddWindow<T>(display, mainWindow, tableObject, true, null);
            }
        });
        insert.setBounds(10, 24 * (NUMBER_OF_LINE + 1) + 100, 100, 40);

        Button update = new Button(this, SWT.PUSH);
        update.setText("Update");
        update.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                T t = tableComposite.getSelected();
                if (t != null) {
                    new AddWindow<T>(display, mainWindow, tableObject, false, t.getMapData());
                }
            }
        });
        update.setBounds(120, 24 * (NUMBER_OF_LINE + 1) + 100, 100, 40);

        Button remove = new Button(this, SWT.PUSH);
        remove.setText("Remove");
        remove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                T t = tableComposite.getSelected();
                if (t != null) {
                    String answer = controller.remove(t.getKey(), tableObject.getClazz());
                    if (answer == null) {
                        mainWindow.updateData();
                    } else {
                        MessageBox messageBox = new MessageBox(display.getShells()[0], SWT.ICON_WARNING);
                        messageBox.setMessage(answer);
                        messageBox.open();
                    }
                }
            }
        });
        remove.setBounds(230, 24 * (NUMBER_OF_LINE + 1) + 100, 100, 40);
    }

    public void redraw(List<T> list) {
        tableComposite.redraw(list);
    }
}
