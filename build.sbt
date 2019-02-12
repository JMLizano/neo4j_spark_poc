name := "neo4j_spark"

version := "0.1"

scalaVersion := "2.11.8"

// Spark Information
val sparkVersion = "2.3.2"

// allows us to include spark packages
resolvers += "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven/"
resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.7"

libraryDependencies ++= Seq(
  // spark core
  "org.apache.spark" %% "spark-core"   % sparkVersion,
  "org.apache.spark" %% "spark-sql"    % sparkVersion,
  
  // testing
  "org.scalatest"  %% "scalatest"  % "3.2.0-SNAP10" % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.0"       % "test",
  "com.storm-enroute" % "scalameter_2.11" % "0.10",

  // logging
  "org.apache.logging.log4j" % "log4j-api" % "2.4.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.4.1",

  //Neo4j
  "neo4j-contrib" % "neo4j-spark-connector" % "2.1.0-M4",
  "org.opencypher" % "spark-cypher" % "0.2.3",
  "org.neo4j.driver" % "neo4j-java-driver" % "1.7.2",
  "org.neo4j" % "neo4j-jdbc-driver" % "3.4.0"
)

javaOptions ++= Seq("-Xms12048M", "-Xmx12048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")