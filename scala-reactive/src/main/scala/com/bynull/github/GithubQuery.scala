package com.bynull.github

import com.bynull.github.GithubQueryOrder._
import com.bynull.github.GithubQuerySort._

/**
  * Github query api
  * Created by null on 10/31/15.
  */
case class GithubQuery
(
  criteria: String,
  paging: GithubQueryPaging = GithubQueryPaging(0, 10),
  sort: GithubQuerySort = Stars,
  order: GithubQueryOrder = Desc
)

case class GithubQueryPaging(page: Int, perPage: Int)

sealed case class GithubQuerySort(sortType: String)

object GithubQuerySort {

  object Stars extends GithubQuerySort("stars")

  object Forks extends GithubQuerySort("forks")

  object Updated extends GithubQuerySort("updated")

}

sealed case class GithubQueryOrder(order: String)

object GithubQueryOrder {

  object Asc extends GithubQueryOrder("asc")

  object Desc extends GithubQueryOrder("desc")
}

