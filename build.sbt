import com.typesafe.startscript.StartScriptPlugin

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

name := "hello"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "twitter4j.org Repository" at "http://twitter4j.org/maven2"

resolvers += "twitter-repo" at "http://maven.twttr.com"

libraryDependencies ++= Seq(
  "com.twitter" % "finagle-core_2.9.1" % "4.0.2",
  "com.twitter" % "finagle-http_2.9.1" % "4.0.2",
  "com.twitter" % "finagle-stream_2.9.1" % "4.0.2",
  "org.twitter4j" % "twitter4j-core" % "[2.2,)"
)
