package example

import org.scalatest.AsyncFlatSpec

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class csvDataReadTest extends AsyncFlatSpec {

  it should "verify the future returns with for and yield" in {
    println("========Testing======================")
    val cc =new csvDataRead()

    val readobj=cc.myread(11)
    val writeobj=cc.mywrite(11)

    for{
       res <- Future{Await.result(readobj,Duration.Inf)}
       res1 <-Future{Await.result(writeobj,Duration.Inf)}

    }yield {assert(res && res1)}//{assert(res||res1 == true)}


  }}