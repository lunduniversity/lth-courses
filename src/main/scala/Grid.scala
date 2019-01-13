/** A companion to Grid with types and settings for tabular data. */
object Grid {
  object Settings {
    var defaultDelim = '\t'
    var defaultEnc = "UTF-8"
    var defaultHeadingShowSep = '-'
    var defaultColShowSep = '|'
  }

  import Settings._

  type Row = Vector[String]
  type Col = Vector[String]
  type Matrix = Vector[Row]
  type RowMap = Map[String, String]

  implicit class StringToFile(underlying: String) {
    def toFile(file: String, enc: String = defaultEnc): Unit = {
      val _ = java.nio.file.Files.write(java.nio.file.Paths.get(file), underlying.getBytes(enc))
    }
  }

  object Text {
    def fromFile(file: String, enc: String = defaultEnc): Vector[String] =
        scala.io.Source.fromFile(file, enc).getLines.toVector
  }

  def fromFile(file: String, delim: Char = defaultDelim, enc: String = defaultEnc): Grid = {
    val lines = io.Source.fromFile(file, enc).getLines.toVector
    fromLines(lines, delim)
  }

  def fromLines(lines: Vector[String], delim: Char = defaultDelim): Grid = {
    val headings = lines(0).split(delim).toVector.map(_.trim.toLowerCase)
    val data = lines.drop(1).map(_.split(delim).toVector
      .take(headings.length).padTo(headings.length,""))
    Grid(headings, data)
  }
}

/** An immutable data structure for manipulating tabular data. */
case class Grid(headings: Grid.Row, data: Grid.Matrix) {
  import Grid._
  import Settings._


  assert(headings == headings.distinct, s"headings contains duplicates: $headings")
  assert(data.map(_.size).forall(_ == headings.size), s"all rows must match headings size")

  lazy val dim = (data.size, headings.size)
  lazy val (nRows, nCols) = dim

  lazy val indexOf: Map[String, Int] = headings.zipWithIndex.toMap
  def rowMap(row: Int): RowMap = headings.map(h => (h, apply(row)(h))).toMap
  lazy val rowMaps: Vector[RowMap] = data.indices.toVector.map(rowMap)

  def apply(row: Int)(colName: String): String = data(row).lift(indexOf(colName)).getOrElse("")
  def apply(colName: String): Col = data.map(row => row.lift(indexOf(colName)).getOrElse(""))
  def apply(row: Int, col: Int): String = data(row).lift(col).getOrElse("")

  def updated(row: Int, colName: String, newValue: String): Grid =
    copy(data = data.updated(row, data(row).updated(indexOf(colName), newValue)))

  def isDistinct(colNames: String*): Boolean =
    colNames.forall{ c => val values = apply(c); values.distinct == values }

  def toMap(keyColName: String)(valueColNames: String*): Map[String, RowMap] =
    data.indices.map { r =>
      val key = apply(r)(keyColName)
      val rowMap = {
        val values = valueColNames.map(c => apply(r)(c))
        valueColNames.zip(values).toMap
      }
      key -> rowMap
    }.toMap

  def filter(colName: String)(p: String => Boolean): Grid =
    copy(data = data.filter(row => p(row(indexOf(colName)))))

  def filterRow(p: RowMap => Boolean): Grid =
      copy(data = (for (i <- data.indices if p(rowMap(i))) yield data(i)).toVector)

  def sorted(colName: String = headings(0)): Grid =
    copy(data = data.sortBy(row => row.lift(indexOf(colName)).getOrElse("")))

  def sortBy[T: Ordering](f: Row => T): Grid = copy(data = data.sortBy(f))

  def mapCol(colName: String)(f: RowMap => String): Grid =
    Grid(headings, data.indices.map(r => data(r).updated(indexOf(colName), f(rowMap(r)))).toVector)

  def addCol(colName: String)(f: RowMap => String): Grid =
    Grid(headings :+ colName, data.indices.map(i => data(i) :+ f(rowMap(i))).toVector)

  def addCol(colName: String, col: Col): Grid =
    Grid(headings :+ colName, data.indices.map(i => data(i) :+ col(i)).toVector)

  def keep(colNames: String*): Grid = {
    val heads = colNames.toVector
    Grid(heads, data.indices.map(i => heads.map(c => apply(i)(c))).toVector)
  }

  def skip(colNames: String*): Grid = {
    val heads = headings diff colNames
    Grid(heads, data.indices.map(i => heads.map(c => apply(i)(c))).toVector)
  }

  def join(colNameInThis: String, colNameInThat: String)(that: Grid): Grid = {
    val extraHeadings = (that.headings diff Seq(colNameInThat)) diff headings
    val thatMap = that.toMap(colNameInThat)(extraHeadings:_*)
    var result = this
    extraHeadings.foreach { colName =>
      result = result.addCol(colName) { rm =>
        val key = rm(colNameInThis)
        thatMap(key)(colName)
      }
    }
    result
  }

  def appendIntersecting(that: Grid): Grid = {
    val common = headings intersect that.headings
    Grid(headings = common, data = keep(common:_*).data ++ that.keep(common:_*).data)
  }

  def find(p: RowMap => Boolean): Option[RowMap] = data.indices.map(rowMap).find(p)

  def rename(colNamePairs: (String,String)*): Grid = {
    val renameMap = colNamePairs.toMap
    copy(headings = headings.map(h => renameMap.getOrElse(h, h)))
  }

  def values: Map[String, Col] = headings.map(h => (h, apply(h).distinct)).toMap

  def replaceBy(colName: String)(f: String => String): Grid ={
    val i = indexOf(colName)
    copy(data=data.map(r => r.updated(i, f(r(i)))))
  }

  def trim: Grid = copy(data=data.map(r => r.map(_.trim)))

  lazy val maxLengths: Vector[Int] = headings.map(h => (apply(h).map(_.size) :+ h.size).max)

  lazy val maxLengthOf: Map[String, Int] = (headings zip maxLengths).toMap

  lazy val showHeadings: String =
    headings.map(h => h + (" " * (maxLengthOf(h) - h.size))).mkString("|")

  lazy val hline: String = (defaultHeadingShowSep.toString * showHeadings.size) + "\n"

  def padToMax(row: Int, pad: String = " ")(colName: String): String =
    apply(row)(colName) + (pad * (maxLengthOf(colName) - apply(row)(colName).size))

  lazy val showData: String =
    data.indices.map(r => headings.map(h =>
      padToMax(r)(h)).mkString(defaultColShowSep.toString)).toVector.mkString("\n")

  lazy val show: String =
    s"$hline$showHeadings\n$hline$showData\n$hline  dim = (nRows, nCols) = ($nRows, $nCols)"

  def print(): Unit = println(show)

  def print(maxWidth: Int): Unit =
    println(show.split('\n').map(_.take(maxWidth)).mkString("\n"))

  def csv(delim: Char = defaultDelim): String =
    headings.mkString("",delim.toString,"\n") +
      data.map(_.mkString("", delim.toString, "\n")).mkString

  def toFile(file: String, delim: Char = defaultDelim, enc: String = defaultEnc): Unit = csv(delim).toFile(file, enc)
}
