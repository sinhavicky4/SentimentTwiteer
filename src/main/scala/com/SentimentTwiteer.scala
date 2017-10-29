package com

import org.apache.spark.{SparkConf}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}




object SentimentTwiteer {
  def main(args: Array[String]): Unit = {

   LoggerStreaming.setLoggerLevel()
    if(args.length<4){
      System.out.print("Enter Consumer Key (API Key) Consumer Secret (API Secret)Access Token Access Token Secret")
      System.exit(1);
    }


    val Array(customer_key,customer_secrect,access_token,access_token_secret)=args.take(4)
    val favTag=args.takeRight(args.length-4)

    System.setProperty("twitter4j.oauth.consumerKey",customer_key)
    System.setProperty("twitter4j.oauth.consumerSecret",customer_secrect)
    System.setProperty("twitter4j.oauth.accessToken",access_token)
    System.setProperty("twitter4j.oauth.accessTokenSecret",access_token_secret)



    //configuration
    val conf=new SparkConf().setAppName("Sentiment").setMaster("local[5]")
    //everything begin here.getting StreamContext.Batch of 2 sec
    val scc=new StreamingContext(conf,Seconds(2))

    //Dstream
    val dStream=TwitterUtils.createStream(scc,None,favTag)

    val hashTag=dStream.flatMap(status=>{status.getText.split(" ").filter(_.startsWith("#"))})

    val topHashTag60=hashTag.map((_,1)).reduceByKeyAndWindow(_+_,Seconds(60))
      .map{case (topic,count)=>(topic,count)}.transform(_.sortByKey(false))

    val topHashTag120=hashTag.map((_,1)).reduceByKeyAndWindow(_+_,Seconds(120))
      .map{case (topic,count)=>(topic,count)}.transform(_.sortByKey(false)).saveAsTextFiles("/root/Desktop/outtweet")


    topHashTag60.foreachRDD(rdd=>{
      val topList=rdd.take(10)
      println("Popular topic in last 60 sec (%s total)".format(rdd.count()))
      topList.foreach{case (count,tag)=>println("%s (%s tweets)".format(tag,count))}

    })


 /*  topHashTag120.foreachRDD(rdd=>{
      val topList=rdd.take(10)
      println("Popular topic in last 10 sec (%s total)".format(rdd.count()))
      topList.foreach{case (count,tag)=>println("%s (%s tweets)".format(tag,count))}
    })*/
    //start streaming
    scc.start()
    scc.awaitTermination()

  }


}
