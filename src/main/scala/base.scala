import org.apache.spark.sql.SparkSession
import scala.util.Properties


trait base {

  val password: String = Properties.envOrElse("NEO4J_PASSWORD", "")
  val uri: String = Properties.envOrElse("NEO4J_URL", "bolt://192.168.0.55")

  val spark: SparkSession = SparkSession.builder
    .appName("Neo4j")
    .master("local[7]")
    .config("spark.driver.memory", "12g")
    .config("spark.neo4j.bolt.url", uri)
    .config("spark.neo4j.bolt.user", "neo4j")
    .config("spark.neo4j.bolt.password", password)
    .getOrCreate()

}
