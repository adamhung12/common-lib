package me.xethh.libs.toolkits.akka.commons.xeActor

import java.util

import me.xethh.libs.toolkits.threadLocal.XEContext
import org.slf4j.MDC

trait MDCActorMessage extends ActorMessage {
  def map():java.util.Map[String,Object]
}
class MDCActorMessageAbs() extends MDCActorMessage {
  val privateMap:java.util.Map[String, Object] = XEContext.copy()
  override def map(): util.Map[String, Object] = privateMap
}
abstract class MDCAroundReceiveActor[AM <: MDCActorMessageAbs] extends AroundReceiveActor[AM] {
  def before(receive:Receive, msg:AM): Unit={
    MDC.clear()
    msg.map().entrySet().forEach{
      entry=> XEContext.put(entry.getKey,entry.getValue)
    }
  }
  def after(receive:Receive, msg:AM): Unit={
    XEContext.copy().entrySet().forEach(x=>XEContext.remove(x.getKey))
  }
}
