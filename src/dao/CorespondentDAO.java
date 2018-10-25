package dao;

import jdbc.JDBC;
import model.Corespondent;
import model.Event;
import model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CorespondentDAO implements DAO<Long, Corespondent>, JDBC {

    private static String INSERT_CORESPONDENT = "INSERT INTO `correspondents` (surname, name, lastName, position, division, chief) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static String UPDATE_CORESPONDENT = "UPDATE `correspondents` " +
            "SET surname = ?, name = ?, lastName = ?, position = ?, division = ?, chief = ? " +
            "WHERE id = ?";

    private static String REMOVE_CORESPONDENT = "DELETE FROM `correspondents` WHERE id = ?";

    private static String SELECT_CORESPONDENT = "SELECT * FROM `correspondents` AS c " +
            "WHERE c.id = ?";

    private static String SELECT_CORESPONDENTS = "SELECT * FROM `correspondents` AS c";

    @Override
    public boolean insert(Corespondent corespondent) throws SQLException {
        return getJDBCSupport().executeUpdate(INSERT_CORESPONDENT, (ps) -> {
            int i = 0;
            ps.setString(++i, corespondent.getSurname());
            ps.setString(++i, corespondent.getName());
            ps.setString(++i, corespondent.getLastName());
            ps.setString(++i, corespondent.getPosition());
            ps.setString(++i, corespondent.getDivision());
            ps.setBoolean(++i, corespondent.isChief());
        }) != 0;
    }

    @Override
    public boolean update(Corespondent corespondent) throws SQLException {
        return getJDBCSupport().executeUpdate(UPDATE_CORESPONDENT, (ps) -> {
            int i = 0;
            ps.setString(++i, corespondent.getSurname());
            ps.setString(++i, corespondent.getName());
            ps.setString(++i, corespondent.getLastName());
            ps.setString(++i, corespondent.getPosition());
            ps.setString(++i, corespondent.getDivision());
            ps.setBoolean(++i, corespondent.isChief());
            ps.setLong(++i, corespondent.getId());
        }) != 0;
    }

    @Override
    public boolean remove(Long aLong) throws SQLException {
        return getJDBCSupport().executeUpdate(REMOVE_CORESPONDENT, (ps) -> {
            ps.setLong(1, aLong);
        }) != 0;
    }

    @Override
    public Corespondent get(Long aLong) throws SQLException {
        ResultSet rs = getJDBCSupport().executeQuery(SELECT_CORESPONDENT, (ps) -> {
            ps.setLong(1, aLong);
        });
        return extractCorespondent(rs);
    }

    @Override
    public List<Corespondent> getAll() throws SQLException {
        List<Corespondent> list = new ArrayList<>();
        ResultSet rs = getJDBCSupport().executeQuery(SELECT_CORESPONDENTS, (ps) -> {
        });
        Corespondent corespondent = extractCorespondent(rs);
        while (corespondent != null) {
            list.add(corespondent);
            corespondent = extractCorespondent(rs);
        }
        return list;
    }

    private Corespondent extractCorespondent(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Corespondent(
                    rs.getLong("c.id"),
                    rs.getString("c.name"),
                    rs.getString("c.surname"),
                    rs.getString("c.lastName"),
                    rs.getString("c.position"),
                    rs.getString("c.division"),
                    rs.getBoolean("c.chief")
            );
        } else {
            return null;
        }
    }

}
