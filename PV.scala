package com.hmtest.IpLocation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * scala 算子模型的实例
  * 获取日志文件中的ip地址进行PV的统计
  * @
  *
  */
object PV {
  def main(args: Array[String]): Unit = {
    val sparkConf : SparkConf=new SparkConf().setAppName("Iplocation").setMaster("local[2]")

    val sc: SparkContext = new SparkContext(sparkConf)

    val data : RDD [String]=sc.textFile("E:\\sdafa\\020090121000132.394251.http.format")
    //切分后的数组第一个字段为ip
    val ips :RDD [String] =data.map(_.split("\\|")(1))
    //根据IP地址去重
    val disIps :RDD[String]=ips.distinct()
    //数组的foreach 输出
    disIps.foreach(println)
    println(disIps.count())

    sc.stop()
  }
}
