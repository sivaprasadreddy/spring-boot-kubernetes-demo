package bookmarks

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class BookmarksSimulation extends Simulation {

  val httpConf = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // Now, we can write the scenario as a composition
  val scnBrowseBookmarks = scenario("Browse Bookmarks")
    .during(3.minutes, "Counter") {
      exec(Browse.bookmarks).pause(2)
    }

    val scnCreateBookmark = scenario("Create Bookmark")
        .during(3.minutes, "Counter") {
            exec(Bookmarks.create).pause(2)
        }

  setUp(
      scnBrowseBookmarks.inject(rampUsers(50) during (5.seconds))
      ,scnCreateBookmark.inject(rampUsers(50) during (5.seconds))
  ).protocols(httpConf)
    .assertions(
      global.responseTime.max.lt(800),
      global.successfulRequests.percent.gt(95)
    )
}

object Browse {

  val gotoPage = repeat(5, "n") {
    exec{ session => session.set("pageNo", session("n").as[Int] + 1) }
    .exec(http("Bookmarks Page ${pageNo}").get("/api/v1/bookmarks/?page=${pageNo}"))
    .pause(1)
  }

  val bookmarks = exec(gotoPage)

}

object Bookmarks {
    val bookmarksFeeder = csv("data/feeders/bookmarks.csv").random
    var randomBookmarkId = Iterator.continually(Map("randomBookmarkId" -> ( Random.nextInt(1) + 1 )))

    val createBookmark = feed(bookmarksFeeder)
        .exec(
            http("Create Bookmark").post("/api/v1/bookmarks")
                .body(ElFileBody("data/feeders/new_bookmark.json")).asJson
        )
        .pause(1)

    val upvoteBookmark = feed(bookmarksFeeder)
        .feed(randomBookmarkId)
        .exec(
            http("Upvote Bookmark").put("/api/v1/bookmarks/${randomBookmarkId}/votes/up")
        )
        .pause(1)

    val downvoteBookmark = feed(bookmarksFeeder)
        .feed(randomBookmarkId)
        .exec(
            http("Downvote Bookmark").put("/api/v1/bookmarks/${randomBookmarkId}/votes/down")
        )
        .pause(1)

    val create = exec(createBookmark).exec(upvoteBookmark).exec(downvoteBookmark)
}
