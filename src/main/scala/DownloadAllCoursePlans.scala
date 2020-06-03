import Grid.StringToFile

object DownloadAllCoursePlans {

  def now = System.currentTimeMillis

  def apply(args: List[String]): Unit = {
    val year = args.lift(0).getOrElse(Data.defaultYear)
    val overview: Grid = Grid.fromFile(s"data/$year/overview.tsv")
    val courseIds: Vector[String] = overview("kurskod")
    val planDir = s"data/$year/plans"
    println(s"Downloading course plans to $planDir/")
    new java.io.File(planDir).mkdirs()

    val t0 = now
    def elapsed = (now - t0)/1000

    var notFound = Vector.empty[String] 

    for (courseId <- courseIds) scala.util.Try {
      print(s"$courseId, ")
      val planHtml = Data.downloadPlanHtml(courseId, year)
      planHtml.toFile(s"$planDir/$courseId.html")
      Data.planToTxt(planHtml).toFile(s"$planDir/$courseId.txt")
    }.recover { case e => 
      println(s"\n*** ERROR: $e")
      notFound :+= courseId
    }
    println(s"\n\n*** READY! Total download time = $elapsed seconds") 
    if (notFound.nonEmpty) {
      println(s"""\n*** Not found: \n ${notFound.mkString(", ")}""")
    }
  }

}
