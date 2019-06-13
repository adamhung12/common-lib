package me.xethh.libs.mtt.MessageTransferTool.base.messagePool;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.japi.pf.ReceiveBuilder;
import me.xethh.libs.mtt.MessageTransferTool.base.MDCActor;

import java.time.Duration;

public abstract class ExecutionActor extends MDCActor {
    public static class NextEntity{}
    public static class Start{}
    public static class Stop{}
    public Duration duration = Duration.ofMinutes(1);
    private Cancellable cancellable;

    protected ActorRef notification;
    public ExecutionActor(ActorRef notification){
        this.notification = notification;
    }

    public abstract ReceiveBuilder buildWith(ReceiveBuilder receiveBuilder);

    @Override
    public Receive createReceive() {
        return buildWith(
                receiveBuilder()
                .match(NextEntity.class, next->{
                    notification.tell(new PoolActor.Pop(), self());
                })
                .match(Start.class, start->{
                    cancellable = getContext().getSystem().scheduler().schedule(Duration.ofSeconds(10), Duration.ofMinutes(1), notification, new PoolActor.Pop(), getContext().getDispatcher(), self());
                })
                .match(Stop.class, stop->{
                    if(cancellable!=null && !cancellable.isCancelled())
                        cancellable.cancel();
                })
        ).build();
    }
}
