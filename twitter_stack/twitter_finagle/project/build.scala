import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import com.twitter.scrooge.ScroogeSBT

object Builds extends Build {
  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1-SNAPSHOT",
    organization := "com.twitter",
    scalaVersion := "2.10.2"
  )

  lazy val app = Project("app", file("."),
    settings = buildSettings 
      ++ assemblySettings 
      ++ ScroogeSBT.newSettings
    ) settings(
      // packageOptions in assembly ~= { os => os filterNot {_.isInstanceOf[Package.MainClass]} }
      // mainClass in assembly := Some("andy.ThriftServer")
      // your settings here
      // mainClass in assembly := Some("com.twitter.finagle.example.thrift.ThriftServer")
      // libraryDependencies ++= Seq(
        // "com.twitter" %% "scrooge-core" % "3.8.0",
        // "org.apache.thrift" % "libthrift" % "0.8.0",
        // "com.twitter" %% "finagle-thrift" % "6.5.2"
        // )
    )
}

// java -cp andy.jar andy.ThriftClient
// Main-Class: andy.ThriftClient


