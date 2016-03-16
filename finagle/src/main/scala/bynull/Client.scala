package bynull

/**
 * Http client
 * Created by null on 9/15/15.
 */
import com.twitter.finagle.httpx.{Response, Request}
import com.twitter.finagle.{Httpx, Service, httpx}
import com.twitter.logging.Logger
import com.twitter.util.{FuturePool, Await, Future}

import scala.collection.mutable.ArrayBuffer

object Client extends App {
  val log = Logger.get(Client.getClass)

  val clients = ArrayBuffer[HttpClient]()
  for (x <- 1 to args(0).toInt) {
    clients += HttpClient()
  }

  val start = System.currentTimeMillis()

  clients
    .map(c => c.sendRequest())
    .foreach(resp => Await.ready(resp))

  log.info("Executed. Time: " + BigDecimal(System.currentTimeMillis() - start) / 1000 + " seconds")
}

case class HttpClient() {
  private val log = Logger.get(Client.getClass)

  private val request = httpx.Request(httpx.Method.Get, "/foo/bar")
  request.host = "localhost"

  private val client = Httpx.newService("localhost:8000")

  def sendRequest() = {
    val response = client.apply(request).onSuccess { resp: Response =>
      log.info("Http client. Got response")
    }
    response
  }
}