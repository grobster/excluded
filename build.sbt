import AssemblyKeys._

assemblySettings

name := "PST Zipper"
organization := "org.grobster"
version := "1.0.7"
scalaVersion := "2.12.0-M4"

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.92-R10"

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))

fork in run := true