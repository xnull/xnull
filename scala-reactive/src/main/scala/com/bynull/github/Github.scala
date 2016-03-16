package com.bynull.github

import com.bynull.common._
import org.slf4j.LoggerFactory

import scala.concurrent.Future

/**
  * Github management tools
  */
object Github extends Github(new HttpClient()) {
  private val SearchApiUrl = "https://api.github.com/search/repositories"
}

class Github(httpClient: HttpClient) {
  private val log = LoggerFactory.getLogger(this.getClass)
  implicit private val ec = ExecutionContextManager.Ec

  def search(query: GithubQuery): Future[GithubDto] = {
    log.debug(s"Github search: $query")

    httpClient.sendRequest(buildUrl(query)) map { response =>
      JsonUtil.fromJsonWithUnderscore(response, classOf[GithubDto])
    }
  }

  private def buildUrl(query: GithubQuery): String = {
    s"""${Github.SearchApiUrl}?
       |q=${query.criteria}
       |&page=${query.paging.page}&per_page=${query.paging.perPage}
       |&sort=${query.sort.sortType}
       |&order=${query.order.order}
    """.stripMargin.replaceAll("\n", "").trim
  }
}

case class GithubDto(items: List[GithubRepositoryDto])

case class GithubRepositoryDto
(
  id: Int,
  name: String,
  htmlUrl: String,
  description: String,
  homepage: String,
  language: String
)