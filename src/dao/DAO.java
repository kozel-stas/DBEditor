package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<K, T> {

    boolean insert(T t) throws SQLException;

    boolean update(T t) throws SQLException;

    boolean remove(K k) throws SQLException;

    T get(K k) throws SQLException;

    List<T> getAll() throws SQLException;

}
