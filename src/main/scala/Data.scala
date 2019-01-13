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
      val plan = loadPlan(id).mkString("\n")
      line <- Key.keyMatcher(key).findFirstIn(plan)
      val value = line.stripPrefix(Key.Obl).trim.split(',').toVector.map(_.trim)
    } yield (id, value)
    pairs.toList
  }

  lazy val plan: Map[String, String] = courseIds.map(id => (id, loadPlan(id).mkString("\n"))).toMap

  lazy val planLowerCase: Map[String, String] = plan.mapValues(_.toLowerCase)

  lazy val obl = loadListByKey(Key.Obl)
  lazy val oblIds = obl.map(_._1)

  lazy val elect = loadListByKey(Key.Elect)
  lazy val electIds = elect.map(_._1)

  lazy val altObl = loadListByKey(Key.AltObl)
  lazy val altOblIds = altObl.map(_._1)

  val digiwords = Vector("programmering", "digital", "programvara")

  def oblDigi(words: Vector[String] = digiwords) = oblIds.filter(
    id => words.exists(w => planLowerCase(id).contains(w))
  )

}
