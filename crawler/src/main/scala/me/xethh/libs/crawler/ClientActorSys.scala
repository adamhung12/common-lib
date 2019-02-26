package me.xethh.libs.crawler

import akka.actor.{ActorSystem, Cancellable}

import scala.concurrent.duration._

class ClientActorSys(private[this] val system:ActorSystem = ActorSystem("TempSystem")) {
  import system.dispatcher

  //This will schedule to send the Tick-message
  //to the tickActor after 0ms repeating every 50ms
  private[this] var cancellable:Cancellable = null

  def schedule(
                runnable: Runnable,
                delay : FiniteDuration = 0 millisecond,
                interval : FiniteDuration = 1 second
              ) = {

    if(cancellable!=null)
      cancellable.cancel()
    cancellable = system.scheduler.schedule(
      delay,
      interval,
      runnable)
    cancellable
  }
}
object ClientActorSys {

  def main(args: Array[String]): Unit = {
    val client = new ClientActorSys
    client.schedule(()=>println("hi"))
    Thread.sleep(50*60*1000)
  }

}
