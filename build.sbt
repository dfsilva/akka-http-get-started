import sbtassembly.MergeStrategy

name := "akka-http-get-started"

version := "0.1"

scalaVersion := "2.13.3"

resolvers += Resolver.mavenLocal

libraryDependencies ++= {
  val akka = "com.typesafe.akka"
  val akkaV = "2.6.9"
  val akkaHttpV = "10.2.1"
  val slickV = "3.3.3"
  Seq(
    akka %% "akka-actor-typed" % akkaV,
    akka %% "akka-stream-typed" % akkaV,
    akka %% "akka-cluster-tools" % akkaV,
    akka %% "akka-cluster-sharding-typed" % akkaV,
    akka %% "akka-serialization-jackson" % akkaV,
    akka %% "akka-slf4j" % akkaV,
    akka %% "akka-http" % akkaHttpV,
    "io.circe" %% "circe-core" % "0.13.0",
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-parser" % "0.13.0",
    "de.heikoseeberger" %% "akka-http-circe" % "1.35.0",
    "ch.megard" %% "akka-http-cors" % "1.0.0",
    "org.flywaydb" % "flyway-core" % "6.5.5",
    "com.typesafe.slick" %% "slick" % slickV,
    "com.typesafe.slick" %% "slick-hikaricp" % slickV,
    "com.github.tminglei" %% "slick-pg" % "0.19.3",
    "com.github.tminglei" %% "slick-pg_circe-json" % "0.19.3",
    "org.postgresql" % "postgresql" % "42.2.16",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) =>
    (xs map {
      _.toLowerCase
    }) match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
        MergeStrategy.discard
      case ps@(x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "plexus" :: xs =>
        MergeStrategy.discard
      case "services" :: xs =>
        MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
        MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.discard
    }
  //  case PathList("META-INF/services/io.grpc.ManagedChannelProvider") => MergeStrategy.first
  case PathList("reference.conf") => MergeStrategy.concat
  case _ => MergeStrategy.first
}
