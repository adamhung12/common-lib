package me.xethh.libs.mtt.MessageTransferTool.base.messagePool;

import akka.actor.ActorRef;
import me.xethh.libs.mtt.MessageTransferTool.base.AckMessage;
import me.xethh.libs.mtt.MessageTransferTool.base.MDCActor;
import me.xethh.libs.mtt.MessageTransferTool.commons.MutableTuple2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PoolActor extends MDCActor {
    public static enum PoolEntityStatus{
        Pending, Processing, Completed
    }

    public static class PoolEntity{
        private PoolEntityStatus status;
        private Object object;

        @Override
        public String toString() {
            return "PoolEntity{" +
                    "status=" + status +
                    ", object=" + object +
                    '}';
        }

        public PoolEntity(PoolEntityStatus status, Object object) {
            this.status = status;
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public PoolEntityStatus getStatus() {
            return status;
        }

        public void setStatus(PoolEntityStatus status) {
            this.status = status;
        }
    }

    private Map<String, MutableTuple2<PoolEntity, ActorRef>> pool = new HashMap<>();


    public Map<String, MutableTuple2<PoolEntity, ActorRef>> getPool() {
        return pool;
    }

    public static class Push {
        private String id;
        private Object object;

        public Push(String id, Object object) {
            this.id = id;
            this.object = object;
        }

        @Override
        public String toString() {
            return "Push{" +
                    "id='" + id + '\'' +
                    ", object=" + object +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
    public static class Pop{}
    public static class Complete {
        private String id;

        public Complete(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Complete{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }
    public static class Error {
        private String id;

        public Error(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    public static class PoolSize{}

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Push.class, msg->{
                    pool.put(msg.getId(), new MutableTuple2(new PoolEntity(PoolEntityStatus.Pending,msg.getObject()), getSender()));
                })
                .match(Pop.class, msg->{
                    if(pool.size()>0){
                        Optional<Map.Entry<String, MutableTuple2<PoolEntity, ActorRef>>> obj = pool.entrySet().stream().filter(x -> x.getValue().getV1().getStatus() == PoolEntityStatus.Pending).findFirst();
                        if(obj.isPresent()){
                            Map.Entry<String, MutableTuple2<PoolEntity, ActorRef>> entry = obj.get();
                            entry.getValue().getV1().setObject(PoolEntityStatus.Processing);
                            getSender().tell(entry, self());
                        }
                    }
                })
                .match(Error.class, msg->{
                    if(pool.containsKey(msg.getId())){
                        MutableTuple2<PoolEntity, ActorRef> obj = pool.remove(msg.getId());
                        obj.getV2().tell(new GeneralAckMessage(msg.getId(), AckMessage.ProcessFailed), self());
                    }
                    else
                        log.info("Fail to Ack Error[id {} not found]",msg.getId());
                })
                .match(Complete.class, msg->{
                    if(pool.containsKey(msg.getId())){
                        MutableTuple2<PoolEntity, ActorRef> obj = pool.remove(msg.getId());
                        obj.getV2().tell(new GeneralAckMessage(msg.getId(), AckMessage.Processed), self());
                    }
                    else
                        log.info("Fail to Ack Complete[id {} not found]",msg.getId());
                })
                .match(PoolSize.class, msg->getSender().tell(pool.size(),getSelf()))
                .match(String.class, msg->{
                    log.info("Received text message {}",msg);
                })
                .build();
    }
}
