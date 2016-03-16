package com.bynull.twitter

import com.bynull.common.ExecutionContextManager
import org.mockito.Matchers._
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.social.twitter.api.{SearchResults, Tweet}

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by null on 10/31/15.
  */
class TwitterTest extends FlatSpec with MockitoSugar with Matchers {
  implicit val ec = ExecutionContextManager.Ec

  it should "send request to twitter and get an answer" in {
    val templateMock = Mockito.mock(classOf[TwitterTemplate], Mockito.RETURNS_DEEP_STUBS)
    val searchResultMock = mock[SearchResults]
    val testTweets = List[Tweet](
      new Tweet(123, "test", "", null, "", "", 0L, 0L, "", "")
    ).asJava
    when(searchResultMock.getTweets).thenReturn(testTweets)
    when(templateMock.searchOperations().search(anyString())).thenReturn(searchResultMock)
    val twitter = new Twitter(templateMock)
    val search = twitter.search("test")
    val result = Await.result(search, 10.seconds)

    result.size shouldBe 1
    result.head.id shouldBe 123
    verify(searchResultMock).getTweets
  }
}
