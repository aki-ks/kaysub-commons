organization := "de.kaysubs"
name := "kaysub-commons"
version := "0.1-SNAPSHOT"

description := "Utility classes shared between multiple project."

javacOptions in (Compile, compile) ++= Seq("-source", "1.8", "-target", "1.8", "-g:lines")

crossPaths := false // drop off Scala suffix from artifact names.
autoScalaLibrary := false // exclude scala-library from dependencies

val httpClientVersion = "4.5.5"
val gsonVersion = "2.8.2"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % httpClientVersion
