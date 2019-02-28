package db.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caiww
 * @date 2019-02-25 14:05:12
 */
public class CompareUtil {

    /**
     * 比较两个数据库中表的差异性并返回差异性集合
     *
     * @param dataBaseName1 数据库1数据库名称
     * @param dataBaseName2 数据库2数据库名称
     * @param list1         数据库1所有表名称集合
     * @param list2         数据库2所有表名称集合
     * @return
     */
    public Map<String, Object> compareTable(String dataBaseName1, String dataBaseName2, List<String> list1, List<String> list2) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String maxListName = dataBaseName1;
        String minListName = dataBaseName2;
        List<String> maxList = list1;
        List<String> minList = list2;
        if (list1.size() < list2.size()) {
            maxList = list2;
            minList = list1;
            maxListName = dataBaseName2;
            minListName = dataBaseName1;
        }
        for (String string : maxList) {
            map.put(string, maxListName);
        }
        for (String string : minList) {
            Object compareStr = map.get(string);
            if (compareStr != null) {
                map.put(string, 1);
            } else {
                map.put(string, minListName);
            }
        }
        List<String> dblist1 = new ArrayList<String>();
        List<String> dblist2 = new ArrayList<String>();
        List<String> commondblist = new ArrayList<String>();
        map.entrySet().stream().forEach(e -> {
            if (e.getValue().equals(dataBaseName1)) {
                dblist1.add(e.getKey());
            } else if (e.getValue().equals(dataBaseName2)) {
                dblist2.add(e.getKey());
            } else {
                commondblist.add(e.getKey());
            }
        });
        resultMap.put("dblist1", dblist1);   //数据库1新增的表名称
        resultMap.put("dblist2", dblist2);   //数据库2新增的表名称
        resultMap.put("commondblist", commondblist);
        return resultMap;
    }

    /**
     * 比较两个数据库中表数据结构的差异性并返回差异性结构集合
     *
     * @param fieldStr      所查询的数据结构字段
     * @param tableName     数据表名称
     * @param dataBaseName1 数据库1数据库名称
     * @param dataBaseName2 数据库2数据库名称
     * @param list1         数据库1某张表的数据结构集合
     * @param list2         数据库2某张表的数据结构集合
     * @return
     */
    public Map<String, Object> compareTableStructure(String fieldStr, String tableName, String dataBaseName1, String dataBaseName2, List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String maxListName = dataBaseName1;
        String minListName = dataBaseName2;
        List<Map<String, Object>> maxList = list1;
        List<Map<String, Object>> minList = list2;
        List<String> maxListColumn = new ArrayList<String>();
        if (list1.size() < list2.size()) {
            maxList = list2;
            minList = list1;
            maxListName = dataBaseName2;
            minListName = dataBaseName1;
        }
        for (Map<String, Object> trMap : maxList) {
            map.put(trMap.get("COLUMN_NAME").toString(), maxListName);
            dataMap.put(trMap.get("COLUMN_NAME").toString(), trMap);
        }
        for (Map<String, Object> trMap : minList) {
            Object compareStr = map.get(trMap.get("COLUMN_NAME").toString());
            if (compareStr != null) {
                map.put(trMap.get("COLUMN_NAME").toString(), 1);
            } else {
                map.put(trMap.get("COLUMN_NAME").toString(), minListName);
                dataMap.put(trMap.get("COLUMN_NAME").toString(), trMap);
            }
        }
        List<Map<String, Object>> dblist1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> dblist2 = new ArrayList<Map<String, Object>>();
        List<String> commondblist = new ArrayList<String>();
        //获取表中不相同的字段名称及结构
        map.entrySet().stream().forEach(e -> {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            if (e.getValue().equals(dataBaseName1)) {
                tempMap.put(e.getKey().toString(), dataMap.get(e.getKey()));
                dblist1.add(tempMap);
            } else if (e.getValue().equals(dataBaseName2)) {
                tempMap.put(e.getKey().toString(), dataMap.get(e.getKey()));
                dblist2.add(tempMap);
            } else {
                commondblist.add(e.getKey());
            }
        });
        //根据数据库封装对应的字段结构
        Map<String, Object> fieldMap1 = new HashMap<String, Object>();
        Map<String, Object> fieldMap2 = new HashMap<String, Object>();
        for (Map<String, Object> entryMap : list1) {
            fieldMap1.put(entryMap.get("COLUMN_NAME").toString(), entryMap);
        }
        for (Map<String, Object> entryMap : list2) {
            fieldMap2.put(entryMap.get("COLUMN_NAME").toString(), entryMap);
        }
        //比较表中相同的字段名称的值
        List<String> fieldList1 = new ArrayList<String>();
        List<String> fieldList2 = new ArrayList<String>();
        List<String> commomFieldList = new ArrayList<String>();
        commondblist.stream().forEach(e -> {
            Boolean flag = true;
            String msg1 = "";
            String msg2 = "";
            Map<String, Object> dbMap1 = (Map<String, Object>) fieldMap1.get(e);
            Map<String, Object> dbMap2 = (Map<String, Object>) fieldMap2.get(e);
            String[] filedArr = fieldStr.split(",");
            for (String filedString : filedArr) {
                if (dbMap1.get(filedString) == null && dbMap2.get(filedString) == null) {
                    //值相同不做任何操作
                    continue;
                } else if (dbMap1.get(filedString) != null && dbMap2.get(filedString) != null) {
                    if (StringUtils.isEmpty(dbMap1.get(filedString).toString()) && StringUtils.isEmpty(dbMap2.get(filedString).toString())) {
                        //值相同不做任何操作
                        continue;
                    } else {
                        String val1 = dbMap1.get(filedString).toString();
                        String val2 = dbMap2.get(filedString).toString();
                        if (val1.equals(val2)) {
                            //值相同不做任何操作
                            continue;
                        } else {
                            //值不同
                            flag = false;
                            msg1 += "," + filedString + ":" + val1;
                            msg2 += "," + filedString + ":" + val2;
                        }
                    }

                } else {
                    String val1 = dbMap1.get(filedString) == null ? "null" : dbMap1.get(filedString).toString();
                    String val2 = dbMap2.get(filedString) == null ? "null" : dbMap2.get(filedString).toString();
                    if (val1.equals(val2)) {
                        //值相同不做任何操作
                        continue;
                    } else {
                        //值不同
                        flag = false;
                        msg1 += "," + filedString + ":" + val1;
                        msg2 += "," + filedString + ":" + val2;
                    }
                }
            }
            if (StringUtils.isNotEmpty(msg1)) {
                msg1 = msg1.substring(1);
                fieldList1.add("字段名称" + e + "={" + msg1 + "}");
            }
            if (StringUtils.isNotEmpty(msg2)) {
                msg2 = msg2.substring(1);
                fieldList2.add("字段名称" + e + "={" + msg2 + "}");
            }
            if (flag) {
                commomFieldList.add(e);
            }
        });
        resultMap.put("dbField1", dblist1);  //数据库1新增的字段名称
        resultMap.put("dbField2", dblist2);  //数据库2新增的字段名称
        resultMap.put("commomFieldList", commondblist);   //数据库1、2中字段结构相同的字段名称
        resultMap.put("commondbField1", fieldList1);   //数据库1中字段结构不同的字段名称及对应结构
        resultMap.put("commondbField2", fieldList2);   //数据库2中字段结构不同的字段名称及对应结构
        return resultMap;
    }

    /**
     * 获取字段结构相同的表名称集合
     *
     * @param tableList     两个数据库中相同的表名称的集合
     * @param map1          数据表1的所有表结构
     * @param map2          数据表2的所有表结构
     * @param dataBaseName1 数据库1数据库名称
     * @param dataBaseName2 数据库2数据库名称
     * @return
     */
    public List<String> getSameTable(List<String> tableList, Map<String, Object> map1, Map<String, Object> map2, String dataBaseName1, String dataBaseName2) {
        List<String> commonTableNameList = new ArrayList<String>();
        for (String tableName : tableList) {
            List<Map<String, Object>> list1 = (List<Map<String, Object>>) map1.get(tableName);
            List<Map<String, Object>> list2 = (List<Map<String, Object>>) map2.get(tableName);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> dataMap = new HashMap<String, Object>();
            String maxListName = dataBaseName1;
            String minListName = dataBaseName2;
            List<Map<String, Object>> maxList = list1;
            List<Map<String, Object>> minList = list2;
            List<String> maxListColumn = new ArrayList<String>();
            if (list1.size() < list2.size()) {
                maxList = list2;
                minList = list1;
                maxListName = dataBaseName2;
                minListName = dataBaseName1;
            }
            for (Map<String, Object> trMap : maxList) {
                map.put(trMap.get("COLUMN_NAME").toString(), maxListName);
                dataMap.put(trMap.get("COLUMN_NAME").toString(), trMap);
            }
            for (Map<String, Object> trMap : minList) {
                Object compareStr = map.get(trMap.get("COLUMN_NAME").toString());
                if (compareStr != null) {
                    map.put(trMap.get("COLUMN_NAME").toString(), 1);
                } else {
                    map.put(trMap.get("COLUMN_NAME").toString(), minListName);
                    dataMap.put(trMap.get("COLUMN_NAME").toString(), trMap);
                }
            }
            List<Map<String, Object>> dblist1 = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> dblist2 = new ArrayList<Map<String, Object>>();
            List<String> commondblist = new ArrayList<String>();
            //获取表中不相同的字段名称及结构
            map.entrySet().stream().forEach(e -> {
                Map<String, Object> tempMap = new HashMap<String, Object>();
                if (e.getValue().equals(dataBaseName1)) {
                    tempMap.put(e.getKey().toString(), dataMap.get(e.getKey()));
                    dblist1.add(tempMap);
                } else if (e.getValue().equals(dataBaseName2)) {
                    tempMap.put(e.getKey().toString(), dataMap.get(e.getKey()));
                    dblist2.add(tempMap);
                } else {
                    commondblist.add(e.getKey());
                }
            });
            if (dblist1.size() == 0 && dblist2.size() == 0) {
                commonTableNameList.add(tableName);
            }
        }
        return commonTableNameList;
    }

    /**
     * 比较两个数据表中数据的差异性
     *
     * @param dataBaseName1 数据库1数据库名称
     * @param dataBaseName2 数据库2数据库名称
     * @param primary1      数据表1主键字段名称
     * @param primary2      数据表2主键字段名称
     * @param list1         数据表1数据集合
     * @param list2         数据表2数据集合
     * @return
     */
    public Map<String, Object> compareTableData(String dataBaseName1, String dataBaseName2, String primary1, String primary2, List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
        Map<String, Object> resulteMap = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> dataMap1 = new HashMap<String, Object>();
        Map<String, Object> dataMap2 = new HashMap<String, Object>();
        String maxListName = dataBaseName1;
        String minListName = dataBaseName2;
        String maxPrimary = primary1;
        String minPrimary = primary2;
        List<Map<String, Object>> maxList = list1;
        List<Map<String, Object>> minList = list2;
        if (list1.size() < list2.size()) {
            maxList = list2;
            minList = list1;
            maxListName = dataBaseName2;
            minListName = dataBaseName1;
            maxPrimary = primary2;
            minPrimary = primary1;
        }
        for (Map<String, Object> rowMap : maxList) {
            map.put(rowMap.get(maxPrimary).toString(), maxListName);
            dataMap.put(rowMap.get(maxPrimary).toString(), rowMap);
        }
        for (Map<String, Object> rowMap : minList) {
            Object compareStr = map.get(rowMap.get(minPrimary));
            if (compareStr != null) {
                map.put(rowMap.get(minPrimary).toString(), 1);
            } else {
                map.put(rowMap.get(minPrimary).toString(), minListName);
                dataMap.put(rowMap.get(minPrimary).toString(), rowMap);
            }
        }
        //根据主键封装数据
        list1.stream().forEach(e -> {
            dataMap1.put(e.get(primary1).toString(), e);
        });
        list2.stream().forEach(e -> {
            dataMap2.put(e.get(primary2).toString(), e);
        });
        List<Map<String, Object>> dblist1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> dblist2 = new ArrayList<Map<String, Object>>();
        List<String> commondblist = new ArrayList<String>();
        //根据主键获取数据行差异
        map.entrySet().stream().forEach(e -> {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            if (e.getValue().equals(dataBaseName1)) {
                tempMap.put(e.getKey().toString(), dataMap.get(e.getKey()));
                dblist1.add(tempMap);
            } else if (e.getValue().equals(dataBaseName2)) {
                tempMap.put(e.getKey().toString(), dataMap.get(e.getKey()));
                dblist2.add(tempMap);
            } else {
                commondblist.add(e.getKey());
            }
        });
        //比较主键值相同的数据行内容
        List<String> datalist1 = new ArrayList<String>();
        List<String> datalist2 = new ArrayList<String>();
        commondblist.stream().forEach(e -> {
            String msg1 = "";
            String msg2 = "";
            Map<String, Object> map1 = (Map<String, Object>) dataMap1.get(e);
            Map<String, Object> map2 = (Map<String, Object>) dataMap2.get(e);
            for (Map.Entry<String, Object> entry : map1.entrySet()) {
                String data1 = entry.getValue() == null ? "null" : entry.getValue().toString();
                String data2 = map2.get(entry.getKey()) == null ? "null" : map2.get(entry.getKey()).toString();
                if (!data1.equals(data2)) {
                    msg1 += "," + entry.getKey() + ":" + data1;
                    msg2 += "," + entry.getKey() + ":" + data2;
                }
            }
            if (StringUtils.isNotEmpty(msg1)) {
                msg1 = msg1.substring(1);
                datalist1.add(primary1 + "=" + e + ",{" + msg1 + "}");
            }
            if (StringUtils.isNotEmpty(msg2)) {
                msg2 = msg2.substring(2);
                datalist2.add(primary2 + "=" + e + ",{" + msg2 + "}");
            }
        });
        resulteMap.put("dblist1", dblist1);
        resulteMap.put("dblist2", dblist2);
        resulteMap.put("datalist1", datalist1);
        resulteMap.put("datalist2", datalist2);
        return resulteMap;
    }
}
