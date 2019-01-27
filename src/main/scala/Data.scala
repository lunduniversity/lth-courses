import scala.io.Source
import scala.io.Codec
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import scala.util.matching.Regex

object Data {
  val defaultYear = "18_19"
  lazy val overview: Grid = Grid.fromFile(s"data/$defaultYear/overview.csv")
  lazy val courseIds: Vector[String] = overview("kurskod")

  val baseUrl = "https://kurser.lth.se/kursplaner"
  val baseDir = "data"

  object Key {
    def keyMatcher(key: String): scala.util.matching.Regex = s"$key[^\\n]*".r

    val Obl    = "Obligatorisk för:"
    val Elect  = "Valfri för:"
    val AltObl = "Alternativobligatorisk för:"
    val keys = Vector(Obl, Elect, AltObl)
  }

  def following(lines: Seq[String], from: String, key: String): (String, String) =
    key -> lines.dropWhile(! _.startsWith(from)).drop(1).headOption.getOrElse("")

  def numberBefore(text: String, before: String): Option[Double] =
    scala.util.Try{
      val i = text.indexOf(before)
      val maybeNumber = text.splitAt(i)._1
        .reverse
        .dropWhile(_.isSpaceChar)
        .takeWhile(!_.isSpaceChar)
        .reverse
      maybeNumber.trim.replaceAllLiterally(",",".").toDouble
    }.toOption

  def section(lines: Seq[String], from: String, to: String, key: String): (String, String) =
    key ->
      lines
        .dropWhile(! _.startsWith(from))
        .takeWhile(!_.startsWith(to))
        .drop(1)
        .mkString(" ")

  def parsePlan(plan: String): Map[String, String] = {
    val lines = plan.split("\n")
    Seq(
      "hp" -> numberBefore(plan, "högskolepoäng").getOrElse("???").toString,
      following(lines,"Kursplan","namn"),
      section(lines, "Syfte", "Kursens examination", "beskrivning")
    ).toMap
  }

  def downloadPlanHtml(courseId: String, year: String = defaultYear): String = {
    val id = courseId.toUpperCase
    val buf = Source.fromURL(s"$baseUrl/$year/$id.html")(Codec.UTF8)
    val result = buf.getLines.mkString("\n")
    buf.close
    result
  }

  def planToTxt(htmlSoup: String): String = Jsoup.parse(htmlSoup).wholeText

  def loadPlan(coursId: String, year: String = defaultYear, suff: String = "txt"): Vector[String] = {
    val buf = Source.fromFile(s"$baseDir/$year/plans/$coursId.txt")(Codec.UTF8)
    val lines = buf.getLines.toVector
    buf.close
    lines
  }

  def loadListByKey(key: String, year: String = defaultYear): List[(String, Vector[String])] = {
    val pairs = for {
      id <- courseIds
      plan = loadPlan(id).mkString("\n")
      line <- Key.keyMatcher(key).findFirstIn(plan)
      value = line.stripPrefix(key).trim.split(',').toVector.map(_.trim)
    } yield (id, value)
    pairs.toList
  }

  lazy val plan: Map[String, String] =
    courseIds.map(id => (id, loadPlan(id).mkString("\n"))).toMap

  lazy val propertyOf: Map[String, Map[String, String]] = // propertyOf("EDAA45")("hp").toDouble
    courseIds.map(id => (id, parsePlan(plan(id)))).toMap

  lazy val planLowerCase: Map[String, String] = plan.mapValues(_.toLowerCase)

  lazy val obl: List[(String, Vector[String])]  = loadListByKey(Key.Obl)

  lazy val oblFor: Map[String, Vector[String]]  = obl.toMap

  lazy val oblForProg: Map[String, Vector[String]] =
    oblFor.mapValues(ps => ps.filterNot(_.contains("-")).map(_.takeWhile(_.isLetter)))

  lazy val oblIds = obl.map(_._1)

  lazy val programs = oblFor.values.fold(Vector())(_ ++ _).toSet

  lazy val elect = loadListByKey(Key.Elect)
  lazy val electIds = elect.map(_._1)

  lazy val altObl = loadListByKey(Key.AltObl)
  lazy val altOblIds = altObl.map(_._1)

  val digiwords = "programmer digital programvar algoritm".split(" ").toVector

  implicit class IdSeqOps(ids: Seq[String]){
    def findCourses(words: Vector[String]): Seq[String] =
      ids.filter(id => words.exists(w => planLowerCase(id).contains(w)))

    def filterOblFor(progInit: String): Seq[String] =
      ids.filter(id => oblFor(id).exists(_.startsWith(progInit)))
  }
}
