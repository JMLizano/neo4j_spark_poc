import org.apache.spark.sql.Row
import java.sql.{DriverManager, ResultSet}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object jdbc extends App with base{

  val con = DriverManager.getConnection(s"jdbc:neo4j:$uri", "neo4j", password)
  val stmt = con.prepareStatement(benchmark.getQuery(1000))
  val rs = stmt.executeQuery

  val records = new Iterator[Row] {
    def getRows(rs: ResultSet): Row= {
      val numColumns = rs.getMetaData.getColumnCount
      val columns = (1 to numColumns).map { i =>
        rs.getString(i)
      }
      Row.fromSeq(columns)
    }
    def hasNext = rs.next()
    def next() = getRows(rs)
  }.toList

  val schema = StructType(List(
    StructField("a", StringType, nullable = true),
    StructField("b", StringType, nullable = true),
    StructField("c", StringType, nullable = true))
  )
  val rdd = spark.sparkContext.makeRDD(records)
  spark.createDataFrame(rdd, schema).show()

  con.close()

}
