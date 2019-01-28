object Main {
  def main(args: Array[String]): Unit = args.toList match {
    case "--download" :: xs => DownloadAllCoursePlans(xs)
    case "--server"   :: xs => CourseWebServer.start(xs); // main(args.tail)
    case xs                 => println(Statistics(xs))
  }
}
