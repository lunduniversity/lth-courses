object Html {
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
