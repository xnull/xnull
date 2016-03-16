package com.bynull.common

import dispatch.{Http, as, url}

/**
  * Simple http client based on dispatch library
  */
class HttpClient {
  implicit private val ec = ExecutionContextManager.Ec
  def sendRequest(queryUrl: String): scala.concurrent.Future[String] = Http(url(queryUrl) OK as.String)
}
