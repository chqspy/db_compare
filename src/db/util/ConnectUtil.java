package db.util;

import db.domain.DataBaseDO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author caiww
 * @date 2019-02-25 11:31:28
 */
public class ConnectUtil {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    /**
     * 连接数据库
     *
     * @param dataBaseDO
     * @return
     */
    public Connection connect(DataBaseDO dataBaseDO) {
        Connection conn = null;
        try {
            // 注册 JDBC 驱动,并连接数据库
            String db_url = dataBaseDO.getDbUrl();
            String user_name = dataBaseDO.getUserName();
            String pass_word = dataBaseDO.getPassWord();
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(db_url, user_name, pass_word);
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return conn;
    }
}
