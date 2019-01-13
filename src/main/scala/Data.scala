package se.lth.courses

import se.bjornregnell.tabular.Grid
import scala.io.Source
import scala.io.Codec
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

object Data {
  lazy val overview2018_19: Grid = Grid.fromFile("data/18_19/overview.csv")
  lazy val courseIds: Vector[String] = overview2018_19("kurskod")

  val baseUrl = "https://kurser.lth.se/kursplaner"

  object Key {
    def apply(key: String): scala.util.matching.Regex = s"$key[^\\n]*".r

    val Obl           = "Obligatorisk för:"
    val OblPattern    = apply(Obl)

    val Elect         = "Valfri för:"
    val ElectPattern  = apply(Elect)

    val AltObl        = "Alternativobligatorisk för:"
    val AltOblPattern = apply(AltObl)
  }

  def downloadPlanHtml(courseId: String, year: String = "18_19"): String = {
    val id = courseId.toUpperCase
    val buf = Source.fromURL(s"$baseUrl/$year/$id.html")(Codec.UTF8)
    val result = buf.getLines.mkString("\n")
    buf.close
    result
  }

  def downloadPlanText(courseId: String, year: String = "18_19"): String = {
    val soup = downloadPlanHtml(courseId, year)
    val doc = Jsoup.parse(soup)
    doc.body.wholeText
  }

}
