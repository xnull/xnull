package com.bynull.common

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

/**
  * Manages all execution contexts of the application
  * Created by null on 10/31/15.
  */
object ExecutionContextManager {
  val Ec = ExecutionContext.fromExecutor(
    Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors)
  )
}
