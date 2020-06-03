import scala.io.Source
import scala.io.Codec
import Grid.StringToFile

object UpdateProgramNames {
  val outFile = "data/program-names.tsv"
  def file(f: String): java.io.File = new java.io.File(f)
  def isExisting(f: String) = file(f).exists
  val backupFile = {
    var fn = outFile
    while (isExisting(fn)) fn = s"$fn.bak"
    fn
  }
  val oldOpt: Option[Grid] = 
    if (isExisting(outFile)) Some(Grid.fromFile(outFile)) else None
  
  val url = "https://kurser.lth.se/lot/?val=program"
  val lines: Vector[String] = Source.fromURL(url)(Codec.UTF8).getLines.toVector
  val start = """<label><input type="radio" name="prog" value=""""
  val progLines = lines.filter(_.contains(start))

  def extract(line: String): (String, String) = {
    val start = line.indexOf("<strong>")
    val trimmed = line
      .substring(start + "<strong>".length)
      .replace(":</strong>", "")
      .takeWhile(_ != '<')
      .trim
    val progId = trimmed.takeWhile(_ != ' ')
    val name = trimmed.dropWhile(_ != ' ').trim
    (progId, name)
  }

  val progNames: Vector[(String,  String)] = progLines.map(extract).sortBy(_._1)

  def apply(args: List[String]): Unit = {
    oldOpt.foreach { old => 
      println(s"*** CURRENT DATA IN $outFile"); 
      old.print
    }
    println(s"Downloaded: ${progNames.size} program names from $url")
    val latest = progNames.map(_._1).toSet
    val old = Data.progName.keySet
    val extra = latest diff old
    val deleted = old diff latest
    println(s"Extra: $extra")
    println(s"Deleted: $deleted  Note: Deleted prog ids == BIG problem...")
    if (Data.progName.toSet != progNames.toSet) {
      println(s"Renaming $outFile to $backupFile")
      file(outFile).renameTo(file(backupFile))
      println(s"Saving updated $outFile")
      val withHeading = ("program" -> "namn") +: progNames
      val outString = withHeading.map{ case (k, v) => s"$k\t$v" }.mkString("\n")
      outString.toFile(outFile)
    } else println("-- NO UPDATE NEEDED - program names are the same!")
    
  }
}
