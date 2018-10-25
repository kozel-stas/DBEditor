package dao;

import jdbc.JDBC;
import model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements DAO<Long, Order>, JDBC {

    // language SQL
    private static String INSERT_ORDER = "INSERT INTO pbz.`orders` " +
            "(content, startDate, owner) " +
            "SELECT " +
            "?, " +
            "?, " +
            "c.id " +
            "FROM pbz.correspondents AS c " +
            "WHERE id = ? AND chief = 1";

    // language SQL
    private static String UPDATE_ORDER = "UPDATE `orders`" +
            " SET `content` = ?" +
            " WHERE `id` = ?";

    // language SQL
    private static String REMOVE_ORDER = "DELETE FROM `orders` WHERE id = ?";

    // language SQL
    private static String SELECT_ORDER = "SELECT * FROM `orders` AS o " +
            "WHERE id = ?";

    // language SQL
    private static String SELECT_ORDERS = "SELECT * FROM `orders` AS o ORDER BY startDate";

    @Override
    public boolean insert(Order order) throws SQLException {
        return getJDBCSupport().executeUpdate(INSERT_ORDER, (ps) -> {
            int i = 0;
            ps.setString(++i, order.getContent());
            ps.setTimestamp(++i, new Timestamp(order.getStartDate()));
            ps.setLong(++i, order.getOwner());
        }) != 0;
    }

    @Override
    public boolean update(Order order) throws SQLException {
        return getJDBCSupport().executeUpdate(UPDATE_ORDER, (ps) -> {
            int i = 0;
            ps.setString(++i, order.getContent());
            ps.setLong(++i, order.getId());
        }) != 0;
    }

    @Override
    public boolean remove(Long key) throws SQLException {
        return getJDBCSupport().executeUpdate(REMOVE_ORDER, (ps) -> {
            ps.setLong(1, key);
        }) != 0;
    }

    @Override
    public Order get(Long key) throws SQLException {
        ResultSet rs = getJDBCSupport().executeQuery(SELECT_ORDER, (ps) -> {
            ps.setLong(1, key);
        });
        return extractOrder(rs);
    }

    @Override
    public List<Order> getAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        ResultSet rs = getJDBCSupport().executeQuery(SELECT_ORDERS, (ps) -> {
        });
        Order order = extractOrder(rs);
        while (order != null) {
            list.add(order);
            order = extractOrder(rs);
        }
        return list;
    }

    private Order extractOrder(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Order(
                    rs.getLong("o.id"),
                    rs.getString("o.content"),
                    rs.getTimestamp("o.startDate").getTime(),
                    rs.getLong("o.owner")
            );
        } else {
            return null;
        }
    }

}
