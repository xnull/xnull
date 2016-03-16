package com.bynull.github

import com.bynull.common.{ExecutionContextManager, HttpClient}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import org.mockito.Matchers._

import scala.io.Source

/**
  * Created by null on 10/31/15.
  */
class GithubTest extends FlatSpec with MockitoSugar with Matchers {
  implicit val ec = ExecutionContextManager.Ec

  it should "send request and get an answer" in {
    val httpClientMock = mock[HttpClient]
    when(httpClientMock.sendRequest(anyObject())).thenReturn(Future {
      Source.fromFile(ClassLoader.getSystemResource("github.json").toURI).mkString
    })

    val query = GithubQuery("test")
    val gitHub = new Github(httpClientMock)
    val search = gitHub.search(query)
    val result = Await.result(search, 10.seconds)

    verify(httpClientMock)
      .sendRequest("https://api.github.com/search/repositories?q=test&page=0&per_page=10&sort=stars&order=desc")
    result.items.size shouldBe 1
    result.items.head.id shouldBe 6430758
  }
}
