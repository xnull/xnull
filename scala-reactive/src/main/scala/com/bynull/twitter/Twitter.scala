package com.bynull.twitter

import com.bynull.common.{ApplicationConfig, ExecutionContextManager}
import org.slf4j.LoggerFactory
import org.springframework.social.twitter.api.impl.TwitterTemplate

import scala.collection.JavaConversions._
import scala.concurrent.Future

/**
  * Twitter management api
  * https://dev.twitter.com/rest/public/search
  */
object Twitter extends Twitter(
  template = new TwitterTemplate(ApplicationConfig.twitterApiKey, ApplicationConfig.twitterApiSecret)
)

class Twitter(template: TwitterTemplate) {
  private val log = LoggerFactory.getLogger(this.getClass)
  implicit private val ec = ExecutionContextManager.Ec

  def search(query: String): Future[List[TweetDto]] = {
    Future {
      log.debug(s"Search: $query")
      val search = template.searchOperations().search(query)
      search.getTweets.toList.map { tweet =>
        TweetDto(tweet.getId, tweet.getText, tweet.getFromUser)
      }
    }
  }
}

case class TweetDto(id: Long, text: String, fromUser: String)