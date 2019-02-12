# Neo4j Spark POC

There are a lot of options to connect from Spark to Neo4j. This project is intended to test them out in order to help to make a decision choosing one.

The options compared here are :

* Neo4j-spark connector: Oficial connector (neo4-contrib) between neo4j and Spark

* Cypher for apache spark (CAPS): Third party Spark module to bring Cypher (Graph query language used by Neo4j) into Spark. There is an open Spark Project Improvement Proposal (SPIP) to make it a core part of Spark for Spark 3.0

* Neo4j driver for Java: Official neo4j driver for Java

* Neo4j JDBC driver: Official (neo4-contrib) JDBC driver for connecting to Neo4j

