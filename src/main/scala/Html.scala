object Html {
  // val faviconMini = "image/x-icon;,"
  // val favicon = """<link href="data:$faviconMini" rel="icon" type="image/x-icon" />"""

  val styles = """
    |body {
    |    background-color: pink;
    |    font-size:medium;
    |    font-family: "Lucida Console", Monaco, monospace;
    |}
    |.button {
    |    background-color: #4CAF50;
    |    border: none;
    |    color: pink;
    |    padding: 4px 4px;
    |    text-align: center;
    |    text-decoration: none;
    |    display: inline-block;
    |    font-size: large;
    |    margin: 4px 4px;
    |    cursor: pointer;
    |}
    |.progsinput {
    |    width: 25%;
    |    font-size: medium;
    |    margin: 1px 1px;
    |}
    |.wordsinput {
    |    width: 25%;
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

  def br(soup: String): String = soup.replaceAllLiterally("\n"," </br>\n")

  def inputFormProgWords(progs: String, words: String): String =
    s"""|<form action="" method="get">
        |  <div>
        |    <label for="program">Programakronymer: </label>
        |    <input name="progs" id="progs" value="$progs" class="progsinput">
        |    </br>
        |    <label for="ord">    Ord i kursplanen: </label>
        |    <input name="words" id="words" value="$words" class="wordsinput">
        |    </br>
        |    <button class="button">Filtrera!</button>
        |  </div>
        |</form>
        |""".stripMargin

  def kursomatenPage(progs: String, words: String, stats: String): String =
    page(s"""
      |${h1("=== KURSOMATEN ===")}
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
}
