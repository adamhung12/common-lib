package me.xethh.libs.mtt.MessageTransferTool.base;

import akka.actor.ActorRef;
import akka.actor.Props;
import me.xethh.libs.mtt.MessageTransferTool.base.messageImpl.HeartBeatMsg;
import me.xethh.libs.mtt.MessageTransferTool.base.notficationActor.ContextBasedMsg;
import me.xethh.libs.mtt.MessageTransferTool.base.receiverActors.AddEntityMessage;
import me.xethh.libs.mtt.MessageTransferTool.base.receiverActors.CheckMsg;
import me.xethh.libs.mtt.MessageTransferTool.base.receiverActors.HeartBeatActor;

import java.time.Duration;

public class Receiver extends MDCActor{
    private ActorRef heartBeatActor = getContext().actorOf(Props.create(HeartBeatActor.class), "heartbeatActor");
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ContextBasedMsg.class, msg->{
                    getContext().getSystem().actorSelection("user/notifier").resolveOne(Duration.ofSeconds(10)).whenComplete((actorRef, throwable) -> {
                        actorRef.tell(msg, getSender());
                    });
                })
                .match(HeartBeatMsg.class, msg->{
                    heartBeatActor.tell(new AddEntityMessage(msg.getMeta()),getSender());
                })
                .match(CheckMsg.class, msg-> heartBeatActor.tell(msg, getSender()))
                .matchAny(msg->{
                    log.info("Received unexpected message {}",msg.getClass());
                })
                .build()
                ;
    }
}
