name := "SentimentTwiteer"

version := "0.1"

scalaVersion := "2.11.11"


libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0"
// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-twitter_2.11
libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.11" % "1.6.3" % "provided"
//libraryDependencies+="org.apache.bahir" %% "spark-streaming-twitter" % "2.0.0"
//--packages "org.apache.spark:spark-streaming-twitter_2.11:1.6.3"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.2.0"
        