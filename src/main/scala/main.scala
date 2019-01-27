import Data._
import Grid.StringToFile

object Main {
  def main(args: Array[String]): Unit = args.toList match {
    case "--download":: xs => DownloadAllCoursePlans(xs)
    case "--words"::xs     => PrintStatistics(xs)
    case _                 => PrintStatistics()
  }
}

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

object PrintStatistics {
  def apply(args: List[String] = List()): Unit = {
    println(s"args=$args")
    val words = if (args.isEmpty) digiwords else args.toVector
    val nOblForProg = oblForProg.filter{ case (_,ps) => ps.nonEmpty }.keys.size
    val digiCourses = oblForProg.keys.toSeq.findCourses(words).sorted
    val n = digiCourses.length
    val percent = n * 100 / nOblForProg
    val stats = s"""
    |  *** COURSES AT LTH 20$defaultYear ***
    |
    |                      courses: ${overview.nRows}
    |           obligatory courses: ${obl.size}
    |          alt.-oblig. courses: ${altObl.size}
    |   program obligatory courses: ${nOblForProg}
    |             elective courses: ${elect.size}
    |
    |  filtered program obligatory courses: $n ($percent%)
    |  containing at least one of these strings: ${words.mkString(" ")}
    |
    |${digiCourses.mkString(",")}
    |""".stripMargin
    println(stats)
  }
}
