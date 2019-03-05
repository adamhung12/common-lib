package me.xethh.libs.crawler

import java.util.Date

import akka.actor.{Actor, ActorRef, ActorSystem, Cancellable, Props}
import me.xethh.libs.crawler.ActorSystem._

import scala.concurrent.duration._

object ActorSystem{
  case class Interval(interval:FiniteDuration)
  case class Delay(delay:FiniteDuration)
  case class POPSize(size:Int)
  case class Stop()
  case class Start()

  case class Message(msg:Any, sender:ActorRef, receiver:ActorRef)
  case class Action(action:(ActorRef, ActorRef)=>Unit, sender:ActorRef, receiver:ActorRef)

}
object ClientActorSys {
  class FlowActor(var interval:FiniteDuration, var delay:FiniteDuration = 0 millisecond, var popSize:Int) extends Actor{
    import scala.collection.mutable.Queue
    private[this] val messageQueue = Queue[(Any, ActorRef, ActorRef)]()

    def messages(oper:(Queue[(Any,ActorRef,ActorRef)])=>Unit) :Queue[(Any, ActorRef, ActorRef)]= synchronized{
      oper(messageQueue)
      messageQueue
    }
    var scheduler:Cancellable =null

    val runnable:Runnable = () => {
      messages((queue)=>{
        println(new Date)
        val poping = scala.math.min(popSize, queue.size)
        if( poping!=0){
          println(s"Poping ${poping} items")
          (1 to poping).foreach(x=>{
            val (msg, sender, receiver) = queue.dequeue()
            if(msg.isInstanceOf[(ActorRef,ActorRef)=>Unit]){
              println("Action")

              msg.asInstanceOf[(ActorRef,ActorRef)=>Unit](sender,receiver)
            }
            else{
              println("Message")
              sender.tell(msg, receiver)
            }
          })
        }
        else{
          println("Non to pop")
        }
      })
    }

    override def receive: Receive = {
      case Action(x,y,z) => messages((queue)=>{
        queue.enqueue((x,y,z))
      })
      case Message(x,y,z) => messages((queue)=>{
        queue.enqueue((x,y,z))
      })
      case Interval(interval) => this.interval = interval
      case Delay(delay) => this.delay = delay
      case POPSize(size) => this.popSize = size
      case Start => {
        println("Scheduler start")
        if(scheduler!=null && !scheduler.isCancelled)
          scheduler.cancel()

        implicit val executionContext = context.system.dispatcher
        scheduler = context.system.scheduler.schedule(delay , interval , runnable)
      }
      case Stop => {
        println("Scheduler stop")
        this.scheduler.cancel()
      }
    }
  }

  class SendActor extends Actor{
    override def receive: Receive = {
      case x => {
        println(s"called sender[${x}]")
        sender() ! x
      }
    }
  }
  class ReceiveActor extends Actor{
    override def receive: Receive = {
      case x => {
        println(s"called receiver[${x}]")
      }
    }
  }

  def main(args: Array[String]): Unit = {
//    val client = new ClientActorSys
//    client.schedule(()=>println("hi"))
//    Thread.sleep(50*60*1000)
    val system = akka.actor.ActorSystem("Hi")
    val flow = system.actorOf(Props(classOf[FlowActor], 10 second, 10 second, 3),"flow")
    val sender:ActorRef = system.actorOf(Props[SendActor],"send")
    val receiver:ActorRef = system.actorOf(Props[ReceiveActor],"receive")

    flow.tell(Action((s,r)=>{println("x")},sender,receiver), ActorRef.noSender)
    flow.tell(Message("a".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Message("b".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Message("c".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Message("d".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Message("e".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Message("f".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Message("g".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Start, ActorRef.noSender)
    Thread.sleep(1000*63)
    flow.tell(Message("h".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Message("i".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    flow.tell(Message("j".asInstanceOf[Any], sender, receiver), ActorRef.noSender)
    Thread.sleep(1000*23)
    flow.tell(Message("k".asInstanceOf[Any], sender, receiver), ActorRef.noSender)



  }

}
