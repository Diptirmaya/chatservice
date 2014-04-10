name := "akka-chat-sample"

scalaVersion := "2.9.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
	 
libraryDependencies ++= Seq(
  "se.scalablesolutions.akka" % "akka-actor" % "1.3.1",
  "se.scalablesolutions.akka" % "akka-stm" % "1.3.1",
  "se.scalablesolutions.akka" % "akka-remote" % "1.2"
)


  scalacOptions ++= Seq("-encoding", "UTF-8")
