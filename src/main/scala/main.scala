object Main {
  def main(args: Array[String]): Unit = args.toList match {
    case "--download" :: xs => DownloadAllCoursePlans(xs)

    case "--server"   :: xs =>
      CourseWebServer.start(
        host = xs.lift(0).getOrElse("0.0.0.0"),
        port = scala.util.Try(xs(1).toInt).getOrElse(8090)
      )

    case xs  => println(Statistics(xs))
  }
}
