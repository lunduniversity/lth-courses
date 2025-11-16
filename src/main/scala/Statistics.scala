import Data._

object Statistics {
  def apply(args: List[String] = List()): String = {
    println(s"args=$args")
    val argPairs = args.grouped(2).collect{
      case Seq(a,b) => (a,b)
    }.toMap
    println(s"argPairs=$argPairs")

    val psTrimmed = argPairs.getOrElse("--programs", "F E M V A K D").trim //.toUpperCase //need to accept program names such as Pi
    val selectedOrAll = if (psTrimmed.nonEmpty) psTrimmed else Data.allExistingProgs
    val programs = selectedOrAll.split(" ").toList
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
    |alla program: ${Data.allExistingProgs}
    |
    |civ.ing.prog: ${Data.civIng.keys.toSeq.sorted.mkString(" ")}
    |
    |valda program: $selectedOrAll
    |""".stripMargin
    //|        obl. efter filtrering: ${n}st ($percent%)
    // |*** FILTRERAD LISTA OBLIGATORISKA KURSER
    // |    med en eller flera av: ${words.mkString(" ")}
    // |    antal: ${n}st ($percent%)
    // |${filteredCourses.showCourses}

    val oblOfProgramFiltered =
      oblOfProgram.map(kv => kv._1 -> kv._2.toSeq.findCourses(words).sorted)

    type SummaryRow = (String, String, Int, Int)
    var summary: Vector[SummaryRow] = Vector()

    val filter = words.mkString(" ")

    val selectedPrograms = programs.map{ p =>
      if (oblOfProgramFiltered.isDefinedAt(p)) {
        val cs = oblOfProgramFiltered(p).toSeq.sorted
        val name = progName(p)
        val count = cs.size
        val credits = cs.map(_.credits.toDouble).sum.round.toInt
        summary = summary :+ ((p, name, count, credits))

        s"""
        |*** OBLIGATORISKA FÖR PROGRAM $p $name
        |   filter: $filter
        |    antal: $count st
        |    poäng: $credits hp
        |${cs.showCourses}
        |
        """.stripMargin
      } else s"\n??? KONSTIG PROGRAMAKRONYM: $p\n"
    }.mkString
    def pad(s: Any, i: Int): String = s.toString.padTo(i, ' ')
    val namePad = summary.map(_._2.size).max + 2
    def latexRow(row: SummaryRow): String = row match {
      case (id, name, count, hp) => 
        s"${pad(id, 4)} & ${pad(name, namePad)} & ${pad(count, 5)} & ${pad(hp, 4)}"
    }
    val latexRows = summary.sortBy(_._3).reverse.map(latexRow).mkString("\n")
    val latexSummary = s"""
    |% Summering för användning i latex-dokument
    |\\begin{tabular}{l l c c}
    |Id   & ${pad("Program", namePad)} & antal & hp \\\\\\hline 
    |$latexRows
    |\\end{tabular}
    """.stripMargin
    s"$allCourses $selectedPrograms <pre>$latexSummary</pre>"
    //s"$selectedPrograms"
  }
}
