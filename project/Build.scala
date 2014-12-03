import sbt._
import Keys._

object BuildSettings {
  val paradiseVersion = "2.0.0"
  val buildSettings = Defaults.defaultSettings ++ Seq(

    // Metadata
    version := "0.2-SNAPSHOT",
    scalacOptions ++= Seq("-deprecation", "-feature", "-language:experimental.macros"),
    scalaVersion := "2.10.4",

    crossScalaVersions := Seq(
      "2.10.4",
      "2.11.4"),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies += {
      if (scalaVersion.value == "2.12.0-SNAPSHOT")
        "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test"
      else
        "org.scalatest" %% "scalatest" % "2.2.0" % "test"
    },
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "acyclic" % "0.1.2" % "provided",
      "com.lihaoyi" %% "utest" % "0.2.4",
      "org.parboiled" %% "parboiled" % "2.0.1"
    ),
    addCompilerPlugin("com.lihaoyi" %% "acyclic" % "0.1.2"),
    autoCompilerPlugins := true,
    testFrameworks += new TestFramework("utest.runner.JvmFramework")
  )

  val macroBuildSettings = buildSettings ++ Seq(
    libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
    libraryDependencies ++= {
      if (scalaBinaryVersion.value == "2.10") Seq(
          compilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full),
          "org.scalamacros" %% "quasiquotes" % paradiseVersion cross CrossVersion.binary
      ) else Nil
    }
  )


}


object ScalaParserBuild extends Build {
  import BuildSettings._

  lazy val root = project.in(file("."))
    .aggregate(core, macros)

  lazy val macros = project.in(file("macros"))
    .settings(macroBuildSettings: _*)
    .settings(
      name := "scala-parser-macros",
      scalacOptions ++= Seq("-deprecation", "-feature")
     )

  lazy val core = project.in(file("core"))
    .settings(macroBuildSettings: _*)
    .settings(
      name := "scala-parser",
      javacOptions ++= Seq("-Xss2M"),
      autoCompilerPlugins := true)
    .dependsOn(macros)

}
