package com.bynull

import com.bynull.common.ExecutionContextManager
import com.bynull.github.{Github, GithubQuery, GithubRepositoryDto}
import com.bynull.twitter.{TweetDto, Twitter}
import org.slf4j.LoggerFactory

import scala.concurrent.Future

/**
  * Reactive service - collect and union the data from github and twitter
  */
object ReactiveService extends ReactiveService(Github, Twitter)

class ReactiveService(github: Github, twitter: Twitter) {
  private val log = LoggerFactory.getLogger(this.getClass)
  implicit private val ec = ExecutionContextManager.Ec

  def search(searchQuery: String): Future[List[ProjectInfo]] = {
    log.debug(s"Search github project tweets, query: $searchQuery")

    val query = GithubQuery(searchQuery)
    github.search(query) flatMap { gitHubResult =>
      val tweets = gitHubResult.items.map { repository =>
        twitter.search(repository.htmlUrl) map { repositoryTweets =>
          ProjectInfo(repository, repositoryTweets)
        }
      }
      Future.sequence(tweets)
    }
  }
}

case class ProjectInfo(githubProject: GithubRepositoryDto, tweets: List[TweetDto])
