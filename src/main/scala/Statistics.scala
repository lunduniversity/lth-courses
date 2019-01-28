import Data._

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
    |        obl. efter filtrering: ${n}st ($percent%)
    |
    |""".stripMargin
    // |*** FILTRERAD LISTA OBLIGATORISKA KURSER
    // |    med en eller flera av: ${words.mkString(" ")}
    // |    antal: ${n}st ($percent%)
    // |${filteredCourses.showCourses}

    val oblOfProgramFiltered =
      oblOfProgram.mapValues(oblSet => oblSet.toSeq.findCourses(words).sorted)

    val selectedPrograms = programs.map{ p =>
      val cs = oblOfProgramFiltered(p).toSeq.sorted
      s"""
      |*** OBLIGATORISKA FÖR PROGRAM $p
      |   filter: ${words.mkString(" ")}
      |    antal: ${cs.size}st
      |    poäng: ${cs.map(_.credits.toDouble).sum.round}hp
      |${cs.showCourses}
      |
      """.stripMargin
    }.mkString//("\n")

    s"$allCourses $selectedPrograms"
    //s"$selectedPrograms"
  }
}
