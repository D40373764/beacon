name := """beacon"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, SwaggerPlugin)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.0"
)

sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

EclipseKeys.preTasks := Seq(compile in Compile)
// Java project. Don't expect Scala IDE
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java          
// Use .class files instead of generated .scala files for views and routes 
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources) 

swaggerDomainNameSpaces := Seq("models")

libraryDependencies += "org.webjars" % "swagger-ui" % "2.2.5"

fork in run := true