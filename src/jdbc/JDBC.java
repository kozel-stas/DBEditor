package jdbc;

public interface JDBC {

    default JDBCSupport getJDBCSupport() {
        return JDBCSupport.getJDBCSupport();
    }

}
