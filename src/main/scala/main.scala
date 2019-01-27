import Data._
import Grid.StringToFile

object Main {
  def main(args: Array[String]): Unit = args.toList match {
    case "--download":: xs => DownloadAllCoursePlans(xs)
    case "--server"::xs    => CourseWebServer.start(); main(args.tail)
    case xs                => println(Statistics(xs))
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

object Statistics {
  def apply(args: List[String] = List()): String = {
    println(s"args=$args")
    val argPairs = args.grouped(2).map{
      case Seq(a,b) => (a,b)
    }.toMap
    println(s"argPairs=$argPairs")
    val programs = argPairs.getOrElse("--programs", "K D").split(" ").toList
    println(s"analyzing programs: $programs")
    val words = argPairs.getOrElse("--words", digiwords).split(" ").toVector
    val nOblForProg = oblForProg.filter{ case (_,ps) => ps.nonEmpty }.keys.size
    val filteredCourses = oblForProg.keys.toSeq.findCourses(words).sorted
    val n = filteredCourses.length
    val percent = n * 100 / nOblForProg
    val allCourses = s"""
    |  *** ALLA KURSER VID LTH 20$defaultYear ***
    |
    |             tot antal kurser: ${overview.nRows}
    |         obligatoriska kurser: ${obl.size}
    |  programobligatoriska kurser: ${nOblForProg}
    |                valfriakurser: ${elect.size}
    |
    |*** FILTRERAD LISTA OBLIGATORISKA KURSER
    |    med en eller flera av: ${words.mkString(" ")}
    |    antal: ${n}st ($percent%)
    |${filteredCourses.showCourses}
    |""".stripMargin

    val oblOfProgramFiltered =
      oblOfProgram.mapValues(oblSet => oblSet.toSeq.findCourses(words).sorted)

    val selectedPrograms = programs.map{ p =>
      val cs = oblOfProgramFiltered(p).toSeq.sorted
      s"""
      |*** OBLIGATORISKA FÖR PROGRAM $p
      |    med en eller flera av: ${words.mkString(" ")}
      |    antal: ${cs.size}st
      |    poäng: ${cs.map(_.credits.toDouble).sum.round}hp
      |${cs.showCourses}
      """.stripMargin
    }.mkString("\n")

    s"$allCourses $selectedPrograms"
  }
}
