//import AssemblyKeys._

// name := "tf"

// version := "1.0"

// scalaVersion := "2.10.2"

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")

scalacOptions += "-P:continuations:enable"

resolvers ++= Seq(
  "oschina" at "http://maven.oschina.net/service/local/repositories/central/content",
  "central.maven" at "http://central.maven.org/maven2",
  "twitter" at "http://maven.twttr.com/"
)

libraryDependencies ++= Seq(
  "com.twitter" %% "twitter-server" % "1.0.3",
  "com.twitter" %% "scrooge-core" % "3.8.0",
  "org.apache.thrift" % "libthrift" % "0.8.0",
  "com.twitter" %% "finagle-thrift" % "6.5.2"
)

unmanagedBase <<= baseDirectory { base => base / "libs" }

// http://twitter.github.io/scrooge/SBTPlugin.html
// com.twitter.scrooge.ScroogeSBT.newSettings

// https://github.com/sbt/sbt-assembly
// assemblySettings
