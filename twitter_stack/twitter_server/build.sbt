name := "ts"

version := "1.0"

scalaVersion := "2.10.2"

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")

scalacOptions += "-P:continuations:enable"

resolvers ++= Seq(
  "oschina" at "http://maven.oschina.net/service/local/repositories/central/content",
  "twitter" at "http://maven.twttr.com/"
)

libraryDependencies ++= Seq(
  "com.twitter" %% "twitter-server" % "1.0.3"
)

unmanagedBase <<= baseDirectory { base => base / "libs" }
