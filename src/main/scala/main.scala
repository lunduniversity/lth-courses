package se.lth.courses

object DownloadAllCoursePlans {
  def now = System.currentTimeMillis
  def main(args: Array[String]): Unit = {
    require(args.nonEmpty, "args empty; missing <year>, use e.g: 18_19")
    val t0 = now
    def elapsed = (now - t0)/1000
    else {
      import se.bjornregnell.tabular.Grid.StringToFile
      val year = args(0)

      println(s"Downloading course plans to data/$year/plans...")
      import Data._
      for (courseId <- courseIds) {
        print(s"$courseId, ")
        downloadPlanText(courseId, year).toFile(s"data/$year/$courseId.txt")
        downloadPlanHtml(courseId, year).toFile(s"data/$year/$courseId.html")
      }
      println(s"READY! Total download time = $elapsed seconds")
    }
  }

}
