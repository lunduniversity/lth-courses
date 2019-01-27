// https://doc.akka.io/docs/akka-http/current/introduction.html#routing-dsl-for-http-servers

    //
    // import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
    // import akka.http.scaladsl.server.HttpApp
    // import akka.http.scaladsl.server.Route
    //
    // // Server definition
    // object WebServer extends HttpApp {
    //   override def routes: Route =
    //     path("hello") {
    //       get {
    //         complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
    //       }
    //     }
    // }
    //
    // // Starting the server
    // WebServer.startServer("localhost", 8080)


trait WebServer {
  import akka.actor.ActorSystem
  import akka.http.scaladsl.Http
  import akka.http.scaladsl.model._
  import akka.http.scaladsl.server.Route
  import akka.http.scaladsl.server.Directives._
  import akka.stream.ActorMaterializer
  import scala.io.StdIn

  def helloRoute(helloPath: String = "hello", msg: String = "<h1>hello</h1") =
    path(helloPath) {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"$msg"))
      }
    }

  def routes: Route = helloRoute()  // override this member with your routes

  def start(port: Int= 8090, host: String = "localhost"): Unit = {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route = routes

    val bindingFuture = Http().bindAndHandle(route, host, port)

    println(s"Server online at http://$host:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
