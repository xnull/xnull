package bynull

import java.util.concurrent.{TimeUnit, Executors}

import com.twitter.finagle._
import com.twitter.finagle.httpx.Request
import com.twitter.finagle.httpx.service.NotFoundService
import com.twitter.logging.Logger
import com.twitter.util._

/**
 * Http server
 * Created by null on 9/15/15.
 */
object Server extends App {
  val server = Httpx.serve(":8000", Server(args(0).toInt))
  Await.ready(server)
}

case class Server(poolSize: Int) extends Service[httpx.Request, httpx.Response] {
  private val pool = FuturePool.apply(Executors.newFixedThreadPool(poolSize))
  val log = Logger.apply(Server.getClass)

  override def apply(request: Request): Future[httpx.Response] = request.path match {
    case "/foo/bar" =>
      pool.apply {
        TimeUnit.SECONDS.sleep(1)
        httpx.Response(request.version, httpx.Status.Ok)
      }
    case _ =>
      NotFoundService(request)
  }
}