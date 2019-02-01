import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._

object CourseWebServer extends WebServer {
  def helloResponse: HttpEntity.Strict =
    HttpEntity(ContentTypes.`text/html(UTF-8)`,
      Html.helloPage(" world! Kursomaten is alive and kicking :)"))

  def statsResponse(progs: String, words: String): HttpEntity.Strict = {
    var soup = Statistics(List("--programs", progs, "--words", words))
    Data.courseIds.foreach{ id =>
      soup = Html.replaceAllCourseIdsWithLinks(soup, id)
    }
    HttpEntity(ContentTypes.`text/html(UTF-8)`,
      Html.kursomatenPage(progs, words, stats = Html.br(soup)))
  }

  override def routes =
    path("hello") { get { complete(helloResponse) } } ~
    path("kursomaten") {
      get { parameters("progs", "words") { (p, w) =>
        complete(statsResponse(p,w))
      } } ~
      get { // no params given; default search
        complete(statsResponse("F E M V A K D", Data.digiwords))
      }
    }
}
