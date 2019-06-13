package me.xethh.libs.mtt.MessageTransferTool;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import me.xethh.libs.mtt.MessageTransferTool.base.messagePool.PoolActor;

public class ActorBuilder {
    public static ActorRef build(ActorSystem system){
        ActorRef notification = system.actorOf(Props.create(PoolActor.class), "notification");
        return notification;
    }
}
