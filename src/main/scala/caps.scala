import org.apache.spark.sql.{DataFrame, SparkSession}
import java.net.URI

import org.opencypher.okapi.api.graph.GraphName
import org.opencypher.okapi.neo4j.io.Neo4jConfig
import org.opencypher.spark.api.{CAPSSession, GraphSources}
import org.opencypher.spark.api.CAPSSession.RecordsAsDF
import org.opencypher.spark.impl.io.neo4j.external.Neo4j
import org.opencypher.spark.impl.CAPSConverters._

/**
  * This object encapsulates methods to access Neo4j using the CAPS connector
  *
  * CAPS: https://github.com/opencypher/cypher-for-apache-spark
  *
  * This library tries to first extract the schema from the Neo4j database,
  * it will fail if it encounters any not supported types (Eg. ZONED_DATE_TIME).
  * See https://github.com/neo4jrb/neo4j/issues/1522
  */
object caps extends App with base {

  lazy val getNeo4jConfig: Neo4jConfig = new Neo4jConfig(
                                            uri=new URI(uri),
                                            user="neo4j",
                                            password=Some(password),
                                            encrypted = true
                                         )

  def load_graph(spark: SparkSession, query: String): DataFrame = {

    val neo4jconf: Neo4jConfig = getNeo4jConfig
    implicit val session = CAPSSession.create(spark)

    val pgds = GraphSources.cypher.neo4j(neo4jconf, omitIncompatibleProperties=true)
    pgds.graph(GraphName("graph")).cypher(query).records.asDataFrame
  }

  def load_graph_neo4j_api(spark: SparkSession, query: String): DataFrame = {

    val neo4jconf: Neo4jConfig = getNeo4jConfig

    // This is not the intended API. In fact this is using the same code as neo4j-spark-connector
    val neo4j = Neo4j(neo4jconf, spark)
    val tr = neo4j.cypher(query).loadNodeRdds

    spark.createDataFrame(tr, tr.first().schema)
  }

  def load_graph_entity_api(spark: SparkSession, relationship: String): DataFrame = {

    implicit val session = CAPSSession.create(spark)
    val neo4jconf: Neo4jConfig = getNeo4jConfig

    val neo4jSource = GraphSources.cypher.neo4j(neo4jconf, omitIncompatibleProperties=false)
    neo4jSource.graph(GraphName("graph")).relationships(relationship).asCaps.df
  }

  val df = load_graph_neo4j_api(spark, benchmark.getAddressQuery(1000))

  df.cache().show()

  spark.stop()

}
