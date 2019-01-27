scalaVersion := "2.12.8"
// https://mvnrepository.com/artifact/org.jsoup/jsoup
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"
libraryDependencies += "com.typesafe.akka" %% "akka-http"   % "10.1.7"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.19"

scalacOptions ++= Seq("-unchecked", "-deprecation")

fork := true
connectInput := true
outputStrategy := Some(StdoutOutput)
