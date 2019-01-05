package com.hmtest.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Hbase 工具类
 * @
 */
public class HbaseUtil {
    private static Connection connection;
    /**
     * 通过表名,行键值和过滤规则进行查询
     * @param tablename
     * @param rowkey
     * @param filter
     * @throws IOException
     */
    public static void ScanByFilter(String tablename,String rowkey,Filter filter) throws IOException {
        //1、创建扫描器
        Scan scan = new Scan();
        //2、将过滤器设置到扫描器中
        scan.setFilter(filter);
        //3、获取HBase的表
        Table table = connection.getTable(TableName.valueOf(tablename));

        ResultScanner scanner = table.getScanner(scan);

        for (Result result : scanner) {
            // 6.打印数据 获取所有的单元格
            List<Cell> cells = result.listCells();
            for (Cell cell : cells) {
                // 打印rowkey,family,qualifier,value
                System.out.println(Bytes.toString(CellUtil.cloneRow(cell))
                        + "==> " + Bytes.toString(CellUtil.cloneFamily(cell))
                        + "{" + Bytes.toString(CellUtil.cloneQualifier(cell))
                        + ":" + Bytes.toString(CellUtil.cloneValue(cell)) + "}");
            }
        }
    }

    /**
     * 扫描表   封装scan方法
     * 可以设置起始行键值,和列名
     * @param tablename
     * @param startKey
     * @param endKey
     * @param filter
     * @return
     * @throws IOException
     */
    public static ArrayList scan(String tablename, String startKey, String endKey, Filter filter) throws IOException {
        //获取表
        Table table = getConnection().getTable(TableName.valueOf(tablename));
        Scan scan=new Scan();
        if (startKey != null && endKey != null) {
            scan.setStartRow(Bytes.toBytes(startKey));
            scan.setStopRow(Bytes.toBytes(endKey));
        }
        if (filter != null) {
            scan.setFilter(filter);
        }
        Result result = null;
        ResultScanner scanner = table.getScanner(scan);
        ArrayList<ArrayList<Map<String, String>>> arrayLists = new ArrayList<ArrayList<Map<String, String>>>();
        while ((result = scanner.next()) != null) {
            ArrayList<Map<String, String>> value = getValue(result);
            arrayLists.add(value);
        }
        return arrayLists;
    }

    /**
     * 根据 表名 ,行键值,列簇,字段名查询数据
     * @param tablename
     * @param rowkey
     * @param cf
     * @param field
     * @return
     * @throws IOException
     */
    public static ArrayList<Map<String, String>> get(String tablename ,String rowkey , String cf,String field) throws IOException {
        Table table = getConnection().getTable(TableName.valueOf(tablename));
        Get get = new Get(Bytes.toBytes(rowkey));
        if (cf != null) {
            get.addFamily(Bytes.toBytes(cf));
        }
        if (field != null) {
            get.addColumn(Bytes.toBytes(cf), Bytes.toBytes(field));
        }
        Result result = table.get(get);
        return getValue( result);
    }


    public static ArrayList<Map<String,String>> getValue(Result result) {
        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("RowKey", Bytes.toString(CellUtil.cloneRow(cell)));
            hashMap.put("Family", Bytes.toString(CellUtil.cloneFamily(cell)));
            hashMap.put("Field", Bytes.toString(CellUtil.cloneQualifier(cell)));
            hashMap.put("Value", Bytes.toString(CellUtil.cloneValue(cell)));
            maps.add(hashMap);
        }
        return maps;
    }


    /**
     * 获取表名和行键值对应的所有数据
     * @param tablename
     * @param rowkey
     * @return
     * @throws IOException
     */
    public static List get(String tablename,String rowkey) throws IOException {
        Table userTable = getConnection().getTable(TableName.valueOf(tablename));
        // 4.1 获取数据使用，get命令
        byte[] getRowkey = Bytes.toBytes(rowkey);
        Get get = new Get(getRowkey);
        // 4.2 获取rowkey对应的所有列簇的数据
        // get 'user','rowkey_16'
        Result result = userTable.get(get);
//        userTable.get(list);
        List<Cell> cells = result.listCells();
        List<String> basedate=new ArrayList<>();
        //String str=""; //该方法可以直接返回值为string,打印rowkey,family,qualifier,value
        for (Cell cell : cells) {
//            str+=Bytes.toString(CellUtil.cloneRow(cell))
//                    + "==> " + Bytes.toString(CellUtil.cloneFamily(cell))
//                    + "{" + Bytes.toString(CellUtil.cloneQualifier(cell))
//                    + ":" + Bytes.toString(CellUtil.cloneValue(cell)) + "}"+"\n";
            basedate.add(Bytes.toString(CellUtil.cloneQualifier(cell)));
            basedate.add(Bytes.toString(CellUtil.cloneValue(cell)));
        }
        userTable.close();
        return basedate;
    }


    /**
     *
     * 向表中添加数据
     * @param tablename 表名
     * @param rowkey  行键值
     * @param cf  列簇名
     * @param field  字段,列名
     * @param value  列的值
     * @throws IOException
     */
    public static void put(String tablename ,String rowkey , String cf,String field,String value) throws IOException {
        TableName tableName = TableName.valueOf(tablename);
        //获取表
        Table user=getConnection().getTable(tableName);

        String rowKey = rowkey;
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(field), Bytes.toBytes(value));
        // 4. 添加数据
        user.put(put);
        user.close();
    }


    /**
     * 该方法创建的表必须不存在
     *String... familyname  表示可以多个值
     * @param tablename
     * @throws IOException
     */
    public static void createTable(String tablename,String... familyname) throws IOException {

        Admin admin = getConnection().getAdmin();
        TableName tableName = TableName.valueOf(tablename);

        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
        hTableDescriptor.addFamily(new HColumnDescriptor(tablename));

        admin.createTable(hTableDescriptor);
    }

    /**
     * 获取Hbase 连接
     * @return
     * @throws IOException
     */
    private static Connection getConnection() throws IOException {
        if(connection==null) {
            Configuration config = HBaseConfiguration.create();
            connection = ConnectionFactory.createConnection(config, Executors.newFixedThreadPool(30));
        }
        return connection;
    }
}

