package com.bynull

import com.bynull.common.{ExecutionContextManager, JsonUtil}
import com.bynull.github.{Github, GithubDto, GithubQuery}
import com.bynull.twitter.{TweetDto, Twitter}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.Source

/**
  * Created by null on 10/31/15.
  */
class ReactiveServiceTest extends FlatSpec with MockitoSugar with Matchers {
  implicit val ec = ExecutionContextManager.Ec

  it should "make requests and collect the data" in {
    val githubMock = mock[Github]
    when(githubMock.search(anyObject())).thenReturn {
      Future {
        JsonUtil.fromJsonWithUnderscore(
          Source.fromFile(ClassLoader.getSystemResource("github.json").toURI).mkString,
          classOf[GithubDto]
        )
      }
    }

    val twitterMock = mock[Twitter]
    when(twitterMock.search(anyString)).thenReturn {
      Future {
        List(
          TweetDto(123, "test message", "test_user"),
          TweetDto(321, "test message_2", "test_user_2")
        )
      }
    }

    val service = new ReactiveService(githubMock, twitterMock)
    val search = service.search("test")

    val result = Await.result(search, 10.seconds)

    result.size shouldBe 1
    result.head.tweets.size shouldBe 2
    verify(githubMock).search(any(classOf[GithubQuery]))
    verify(twitterMock).search(anyString)
  }
}
