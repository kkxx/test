name := "hello"

version := "1.0"

// scalaVersion := "2.9.2"
scalaVersion := "2.10.2"

autoCompilerPlugins := true

// addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.2")
addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")

scalacOptions += "-P:continuations:enable"


// resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "ScalaTools Snapshots nexus" at "http://nexus.scala-tools.org/content/repositories/snapshots",
  "repository.jboss.org" at "https://repository.jboss.org/nexus/content/repositories/releases/",
  "Akka Repository" at "http://akka.io/repository",
  "Scala Tools" at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  "io.netty" % "netty" % "3.6.6.Final"
)

ivyXML := <dependencies>
    <exclude module="log4j" />
    <exclude module="slf4j-log4j12" />
    <exclude module="slf4j-api-1.6.0"  />
    <exclude org="org.jboss.netty" />
  </dependencies>
