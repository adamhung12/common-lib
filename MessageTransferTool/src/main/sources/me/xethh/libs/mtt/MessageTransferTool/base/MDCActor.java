package me.xethh.libs.mtt.MessageTransferTool.base;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.slf4j.MDC;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.Map;

public abstract class MDCActor extends AbstractActor {
    protected LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public void aroundReceive(PartialFunction<Object, BoxedUnit> receive, Object msg) {
        if(msg instanceof MDCEntity){
            log.info("Received MDCEntity {}",msg.getClass());
            MDC.clear();
            for(Map.Entry<String,String> entry:((MDCEntity) msg).getMaps().entrySet())
                MDC.put(entry.getKey(),entry.getValue());
            super.aroundReceive(receive, msg);
            MDC.clear();
        }
        else{
            log.info("Received Non MDCEntity {}", msg.getClass());
            super.aroundReceive(receive,msg);
        }
    }
}
