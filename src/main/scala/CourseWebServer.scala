import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._

object CourseWebServer extends WebServer {
  def helloRoute =
    path("hello") {
      get {
        val he = HttpEntity(ContentTypes.`text/html(UTF-8)`, Html.helloPage("hi"))
        println(he)
        complete(he)
      }
    }

  override def routes = helloRoute
}
