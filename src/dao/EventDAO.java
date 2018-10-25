package dao;

import jdbc.JDBC;
import model.Corespondent;
import model.Event;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO implements DAO<Long, Event>, JDBC {

    // language SQL
    private static String INSERT_EVENT = "INSERT INTO `events` " +
            "(name, startDate, endDate, completed, doer, `orderID`) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    // language SQL
    private static String UPDATE_EVENT = "UPDATE `events`" +
            " SET `name` = ?, `startDate = ?`, `endDate` = ?, `completed` = ?, `doer` = ?, `orderID` = ?" +
            " WHERE `id` = ?";

    // language SQL
    private static String REMOVE_EVENT = "DELETE FROM `events` WHERE id = ?";

    // language SQL
    private static String SELECT_EVENT = "SELECT * " +
            "FROM `events` AS e" +
            " JOIN correspondents c on e.doer = c.id" +
            " JOIN orders o on e.orderID = o.id" +
            " WHERE e.id = ?";

    // language SQL
    private static String SELECT_EVENTS = "SELECT * " +
            "FROM `events` AS e" +
            " JOIN correspondents c on e.doer = c.id" +
            " JOIN orders o on e.orderID = o.id ORDER BY e.startDate";

    // language SQL
    private static String SELECT_EVENTS_FOR_ORDER = "SELECT * " +
            "FROM `events` AS e" +
            " JOIN correspondents c on e.doer = c.id" +
            " JOIN orders o on e.orderID = o.id "
            + " WHERE o.id = ? ORDER BY e.startDate";

    // language SQL
    private static String SELECT_EVENTS_NOT_COMPLETED = "SELECT * " +
            "FROM `events` AS e" +
            " JOIN correspondents c on e.doer = c.id" +
            " JOIN orders o on e.orderID = o.id " +
            "WHERE completed = 0 AND e.endDate < NOW()";

    // language SQL
    private static String SELECT_EVENTS_FOR_PERIOD = "SELECT * " +
            "FROM `events` AS e" +
            " JOIN correspondents c on e.doer = c.id" +
            " JOIN orders o on e.orderID = o.id " +
            "WHERE  ? <= e.startDate AND e.startDate <= ?";

    @Override
    public boolean insert(Event event) throws SQLException {
        return getJDBCSupport().executeUpdate(INSERT_EVENT, (ps) -> {
            int i = 0;
            ps.setString(++i, event.getName());
            ps.setTimestamp(++i, new Timestamp(event.getStartDate()));
            if (event.getEndDate() != -1) {
                ps.setTimestamp(++i, new Timestamp(event.getEndDate()));
            } else {
                ps.setNull(++i, Types.TIMESTAMP);
            }
            ps.setBoolean(++i, event.isCompleted());
            ps.setLong(++i, event.getCorespondentID());
            ps.setLong(++i, event.getOrderID());
        }) != 0;
    }

    @Override
    public boolean update(Event event) throws SQLException {
        return getJDBCSupport().executeUpdate(UPDATE_EVENT, (ps) -> {
            int i = 0;
            ps.setString(++i, event.getName());
            ps.setTimestamp(++i, new Timestamp(event.getStartDate()));
            ps.setTimestamp(++i, new Timestamp(event.getEndDate()));
            ps.setBoolean(++i, event.isCompleted());
            ps.setLong(++i, event.getCorespondentID());
            ps.setLong(++i, event.getOrderID());
        }) != 0;
    }

    @Override
    public boolean remove(Long id) throws SQLException {
        return getJDBCSupport().executeUpdate(REMOVE_EVENT, (ps) -> {
            ps.setLong(1, id);
        }) != 0;
    }

    @Override
    public Event get(Long key) throws SQLException {
        ResultSet rs = getJDBCSupport().executeQuery(SELECT_EVENT, (ps) -> {
            ps.setLong(1, key);
        });
        return extractEvent(rs);
    }

    @Override
    public List<Event> getAll() throws SQLException {
        return getEvents(SELECT_EVENTS);
    }

    public List<Event> getAllExpiredEvents() throws SQLException {
        return getEvents(SELECT_EVENTS_NOT_COMPLETED);
    }

    public List<Event> getEventsForPeriod(long startDate, long endDate) throws SQLException {
        List<Event> list = new ArrayList<>();
        ResultSet rs = getJDBCSupport().executeQuery(SELECT_EVENTS_FOR_PERIOD, (ps) -> {
            ps.setTimestamp(1, new Timestamp(startDate));
            ps.setTimestamp(2, new Timestamp(endDate));
        });
        Event event = extractEvent(rs);
        while (event != null) {
            list.add(event);
            event = extractEvent(rs);
        }
        return list;
    }

    public List<Event> getAllForOrder(long order) throws SQLException {
        List<Event> list = new ArrayList<>();
        ResultSet rs = getJDBCSupport().executeQuery(SELECT_EVENTS_FOR_ORDER, (ps) -> {
            ps.setLong(1, order);
        });
        Event event = extractEvent(rs);
        while (event != null) {
            list.add(event);
            event = extractEvent(rs);
        }
        return list;
    }

    private List<Event> getEvents(String select) throws SQLException {
        List<Event> list = new ArrayList<>();
        ResultSet rs = getJDBCSupport().executeQuery(select, (ps) -> {
        });
        Event event = extractEvent(rs);
        while (event != null) {
            list.add(event);
            event = extractEvent(rs);
        }
        return list;
    }

    private Event extractEvent(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Event(
                    rs.getLong("e.id"),
                    rs.getString("e.name"),
                    rs.getTimestamp("e.startDate").getTime(),
                    rs.getTimestamp("e.endDate").getTime(),
                    rs.getLong("c.id"),
                    rs.getLong("o.id"),
                    rs.getBoolean("e.completed")
            );
        } else {
            return null;
        }
    }

}
