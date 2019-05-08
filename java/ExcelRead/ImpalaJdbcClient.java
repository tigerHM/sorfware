package com.zhixin.Utils;


import com.google.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class ImpalaJdbcClient {

    private static String connectionUrl;

    private static String jdbcDriverName;

    /**
     *加载配置文件
     *
     * @throws IOException
     */
    private static Connection getConnection() {

        Connection connect=null;
        try {
            jdbcDriverName = readProperties("src/main/resources/database.properties").getProperty("impala.DriverName");
            connectionUrl = readProperties("src/main/resources/database.properties").getProperty("impala.connectionUrl");

            Class.forName(jdbcDriverName);
            connect = DriverManager.getConnection(connectionUrl);

        } catch ( Exception e){
            e.printStackTrace();
        } finally {
        }
        return connect;
    }


    /**
     * 获取配置文件中的参数
     *
     * 使用guava的Resources获取resources目录下的配置文件
     * @param file
     * @return
     * @throws IOException
     */

    public static Properties readProperties(String file) throws IOException{
        Properties properties = new Properties();
        InputStream inputStream = Resources.getResource("database.properties").openStream();
        properties.load(inputStream);
        return properties;
    }

    public static void showTables() {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet show_tables = statement.executeQuery("show tables");
            while (show_tables.next()) {
                System.out.println(show_tables.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void searchByname(String name) {
        try {
            Statement statement = getConnection().createStatement();
            //刷新元数据
            statement.execute("INVALIDATE METADATA");
            ResultSet show_tables = statement.executeQuery("select * from test where username = "+"'"+name+"'");
            while (show_tables.next()) {
                System.out.println(show_tables.getString(1)+" "+show_tables.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
