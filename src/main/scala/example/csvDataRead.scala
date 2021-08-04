package example

import org.apache.log4j._
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

class csvDataRead {
  Logger.getLogger("org").setLevel(Level.ERROR)

  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  val spark: SparkSession = SparkSession.builder().master("local[*]").appName("wordCount").getOrCreate()

  val df: DataFrame = spark.read.format("csv").option("header","true").csv("resource/")
  df.createOrReplaceTempView("basicdata")

  df.show()

   private def getFromDF[A](id: Int): Future[Boolean] = {
    Future {

     @tailrec
     def recfun[A](iterate: List[Int]): Boolean = {

              Thread.sleep(500)
              iterate match {

                case Nil=> false

                case _::tail => println("==2nd case====="+tail)
                  if(findID(id)) true
                  else recfun(tail)

              }
           }

    recfun(List(1,2,3,4,5,6))
  }}


 private def updateTable(min_ret:Int):Future[Boolean]={
    Future{
    val finaldf = df.union(Seq((min_ret, "D"+min_ret.toString)).toDF("id", "Name"))
    finaldf.createOrReplaceTempView("basicdata")
    println("Inserted row in the table for ID = " + min_ret)
    finaldf.show
    true
     }
  }


  def findID(min_ret:Int): Boolean ={
    val df: DataFrame = spark.read.format("csv").option("header","true").csv("resource/")
    df.createOrReplaceTempView("basicdata")
    val result =  spark.sql("select * from basicdata where id="+min_ret)
    val status =result.count()
    if(status==1){ println("Found row in the table for ID = " + min_ret)
      result.show()
      true
    }
    else false

  }



  def myread(min_ret: Int):Future[Boolean]={
    getFromDF(min_ret)
  }
  def mywrite(min_ret:Int):Future[Boolean]= {
    if(findID(min_ret))Future{ true}
    else{
    //Thread.sleep(4000)//Will insert at end
    //Thread.sleep(2000)//Will insert at 3rd iteration
    Thread.sleep(3000)//Will insert at 5th iteration
    updateTable(min_ret)
  }}
}//End of Class

object csvDataRead extends App {
  def apply() = new csvDataRead
  val obj = new csvDataRead()
 // obj.myfun(10)
}