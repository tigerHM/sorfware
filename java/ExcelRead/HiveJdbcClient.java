package com.zhixin.Utils;


import java.sql.*;

/**
 *
 */
public class HiveJdbcClient {
    private static String drivername="org.apache.hadoop.hive.jdbc.HiveDriver";

    public static Connection getConnnection(){
        Connection connection=null;
        try {
            connection= DriverManager.getConnection("jdbc:hive2://192.168.32.132:10000/test1","root","123456");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    /**
     *  show tables
     *  查看所有表
     */
    public static void findAlltables() {
        try {
            Statement statement=getConnnection().createStatement();
            ResultSet resultSet = statement.executeQuery("show tables");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查看表结构
     * describe table
     * @param tablename
     */


    public static void findtablesDesc(String tablename) {
        try {
            Statement statement=getConnnection().createStatement();
            statement.execute("describe "+tablename);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建表
     */
    public static void createtable(String tablename) {
        try {
            Statement statement=getConnnection().createStatement();
            ResultSet resultSet = statement.executeQuery("CREATE TABLE "+tablename+" (id INT, name STRING,age int );");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * load data into table
     * @param filepath
     * @param tableName
     */
    public static void loadFiletotable(String filepath,String tableName) {
        try {
            Statement statement=getConnnection().createStatement();
            String sql = "load data local inpath '" + filepath + "' into table " + tableName;
            statement.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
