package me.xethh.libs.mtt.MessageTransferTool.base.notficationActor;

import me.xethh.libs.mtt.MessageTransferTool.base.MDCActor;

public class NotificationActor extends MDCActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApplicationDownMsg.class, msg->{
                    log.info("Applicaton down {}",msg);
                })
                .matchAny(msg->{
                    log.info("Receive notification: {}",msg);
                })
                .build();
    }
}
