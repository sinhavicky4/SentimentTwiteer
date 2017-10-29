package com

import org.apache.log4j.{Level, Logger}
import org.apache.spark.Logging

object LoggerStreaming extends Logging {

  def setLoggerLevel(): Unit = {

    val v = Logger.getRootLogger.getAllAppenders.hasMoreElements
    if (!v) {
      Logger.getRootLogger.setLevel(Level.WARN)
    }

  }

}
