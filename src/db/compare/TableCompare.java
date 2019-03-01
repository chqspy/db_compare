package db.compare;

import db.domain.DataBaseDO;
import db.util.CompareUtil;
import db.util.ConnectUtil;
import db.util.QueryUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author caiww
 * @date 2019-02-25 13:30:44
 */
public class TableCompare {

    private CompareUtil compareUtil = new CompareUtil();

    private static final String tableNameSql = "select table_name from information_schema.tables";

    /**
     * 比较两个数据库中表的差异性
     *
     * @param dataBaseDO1   数据库1连接信息
     * @param dataBaseName1 数据库1数据库名称
     * @param dataBaseDO2   数据库2连接信息
     * @param dataBaseName2 数据库2数据库名称
     */
    public String compareTableName(DataBaseDO dataBaseDO1, String dataBaseName1, DataBaseDO dataBaseDO2, String dataBaseName2) {
        String sql1 = tableNameSql + " where table_schema='" + dataBaseName1 + "' order by table_name asc";
        String sql2 = tableNameSql + " where table_schema='" + dataBaseName2 + "' order by table_name asc";
        Connection conn1 = null;
        Connection conn2 = null;
        try {
            String msg = "";
            ConnectUtil connectUtil = new ConnectUtil();
            QueryUtil queryUtil = new QueryUtil();
            conn1 = connectUtil.connect(dataBaseDO1);
            conn2 = connectUtil.connect(dataBaseDO2);
            List<String> list1 = queryUtil.queryTableName(conn1, sql1);
            List<String> list2 = queryUtil.queryTableName(conn2, sql2);
            //获取两个数据库中表的差异化数据
            Map<String, Object> resultMap = compareUtil.compareTable(dataBaseName1, dataBaseName2, list1, list2);
            List<String> dblist1 = (List<String>) resultMap.get("dblist1");
            List<String> dblist2 = (List<String>) resultMap.get("dblist2");
            int dblistSize1 = dblist1.size();
            int dblistSize2 = dblist2.size();
            if (dblistSize1 > 0) {
                msg += dataBaseName1 + "新增的表:\n";
                StringJoiner stringJoiner = new StringJoiner("\n");
                dblist1.stream().forEach(e -> {
                    stringJoiner.add(e);
                });
                msg += stringJoiner.toString() + "\n";
            }
            if (dblistSize2 > 0) {
                msg += dataBaseName2 + "新增的表:\n";
                StringJoiner stringJoiner = new StringJoiner("\n");
                dblist2.stream().forEach(e -> {
                    stringJoiner.add(e);
                });
                msg += stringJoiner.toString() + "\n";
            }
            if (dblistSize1 == 0 && dblistSize2 == 0) {
                msg += "两个数据库中的表一致" + "\n";
            }
            return msg;
        } finally {
            try {
                //关闭DB连接
                if (conn1 != null) {
                    conn1.close();
                }
                if (conn2 != null) {
                    conn2.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * 比较两个数据库中表数据结构的差异性
     *
     * @param dataBaseDO1   数据库1连接信息
     * @param dataBaseName1 数据库1数据库名称
     * @param dataBaseDO2   数据库2连接信息
     * @param dataBaseName2 数据库2数据库名称
     */
    public String compareTableStructure(DataBaseDO dataBaseDO1, String dataBaseName1, DataBaseDO dataBaseDO2, String dataBaseName2) {
        //要比较的数据表结构字段集合
        String fieldStr = "TABLE_NAME,COLUMN_NAME,ORDINAL_POSITION,COLUMN_DEFAULT,IS_NULLABLE,DATA_TYPE," +
                "CHARACTER_MAXIMUM_LENGTH,CHARACTER_OCTET_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE," +
                "DATETIME_PRECISION,CHARACTER_SET_NAME,COLLATION_NAME,COLUMN_TYPE,COLUMN_KEY,EXTRA," +
                "PRIVILEGES,COLUMN_COMMENT,GENERATION_EXPRESSION";
        String sql1 = "select " + fieldStr + " from information_schema.columns" +
                " where table_schema ='" + dataBaseName1 + "' order by TABLE_NAME asc,COLUMN_NAME asc";
        String sql2 = "select " + fieldStr + " from information_schema.columns" +
                " where table_schema ='" + dataBaseName2 + "' order by TABLE_NAME asc,COLUMN_NAME asc";
        String tablesql1 = tableNameSql + " where table_schema='" + dataBaseName1 + "' order by table_name asc";
        String tablesql2 = tableNameSql + " where table_schema='" + dataBaseName2 + "' order by table_name asc";
        Connection conn1 = null;
        Connection conn2 = null;
        try {
            String msg = "";
            ConnectUtil connectUtil = new ConnectUtil();
            QueryUtil queryUtil = new QueryUtil();
            conn1 = connectUtil.connect(dataBaseDO1);
            conn2 = connectUtil.connect(dataBaseDO2);
            List<String> list1 = queryUtil.queryTableName(conn1, tablesql1);
            List<String> list2 = queryUtil.queryTableName(conn2, tablesql2);
            Map<String, Object> resultMap = compareUtil.compareTable(dataBaseName1, dataBaseName2, list1, list2);
            Map<String, Object> map1 = queryUtil.queryTableStructure(conn1, sql1);
            Map<String, Object> map2 = queryUtil.queryTableStructure(conn2, sql2);

            //根据相同的表名进行表结构比对
            for (String tableName : (List<String>) resultMap.get("commondblist")) {
                List<Map<String, Object>> dbList1 = (List<Map<String, Object>>) map1.get(tableName);
                List<Map<String, Object>> dbList2 = (List<Map<String, Object>>) map2.get(tableName);
                Map<String, Object> reMap = compareUtil.compareTableStructure(fieldStr, tableName, dataBaseName1, dataBaseName2, dbList1, dbList2);
                List<Map<String, Object>> dbField1 = (List<Map<String, Object>>) reMap.get("dbField1");  //数据库1新增的字段名称
                List<Map<String, Object>> dbField2 = (List<Map<String, Object>>) reMap.get("dbField2");  //数据库2新增的字段名称
                List<String> commomFieldList = (List<String>) reMap.get("commomFieldList");  //数据库1、2中字段结构相同的字段名称
                List<String> commondbField1 = (List<String>) reMap.get("commondbField1");    //数据库1中字段结构不同的字段名称及对应结构
                List<String> commondbField2 = (List<String>) reMap.get("commondbField2");   //数据库2中字段结构不同的字段名称及对应结构
                int dbFieldSize1 = dbField1.size();
                int dbFieldSize2 = dbField2.size();
                int commondbFieldSize1 = commondbField1.size();
                int commondbFieldSize2 = commondbField2.size();
                if (dbFieldSize1 > 0) {
                    msg += dataBaseName1 + "." + tableName + "新增的字段:\n";
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    dbField1.stream().forEach(e -> {
                        stringJoiner.add(e.toString());
                    });
                    msg += stringJoiner.toString() + "\n";
                }
                if (dbFieldSize2 > 0) {
                    msg += dataBaseName2 + "." + tableName + "新增的字段:\n";
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    dbField2.stream().forEach(e -> {
                        stringJoiner.add(e.toString());
                    });
                    msg += stringJoiner.toString() + "\n";
                }
                if (commondbFieldSize1 > 0) {
                    msg += dataBaseName1 + "." + tableName + "不同于数据库" + dataBaseName2 + "." + tableName + "结构的字段:\n";
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    commondbField1.stream().forEach(e -> {
                        stringJoiner.add(e);
                    });
                    msg += stringJoiner.toString() + "\n";
                }
                if (commondbFieldSize2 > 0) {
                    msg += dataBaseName2 + "." + tableName + "不同于数据库" + dataBaseName1 + "." + tableName + "结构的字段:\n";
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    commondbField2.stream().forEach(e -> {
                        stringJoiner.add(e);
                    });
                    msg += stringJoiner.toString() + "\n";
                }
                if (dbFieldSize1 == 0 && dbFieldSize2 == 0 && commondbFieldSize1 == 0 && commondbFieldSize2 == 0) {
                    msg += tableName + "数据结构一致\n";
                }
            }
            return msg;
        } finally {
            try {
                //关闭DB连接
                if (conn1 != null) {
                    conn1.close();
                }
                if (conn2 != null) {
                    conn2.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    /**
     * 比较两个数据库中表数据的差异性
     *
     * @param dataBaseDO1   数据库1连接信息
     * @param dataBaseName1 数据库1数据库名称
     * @param dataBaseDO2   数据库2连接信息
     * @param dataBaseName2 数据库2数据库名称
     */
    public String compareTableData(DataBaseDO dataBaseDO1, String dataBaseName1, DataBaseDO dataBaseDO2, String dataBaseName2) {
        String fieldStr = "TABLE_NAME,COLUMN_NAME,ORDINAL_POSITION,COLUMN_DEFAULT,IS_NULLABLE,DATA_TYPE," +
                "CHARACTER_MAXIMUM_LENGTH,CHARACTER_OCTET_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE," +
                "DATETIME_PRECISION,CHARACTER_SET_NAME,COLLATION_NAME,COLUMN_TYPE,COLUMN_KEY,EXTRA," +
                "PRIVILEGES,COLUMN_COMMENT,GENERATION_EXPRESSION";
        String sql1 = "select " + fieldStr + " from information_schema.columns" +
                " where table_schema ='" + dataBaseName1 + "' order by TABLE_NAME asc,COLUMN_NAME asc";
        String sql2 = "select " + fieldStr + " from information_schema.columns" +
                " where table_schema ='" + dataBaseName2 + "' order by TABLE_NAME asc,COLUMN_NAME asc";
        String tablesql1 = tableNameSql + " where table_schema='" + dataBaseName1 + "' order by table_name asc";
        String tablesql2 = tableNameSql + " where table_schema='" + dataBaseName2 + "' order by table_name asc";
        Connection conn1 = null;
        Connection conn2 = null;
        try {
            String msg = "";
            ConnectUtil connectUtil = new ConnectUtil();
            QueryUtil queryUtil = new QueryUtil();
            conn1 = connectUtil.connect(dataBaseDO1);
            conn2 = connectUtil.connect(dataBaseDO2);
            List<String> list1 = queryUtil.queryTableName(conn1, tablesql1);
            List<String> list2 = queryUtil.queryTableName(conn2, tablesql2);
            Map<String, Object> resultMap = compareUtil.compareTable(dataBaseName1, dataBaseName2, list1, list2);
            Map<String, Object> map1 = queryUtil.queryTableStructure(conn1, sql1);
            Map<String, Object> map2 = queryUtil.queryTableStructure(conn2, sql2);
            //获取字段结构相同的表名称集合
            List<String> commonTableNameList = compareUtil.getSameTable((List<String>) resultMap.get("commondblist"), map1, map2, dataBaseName1, dataBaseName2);
            for (String tableName : commonTableNameList) {
                List<Map<String, Object>> tableDataList1 = queryUtil.queryTableData(conn1, tableName);
                List<Map<String, Object>> tableDataList2 = queryUtil.queryTableData(conn2, tableName);
                String primary1 = queryUtil.getPrimary(conn1, dataBaseName1, tableName);
                String primary2 = queryUtil.getPrimary(conn2, dataBaseName2, tableName);
                Map<String, Object> reMap = compareUtil.compareTableData(dataBaseName1, dataBaseName2, primary1, primary2, tableDataList1, tableDataList2);
                List<Map<String, Object>> dblist1 = (List<Map<String, Object>>) reMap.get("dblist1");
                List<Map<String, Object>> dblist2 = (List<Map<String, Object>>) reMap.get("dblist2");
                List<String> datalist1 = (List<String>) reMap.get("datalist1");
                List<String> datalist2 = (List<String>) reMap.get("datalist2");
                int dblistSize1 = dblist1.size();
                int dblistSize2 = dblist2.size();
                int datalistSize1 = datalist1.size();
                int datalistSize2 = datalist2.size();
                if (dblistSize1 == 0 && dblistSize2 == 0 && datalistSize1 == 0 && datalistSize2 == 0) {
                    msg += tableName + "数据表数据一致\n";
                }
                if (dblistSize1 > 0) {
                    msg += dataBaseName1 + "." + tableName + "新增的数据:\n";
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    dblist1.stream().forEach(e -> {
                        e.entrySet().stream().forEach(obj -> {
                            stringJoiner.add(obj.getValue().toString());
                        });
                    });
                    msg += stringJoiner.toString() + "\n";
                }
                if (dblistSize2 > 0) {
                    msg += dataBaseName2 + "." + tableName + "新增的数据:\n";
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    dblist2.stream().forEach(e -> {
                        e.entrySet().stream().forEach(obj -> {
                            stringJoiner.add(obj.getValue().toString());
                        });
                    });
                    msg += stringJoiner.toString() + "\n";
                }
                if (datalistSize1 > 0) {
                    msg += dataBaseName1 + "." + tableName + "不同于数据库" + dataBaseName2 + "." + tableName + "数据字段数据:\n";
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    datalist1.stream().forEach(e -> {
                        stringJoiner.add(e);
                    });
                    msg += stringJoiner.toString() + "\n";
                }
                if (datalistSize2 > 0) {
                    msg += dataBaseName2 + "." + tableName + "不同于数据库" + dataBaseName1 + "." + tableName + "数据字段数据:\n";
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    datalist2.stream().forEach(e -> {
                        stringJoiner.add(e);
                    });
                    msg += stringJoiner.toString() + "\n";
                }
            }
            return msg;
        } finally {
            try {
                //关闭DB连接
                if (conn1 != null) {
                    conn1.close();
                }
                if (conn2 != null) {
                    conn2.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
