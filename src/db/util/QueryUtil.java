package db.util;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caiww
 * @date 2019-02-25 11:40:08
 */
public class QueryUtil {
    /**
     * 查询数据库中所有的表
     *
     * @param conn
     * @param sql
     * @return
     */
    public List<String> queryTableName(Connection conn, String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // 通过字段检索
                list.add(rs.getString("table_name"));
            }
            // 完成后关闭
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // 处理sql查询错误
            e.printStackTrace();
        } finally {
            try {
                //关闭查询结果集
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 查询数据库中所有表的数据结构并根据表名进行分类封装
     *
     * @param conn
     * @param sql
     * @return
     */
    public Map<String, Object> queryTableStructure(Connection conn, String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, Object> tableMap = new HashMap<String, Object>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String beforeVal = "";
            while (rs.next()) {
                // 通过字段检索
                String tableName = rs.getString("table_name");
                if (!beforeVal.equals(tableName)) {
                    List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> columnMap = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsmd.getColumnName(i);
                        String columnVal = rs.getString(columnName);
                        columnMap.put(columnName, columnVal);
                    }
                    rowList.add(columnMap);
                    tableMap.put(tableName, rowList);
                    beforeVal = tableName;
                } else {
                    List<Map<String, Object>> row = (List<Map<String, Object>>) tableMap.get(tableName);
                    Map<String, Object> columnMap = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsmd.getColumnName(i);
                        String columnVal = rs.getString(columnName);
                        columnMap.put(columnName, columnVal);
                    }
                    row.add(columnMap);
                    tableMap.put(tableName, row);
                }
            }

            // 完成后关闭
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // 处理sql查询错误
            e.printStackTrace();
        } finally {
            try {
                //关闭查询结果集
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
        return tableMap;
    }

    /**
     * 查询表数据
     *
     * @param conn      数据库连接
     * @param tableName 表名称
     * @return
     */
    public List<Map<String, Object>> queryTableData(Connection conn, String tableName) {
        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String sql = "select * from " + tableName;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> columnMap = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnVal = rs.getString(columnName);
                    columnMap.put(columnName, columnVal);
                }
                list.add(columnMap);
            }
            // 完成后关闭
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // 处理sql查询错误
            e.printStackTrace();
        } finally {
            try {
                //关闭查询结果集
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取表主键字段
     *
     * @param conn         连接信息
     * @param dataBaseName 数据库名称
     * @param tableName    数据表名称
     * @return
     */
    public String getPrimary(Connection conn, String dataBaseName, String tableName) {
        Statement stmt = null;
        ResultSet rs = null;
        String primary = "id";
        try {
            String sql = "SELECT column_name FROM INFORMATION_SCHEMA.`KEY_COLUMN_USAGE` WHERE table_schema='" + dataBaseName
                    + "' AND constraint_name='PRIMARY' and table_name='" + tableName + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // 通过字段检索
                primary = rs.getString("column_name");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // 处理sql查询错误
            e.printStackTrace();
        } finally {
            try {
                //关闭查询结果集
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
        return primary;
    }
}
