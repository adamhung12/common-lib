package me.xethh.libs.mtt.MessageTransferTool.base.receiverActors;

import akka.actor.ActorRef;
import akka.actor.dsl.Creators;
import me.xethh.libs.mtt.MessageTransferTool.base.MDCActor;
import me.xethh.libs.mtt.MessageTransferTool.base.Meta;
import me.xethh.libs.mtt.MessageTransferTool.base.notficationActor.ApplicationDownMsg;
import me.xethh.utils.dateManipulation.DateFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HeartBeatActor extends MDCActor {

    private List<HeartBeatEntity> entities = new ArrayList<>();


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddEntityMessage.class, msg->{
                    Meta meta = msg.getMeta();
                    HeartBeatEntity entity = new HeartBeatEntity();
                    entity.setHost(meta.getHost());
                    entity.setPort(meta.getPort());
                    if(entities.contains(entity)){
                        HeartBeatEntity origin = entities.get(entities.indexOf(entity));
                        if(origin.getDate().before(meta.getDate())) {
                            log.info("updated new date to {}:{}",meta.getHost(),meta.getPort());
                            origin.setDate(meta.getDate());
                        }
                        else{
                            log.info("Received {} but the date field before existing one ",meta);
                        }
                    }
                    else{
                        log.info("{} not exist, create new one",meta);
                        entity.setDate(meta.getDate());
                        entities.add(entity);
                    }
                })
                .match(RemoveEntityMessage.class, msg->{
                    Meta meta = msg.getMeta();
                    HeartBeatEntity entity = new HeartBeatEntity();
                    entity.setHost(meta.getHost());
                    entity.setPort(meta.getPort());
                    if(entities.contains(entity)){
                        log.info("Removing {}", entity);
                        entities.remove(entity);
                    }
                    else
                        log.info("{} not exist in heart beat entity list",entity);
                })
                .match(CheckMsg.class, msg->{
                    Date date = DateFactory.now().addMins(-1).asDate();
                    List<HeartBeatEntity> downed = entities.stream().filter(x -> x.getDate().before(date)).collect(Collectors.toList());
                    if(downed.size()>0) {
                        log.info("{} downed application found", downed.size());
                        getContext().getSystem().actorSelection("user/notifier").resolveOne(Duration.ofSeconds(10)).whenComplete((actorRef, throwable) -> {
                            for (HeartBeatEntity e : downed) {
                                log.info("Sending down application to notification {]", e);
                                ApplicationDownMsg newMsg = new ApplicationDownMsg();
                                newMsg.setHeartBeatEntity(e);
                                actorRef.tell(e, ActorRef.noSender());
                            }
                        });
                    }
                    else
                        log.info("No downed application");

                })
                .build()
                ;
    }
}
