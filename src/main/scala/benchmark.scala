import org.scalameter.api._


object benchmark extends Bench.LocalTime with base {

  def  getQuery(limit: Int): String = {
    "MATCH p=(a)-[r:TRANSFER_TO]->(b) " +
      "RETURN a.address as from, b.address as to, r.amount as amount " +
      s"LIMIT $limit "
  }

  def  getAddressQuery(limit: Int): String = {
    "MATCH (a: Address)" +
    "RETURN a.address as address " +
    s"LIMIT $limit "
  }


  val limits = Gen.range("size")(1000000, 2000000, 1000000)

  performance of "Neo4j" in {
    measure method "spark-connector" config (
      exec.benchRuns -> 3,
      exec.maxWarmupRuns -> 0
    ) in {
      using(limits) in {
        limit => {
          val df = neo4jSparkConnector.load_graph(spark, getQuery(limit)).cache()
          println(df.first())
          df.unpersist()
        }
      }
    }
    measure method "caps" config (
      exec.benchRuns -> 3,
      exec.maxWarmupRuns -> 0
    ) in {
      using(limits) in {
        limit => {
          val df = caps.load_graph_neo4j_api(spark, getQuery(limit)).cache()
          println(df.first())
          df.unpersist()
        }
      }
    }
    measure method "java-driver" config (
      exec.benchRuns -> 3,
      exec.maxWarmupRuns -> 0
    ) in {
      using(limits) in {
        limit => {
          val df = javaDriver.load_graph(spark, getQuery(limit)).cache()
          println(df.first())
          df.unpersist()
        }
      }
    }
  }
}


