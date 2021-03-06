/** Name of project */
name := "AkkaHttpServer"

/** Organization */
organization := "com.github.sguzman"

/** Project Version */
version := "1.0"

/** Scala version */
scalaVersion := "2.12.3"

/** Scalac parameters */
scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

/** Javac parameters */
javacOptions ++= Seq("-server")

/** Resolver */
resolvers ++= Seq(
  DefaultMavenRepository,
  Resolver.sonatypeRepo("public"),
  Resolver.typesafeRepo("releases"),
  Resolver.sbtPluginRepo("releases"),
  Resolver.jcenterRepo,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
)

/** Source Dependencies */
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.9.0-M2",
  "io.circe" %% "circe-generic" % "0.9.0-M2",
  "io.circe" %% "circe-parser" % "0.9.0-M2",
  "org.scala-lang" % "scala-library" % "2.12.3",
  "com.typesafe.akka" %% "akka-http" % "10.1.0-RC1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.8",
  "com.lihaoyi" %% "scalatags" % "0.6.7",
  "org.feijoas" %% "mango" % "0.14"
)

/** Make sure to fork on run */
fork in run := true