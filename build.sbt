scalaVersion := "2.13.17"
// https://mvnrepository.com/artifact/org.jsoup/jsoup
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"
libraryDependencies += "com.typesafe.akka" %% "akka-http"   % "10.1.12"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.26"

scalacOptions ++= Seq("-unchecked", "-deprecation")

fork := true
connectInput := true
outputStrategy := Some(StdoutOutput)
