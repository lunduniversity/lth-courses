import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._

object CourseWebServer extends WebServer {
  def staticsResponse(progs: String, words: String): HttpEntity.Strict = HttpEntity(
    ContentTypes.`text/html(UTF-8)`,
    Html.kursomatenPage(progs, words,
      stats =
        Html.br(Statistics(List("--programs", progs, "--words", words)))
    )
  )

  def helloRoute =
    path("hello") {
      get {
        val he = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          Html.helloPage(" world! Kursomaten is alive and kicking :)")
        )
        complete(he)
      }
    } ~ path("kursomaten") {
      get {
        parameters("progs", "words") { (p, w) =>
          println(s"progs=$p")
          println(s"words=$w")
          val he = HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            Html.kursomatenPage(
              progs = p,
              words = w,
              stats =
                Html.br(Statistics(List("--programs", p, "--words", w)))
            )
          )
          complete(staticsResponse(p,w))
        }
      } ~ get {
          val (p, w) = ("F E M V A K D", Data.digiwords)
          println(s"Using defaults: progs=$p words=$w")
          val he = HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            Html.kursomatenPage(
              progs = p,
              words = w,
              stats =
                Html.br(Statistics(List("--programs", p, "--words", w)))
            )
          )
          //println(he)
          complete(staticsResponse(p,w))
      }
    }


  override def routes = helloRoute
}
