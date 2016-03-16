package com.bynull

import com.bynull.common.{ExecutionContextManager, JsonUtil}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

/**
  * Reactive application
  * Created by null on 10/31/15.
  */
object ReactiveApp extends App {
  private val log = LoggerFactory.getLogger(this.getClass)
  implicit private val ec = ExecutionContextManager.Ec

  log.info("Start an application")

  try {
    ReactiveService.search("Reactive") onComplete {
      case Failure(exception) =>
        println(exception)
      case Success(projects) =>
        println(JsonUtil.toJson(projects))
        println("Completed. Please stop program (press ctrl + c)")
    }
  } catch {
    case e: Throwable =>
      log.error("Can't get the data", e)
  }
}