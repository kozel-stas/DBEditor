package jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCSupport {

    private static JDBCSupport jdbcSupport = null;

    private static String url = System.getProperty("dbUrl");
    private static String user = System.getProperty("dbUser");
    private static String password = System.getProperty("dbPassword");

    private JDBCSupport() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static synchronized JDBCSupport getJDBCSupport() {
        if (jdbcSupport == null) {
            jdbcSupport = new JDBCSupport();
        }
        return jdbcSupport;
    }

    public ResultSet executeQuery(String query, PreparedStatementSetAction setAction) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setAction.setValues(preparedStatement);
            return preparedStatement.executeQuery();
        }
    }

    public int executeUpdate(String query, PreparedStatementSetAction setAction) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setAction.setValues(preparedStatement);
            int returnValue = preparedStatement.executeUpdate();
            return returnValue;
        }
    }

    public interface PreparedStatementSetAction {

        void setValues(PreparedStatement ps) throws SQLException;

    }

}

