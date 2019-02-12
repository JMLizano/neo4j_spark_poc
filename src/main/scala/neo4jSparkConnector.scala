import org.apache.spark.sql.{DataFrame, SparkSession}
import org.neo4j.spark._


/**
  * This object encapsulates methods to access Neo4j using the neo4j-spark-connector
  *
  * https://github.com/neo4j-contrib/neo4j-spark-connector
  *
  */
object neo4jSparkConnector extends App with base {

  def load_graph(spark: SparkSession, query: String): DataFrame = {
    val neo = Neo4j(spark.sqlContext.sparkContext)
    neo.cypher(query).loadDataFrame
  }

  def load_graph_rdd(spark: SparkSession, query: String): DataFrame = {
    val neo = Neo4j(spark.sqlContext.sparkContext)
    val tr = neo.cypher(query).loadNodeRdds

    spark.createDataFrame(tr, tr.first().schema)
  }

  val df = load_graph(spark, benchmark.getQuery(1000))
  df.show()

  spark.stop()

}
