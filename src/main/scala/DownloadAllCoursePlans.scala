import Data._
import Grid.StringToFile

object DownloadAllCoursePlans {

  def now = System.currentTimeMillis

  def apply(args: List[String]): Unit = {
    val year = args.lift(0).getOrElse(defaultYear)
    println(s"Downloading course plans to data/$year/plans/")

    val t0 = now
    def elapsed = (now - t0)/1000


    for (courseId <- courseIds) {
      print(s"$courseId, ")
      val planHtml = downloadPlanHtml(courseId, year)
      planHtml.toFile(s"data/$year/$courseId.html")
      planToTxt(planHtml).toFile(s"data/$year/$courseId.txt")
    }
    println(s"READY! Total download time = $elapsed seconds")
  }

}
