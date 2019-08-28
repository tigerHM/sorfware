package com.xxxx.test

import java.io.InputStream
import java.time.ZoneId
import java.util.Properties
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.fs.StringWriter
import org.apache.flink.streaming.connectors.fs.bucketing.{BucketingSink, DateTimeBucketer}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer


class flinktest {
  def main(args: Array[String]): Unit = {
    //获取运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //设置检查点
    env.enableCheckpointing(5000L)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    //配置文件中获取属性
    val stream: InputStream = processTest.getClass.getClassLoader.getResourceAsStream("test.properties")
    val Param = ParameterTool.fromPropertiesFile(stream)

    val properties: Properties = new Properties
    properties.setProperty("bootstrap.servers",Param.get("KAFKA_BROKER"))
    properties.setProperty("gourp.id",Param.get("gourp-id"))

    //将数据源添加到环境中
    val value: DataStream[String] = env.addSource(new FlinkKafkaConsumer[String](Param.get("topicname"), new SimpleStringSchema(), properties))

    //配置sink :hdfs
    val hdfssink: BucketingSink[String] = new BucketingSink[String]("/test-dw/***")

    hdfssink.setBucketer(new DateTimeBucketer("yyyy-MM-dd",ZoneId.of("Asia/Shanghai")))
    hdfssink.setWriter(new StringWriter())
    hdfssink.setBatchSize(1024 * 1024 * 128L)
    hdfssink.setBatchRolloverInterval(60 * 60 * 1000)
    //将输出添加到环境
    value.addSink(hdfssink)
    //启动任务
    env.execute("Get data form kafka write to hdfs.")
  }
}
