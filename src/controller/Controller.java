package controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import dao.CorespondentDAO;
import dao.DAO;
import dao.EventDAO;
import dao.OrderDAO;
import model.Corespondent;
import model.Event;
import model.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Controller {

    private static Controller controller;

    public synchronized static Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    private EventDAO eventDAO;
    private OrderDAO orderDAO;
    private CorespondentDAO corespondentDAO;

    //TODO rework.
    private Map<Class, DAO> mapOfClass;

    private Controller() {
        eventDAO = new EventDAO();
        orderDAO = new OrderDAO();
        corespondentDAO = new CorespondentDAO();
        mapOfClass = Map.of(Event.class, eventDAO, Order.class, orderDAO, Corespondent.class, corespondentDAO);
    }

    public List<Event> getEvents() {
        try {
            return eventDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Order> getOrders() {
        try {
            return orderDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Corespondent> getCorespondents() {
        try {
            return corespondentDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String remove(long key, Class clazz) {
        DAO dao = mapOfClass.get(clazz);
        if (dao != null) {
            try {
                dao.remove(key);
                return null;
            } catch (MySQLIntegrityConstraintViolationException ex) {
                return "Зависимость между записями, удалите все связи!!";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return "Непредвиденная ошибка";
    }

    public String add(Object object, Class clazz) {
        DAO dao = mapOfClass.get(clazz);
        if (dao != null) {
            try {
                if (dao.insert(object)) {
                    return null;
                } else {
                    return "Данные не валидны";
                }
            } catch (MySQLIntegrityConstraintViolationException ex) {
                return "Не удалось установить зависимости между объектами.";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return "Непредвиденная ошибка.";
    }

    public String update(Object object, Class clazz) {
        DAO dao = mapOfClass.get(clazz);
        if (dao != null) {
            try {
                if (dao.update(object)) {
                    return null;
                } else {
                    return "Данные не валидны";
                }
            } catch (MySQLIntegrityConstraintViolationException ex) {
                return "Не удалось установить зависимости между объектами.";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return "Непредвиденная ошибка.";
    }

    public EventDAO getEventDAO() {
        return eventDAO;
    }

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public CorespondentDAO getCorespondentDAO() {
        return corespondentDAO;
    }

}
