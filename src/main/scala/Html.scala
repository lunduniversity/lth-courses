object Html {
  // val faviconMini = "image/x-icon;,"
  // val favicon = """<link href="data:$faviconMini" rel="icon" type="image/x-icon" />"""

  val styles = """
    |body {
    |    background-color: pink;
    |    font-size: medium;
    |    font-family: "Lucida Console", Monaco, monospace;
    |}
    |.button {
    |    background-color: #4CAF50;
    |    border: none;
    |    color: pink;
    |    padding: 8px 8px;
    |    text-align: center;
    |    text-decoration: none;
    |    display: inline-block;
    |    font-size: large;
    |    margin: 8px 8px;
    |    cursor: pointer;
    |}
    |.progsinput {
    |    width: 65%;
    |    font-size: medium;
    |    margin: 1px 1px;
    |}
    |.wordsinput {
    |    width: 65%;
    |    font-size: medium;
    |    margin: 1px 1px;
    |}
  """.stripMargin

  def page(body: String): String =
    s"""<!DOCTYPE html>
       |<html><head><meta charset="UTF-8"><title>KURSOMATEN@LTH</title>
       |<style>
       |$styles
       |</style>
       |</head>
       |<body>
       |$body
       |</body>
       |</html>
       |""".stripMargin

  def h1(heading: String): String = s"<h1>$heading</h1>"

  def br(soup: String): String = soup.replace("\n"," </br>\n")

  def inputFormProgWords(progs: String, words: String): String =
    s"""|<form action="" method="get">
        |  <div>
        |<p>Skriv programakronymer med blank emellan (om tom ges alla program).</p>
        |    <label for="program"><b>Programakronymer:</b> </label>
        |    <input name="progs" id="progs" value="$progs" class="progsinput">
        |    </br>
        |<p>Skriv (påbörjade) ord att söka på i kursplan med blank emellan (om tom ges alla obl. kurser).</p>
        |    <label for="ord"><b>Ord i kursplanen:</b> </label>
        |    <input name="words" id="words" value="$words" class="wordsinput">
        |    </br>
        |    <button class="button">Filtrera!</button>
        |  </div>
        |</form>
        |""".stripMargin

  def kursomatenPage(progs: String, words: String, stats: String): String =
    page(s"""
      |${h1("=== KURSOMATEN ===")}
      |<p>En snabbsökande webbapp för fritextfiltrering av LTH:s obligatoriska kursutbud.</p>
      |<p>Koden finns <a href="https://github.com/lunduniversity/lth-courses/">här</a>
      |   som öppen källkod (GPLv3).
      |   Bidrag välkomna, kontakta
      |   <a href="mailto://bjorn.regnell@cs.lth.se">bjorn.regnell@cs.lth.se</a>
      |</p>
      |
      |${inputFormProgWords(progs, words)}
      |
      |$stats
      |""".stripMargin)

  def helloPage(msg: String = "hello"): String =
    s"""|<!DOCTYPE html>
        |<html>
        |  <head>
        |    <title>Page Title</title>
        |    <style>
        |      body {background-color: powderblue;}
        |      h1 {color: red;}
        |      p {color: blue;}
        |    </style>
        |  </head>
        |  <body>
        |    <h1>Hello $msg</h1>
        |  </body>
        |</html>""".stripMargin

  def linkToCourse(id: String): String =
    s"""<a href="https://kurser.lth.se/kursplaner/18_19/$id.html">$id</a>"""

  def replaceAllCourseIdsWithLinks(soup: String, courseId: String): String =
    soup.replace(courseId, linkToCourse(courseId))
}
