name := """movie_mediatheque"""
organization := "hawk"

version := "1.0-SNAPSHOT"

scalaVersion := "2.13.7"


lazy val root = (project in file(".")).enablePlugins(PlayScala)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

libraryDependencies += guice
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"