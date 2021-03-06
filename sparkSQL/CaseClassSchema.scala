package com.hmtest.SparkSQLDemo

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * @
  *注意spark-core和spark-sqlpom的版本号一致
  */
case class Person (id:Int,name:String,age:Int)
object CaseClassSchema {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("CaseClassSchema")
      .master("local[2]").getOrCreate()
    //todo:2、从sparkSession获取sparkContext对象
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")//设置日志输出级别
    val dataRDD: RDD[String] = sc.textFile("D:\\temp\\user.txt")
    //todo:4、切分每一行记录
    val lineArrayRDD: RDD[Array[String]] = dataRDD.map(_.split(" "))
    //todo:5、将RDD与Person类关联
    val personRDD: RDD[Person] = lineArrayRDD.map(x=>Person(x(0).toInt,x(1),x(2).toInt))

    import spark.implicits._
    val personDF: DataFrame = personRDD.toDF()

    //todo-------------------DSL语法操作 start--------------
    //1、显示DataFrame的数据，默认显示20行
    personDF.show()
    //2、显示DataFrame的schema信息
    personDF.printSchema()
    //3、显示DataFrame记录数
    println(personDF.count())
    //4、显示DataFrame的所有字段
    personDF.columns.foreach(println)
    //5、取出DataFrame的第一行记录
    println(personDF.head())
    //6、显示DataFrame中name字段的所有值
    personDF.select("name").show()
    //7、过滤出DataFrame中年龄大于30的记录
    personDF.filter($"age" > 30).show()
    //8、统计DataFrame中年龄大于30的人数
    println(personDF.filter($"age">30).count())
    //9、统计DataFrame中按照年龄进行分组，求每个组的人数
    personDF.groupBy("age").count().show()
    //todo-------------------DSL语法操作 end-------------

    //todo--------------------SQL操作风格 start-----------
    //todo:将DataFrame注册成表
    personDF.createOrReplaceTempView("t_person")
    //todo:传入sql语句，进行操作

    spark.sql("select * from t_person").show()

    spark.sql("select * from t_person where name='zhangsan'").show()

    spark.sql("select * from t_person order by age desc").show()
    //todo--------------------SQL操作风格 end-------------


    sc.stop()
    spark.stop()
  }
}
