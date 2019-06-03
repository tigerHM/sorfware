package com.kafka

import java.sql.Connection
import com.mchange.v2.c3p0.ComboPooledDataSource


/**
  * 使用C3P0连接池进行数据库连接
  * @
  *
  */


class JDBCpools {}
object JDBCpools {
  private val pooledDataSource = new ComboPooledDataSource()
  private var connection: Connection = null

  /**
    * 创建数据库连接
    * @return
    */
  private def getConnection: Connection = {
    connection = pooledDataSource.getConnection
    connection
  }

  /**
    * 释放连接
    * @param connection
    */
  private def releaseConnection(connection: Connection) = {
    if( connection!= null){
      connection.close()
    }
  }

}
