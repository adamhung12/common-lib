package me.xethh.libs.mtt.MessageTransferTool;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import me.xethh.libs.mtt.MessageTransferTool.base.Meta;
import me.xethh.libs.mtt.MessageTransferTool.base.Receiver;
import me.xethh.libs.mtt.MessageTransferTool.base.messageImpl.HeartBeatMsg;
import me.xethh.libs.mtt.MessageTransferTool.base.notficationActor.ContextBasedMsg;
import me.xethh.libs.mtt.MessageTransferTool.base.notficationActor.NotificationActor;
import me.xethh.libs.mtt.MessageTransferTool.base.receiverActors.CheckMsg;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.time.Duration;
import java.util.Date;
/**
 * Hello world!
 *
 */
@SpringBootApplication
public class JavaApp
{
    public static void systemInit(){
        if(System.getProperty("base.log.path")==null){
            File file = new File("./target/test-app/logs/");
            file.mkdirs();
            System.setProperty("base.log.path",file.toString());
        }
    }
    public static void main( String[] args )
    {
        systemInit();
//        TimeZone.setDefault(BaseTimeZone.Hongkong.timeZone());
//        SpringApplication.run(JavaApp.class, args);

        ActorSystem system = ActorSystem.create("TestSystem");
        ActorRef notifier = system.actorOf(Props.create(NotificationActor.class), "notifier");
        notifier.tell("hi",ActorRef.noSender());
        system.actorSelection("akka://TestSystem/user/notifier").resolveOne(Duration.ofSeconds(10)).whenComplete(((actorRef, throwable) ->  actorRef.tell("hello",ActorRef.noSender())));
        ActorRef receiver = system.actorOf(Props.create(Receiver.class), "receiver");
        system.scheduler().schedule(Duration.ofSeconds(10), Duration.ofMinutes(1),receiver,new CheckMsg(),system.dispatcher(), ActorRef.noSender());
        receiver.tell(new ContextBasedMsg(),ActorRef.noSender());
        int i =0;
        while (i<20){
            HeartBeatMsg msg = new HeartBeatMsg();
            msg.setData(new Object());
            msg.setId(String.format("id%06d", ++i));
            msg.setMeta(new Meta());
            msg.getMeta().setHost("192.168.101.122");
            msg.getMeta().setPort("8443");
            msg.getMeta().setDate(new Date());
            receiver.tell(msg, ActorRef.noSender());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
