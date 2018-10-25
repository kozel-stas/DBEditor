package view;

import controller.Controller;
import model.Corespondent;
import model.Event;
import model.Order;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.eclipse.widgets.DBTableComposite;
import org.eclipse.widgets.TableComposite;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MainWindow {
    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1517;
    private Display display;
    private Shell shell;
    private DBTableComposite<Corespondent> corespondentTable;
    private DBTableComposite<Event> eventTable;
    private DBTableComposite<Order> orderTable;
    private Controller controller = Controller.getInstance();

    public MainWindow() {
        display = new Display();
        shell = new Shell(display, SWT.TITLE | SWT.CLOSE);
        shell.setModified(false);
        shell.setSize(WIDTH, HEIGHT);
        centerWindow();

        corespondentTable = new DBTableComposite<Corespondent>(shell, Corespondent.getTableObject(), SWT.NULL, display, this);
        corespondentTable.setBounds(10, 10, corespondentTable.getSize().x, corespondentTable.getSize().y);

        eventTable = new DBTableComposite<Event>(shell, Event.getTableObject(), SWT.NULL, display, this);
        eventTable.setBounds(corespondentTable.getSize().x, 0, eventTable.getSize().x, eventTable.getSize().y);

        orderTable = new DBTableComposite<Order>(shell, Order.getTableObject(), SWT.NULL, display, this);
        corespondentTable.setBounds(corespondentTable.getSize().x + eventTable.getSize().x, 0, orderTable.getSize().x, orderTable.getSize().y);

        Button expiredEvents = new Button(shell, SWT.PUSH);
        expiredEvents.setText("Expired events");
        expiredEvents.setBounds(10, 900, 120, 50);
        expiredEvents.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                try {
                    List<Event> events = controller.getEventDAO().getAllExpiredEvents();
                    List<Order> orders = new ArrayList<>();
                    List<Corespondent> corespondents = new ArrayList<>();
                    for (Event event : events) {
                        corespondents.add(controller.getCorespondentDAO().get(event.getCorespondentID()));
                        orders.add(controller.getOrderDAO().get(event.getOrderID()));
                    }
                    corespondentTable.redraw(corespondents);
                    eventTable.redraw(events);
                    orderTable.redraw(orders);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {
            }
        });

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
        DateTime startDate = new DateTime(shell, SWT.DATE);
        startDate.setDay(dateTime.getDayOfMonth());
        startDate.setMonth(dateTime.getMonth().getValue() - 1);
        startDate.setYear(dateTime.getYear());
        startDate.setBounds(150, 900, 100, 25);

        DateTime endDate = new DateTime(shell, SWT.DATE);
        endDate.setDay(dateTime.getDayOfMonth());
        endDate.setMonth(dateTime.getMonth().getValue() - 1);
        endDate.setYear(dateTime.getYear());
        endDate.setBounds(150, 926, 100, 25);

        Button eventForDate = new Button(shell, SWT.PUSH);
        eventForDate.setText("Events for date");
        eventForDate.setBounds(270, 900, 120, 50);
        eventForDate.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                try {
                    List<Event> events = controller.getEventDAO().getEventsForPeriod(
                            Timestamp.valueOf(LocalDate.of(startDate.getYear(), startDate.getMonth() + 1, startDate.getDay()).atStartOfDay()).getTime(),
                            Timestamp.valueOf(LocalDate.of(endDate.getYear(), endDate.getMonth() + 1, endDate.getDay()).atStartOfDay()).getTime()
                    );
                    List<Order> orders = new ArrayList<>();
                    List<Corespondent> corespondents = new ArrayList<>();
                    for (Event event : events) {
                        corespondents.add(controller.getCorespondentDAO().get(event.getCorespondentID()));
                        orders.add(controller.getOrderDAO().get(event.getOrderID()));
                    }
                    corespondentTable.redraw(corespondents);
                    eventTable.redraw(events);
                    orderTable.redraw(orders);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {
            }
        });


        Combo combo = new Combo(shell, SWT.DROP_DOWN);
        combo.setBounds(410, 900, 100, 25);
        combo.add("Мероприятие");
        combo.add("Приказ");

        DateTime sortDate = new DateTime(shell, SWT.DATE);
        sortDate.setDay(dateTime.getDayOfMonth());
        sortDate.setMonth(dateTime.getMonth().getValue() - 1);
        sortDate.setYear(dateTime.getYear());
        sortDate.setBounds(410, 926, 100, 25);

        Button sortByDate = new Button(shell, SWT.PUSH);
        sortByDate.setText("Sort documents");
        sortByDate.setBounds(530, 900, 120, 50);
        sortByDate.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                if (combo.getSelectionIndex() == 0) {
                    try {
                        List<Event> events = controller.getEventDAO().getAll();
                        List<Order> orders = new ArrayList<>();
                        List<Corespondent> corespondents = new ArrayList<>();
                        LocalDate sort = LocalDate.of(sortDate.getYear(), sortDate.getMonth() + 1, sortDate.getDay());
                        Iterator<Event> iterator = events.iterator();
                        while (iterator.hasNext()) {
                            Event event = iterator.next();
                            if (LocalDate.ofInstant(Instant.ofEpochMilli(event.getStartDate()), ZoneId.systemDefault()).equals(sort)) {
                                corespondents.add(controller.getCorespondentDAO().get(event.getCorespondentID()));
                                orders.add(controller.getOrderDAO().get(event.getOrderID()));
                            } else {
                                iterator.remove();
                            }
                        }
                        corespondentTable.redraw(corespondents);
                        eventTable.redraw(events);
                        orderTable.redraw(orders);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (combo.getSelectionIndex() == 1) {
                    try {
                        List<Order> orders = controller.getOrderDAO().getAll();
                        List<Event> events = new ArrayList<>();
                        List<Corespondent> corespondents = new ArrayList<>();
                        LocalDate sort = LocalDate.of(sortDate.getYear(), sortDate.getMonth() + 1, sortDate.getDay());
                        Iterator<Order> iterator = orders.iterator();
                        while (iterator.hasNext()) {
                            Order order = iterator.next();
                            if (LocalDate.ofInstant(Instant.ofEpochMilli(order.getStartDate()), ZoneId.systemDefault()).equals(sort)) {
                                events.addAll(controller.getEventDAO().getAllForOrder(order.getId()));
                            } else {
                                iterator.remove();
                            }
                        }
                        for (Event event : events) {
                            corespondents.add(controller.getCorespondentDAO().get(event.getCorespondentID()));
                        }
                        corespondentTable.redraw(corespondents);
                        eventTable.redraw(events);
                        orderTable.redraw(orders);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {
            }
        });

        Button resetEvent = new Button(shell, SWT.PUSH);
        resetEvent.setText("View all");
        resetEvent.setBounds(670, 900, 120, 50);
        resetEvent.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                updateData();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {
            }
        });

        updateData();

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public void updateData() {
        corespondentTable.redraw(controller.getCorespondents());
        eventTable.redraw(controller.getEvents());
        orderTable.redraw(controller.getOrders());
    }

    private void centerWindow() {
        Rectangle rectangle = shell.getDisplay().getBounds();
        Point p = shell.getSize();
        int nLeft = (rectangle.width - p.x) / 2;
        int nTop = (rectangle.height - p.y) / 2;
        shell.setBounds(nLeft, nTop, p.x, p.y);
    }

}
