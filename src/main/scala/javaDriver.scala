import java.net.URI

import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.neo4j.driver.v1.{AuthTokens, GraphDatabase, Record, Value}


import scala.collection.JavaConversions._


object javaDriver extends App with base{

  def load_graph(spark: SparkSession, query: String): DataFrame = {
    // Get connection to Neo4j
    val driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", password))
    val session = driver.session

    // Execute query
    val result = session.run(query)

    // Transform Driver response to Dataframe
    val schema = createSchema(result.peek())
    val records = result.list().toList.map { r =>
      Row.fromSeq(r.values().map(_.asObject()))
    }
    spark.createDataFrame(records, schema)
  }

  def createSchema(r: Record): StructType = {
    val fields = r.keys().map { k =>
      val t = r.get(k).`type`().name()
      t match {
        case "FLOAT" => StructField(k, DoubleType, nullable = true)
        case "STRING" => StructField(k, StringType, nullable = true)
        case "BOOLEAN" => StructField(k, BooleanType, nullable = true)
        case "DATE_TIME" => StructField(k, DateType, nullable = true)
        case _ => StructField(k, StringType, nullable = true)
      }
    }
    StructType(fields)
  }

  val df = load_graph(spark, benchmark.getQuery(1000))
  df.show()

}
